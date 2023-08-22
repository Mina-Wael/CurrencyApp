package com.example.currencyapp.di

import com.example.currencyapp.data.remote.CurrencyApi
import com.example.currencyapp.data.repository.MainRepositoryImpl
import com.example.currencyapp.domain.repository.MainRepository
import com.example.currencyapp.utils.Constants.BASE_URL
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@dagger.Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    @Singleton
    fun provideCurrencyApi(): CurrencyApi =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build().create(CurrencyApi::class.java)

    @Provides
    fun provideMainRepository(api: CurrencyApi): MainRepository = MainRepositoryImpl(api)
}