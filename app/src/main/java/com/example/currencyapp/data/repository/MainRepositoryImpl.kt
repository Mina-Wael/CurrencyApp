package com.example.currencyapp.data.repository

import com.example.currencyapp.data.remote.CurrencyApi
import com.example.currencyapp.domain.pojo.SymbolsResponse
import com.example.currencyapp.domain.repository.MainRepository
import com.example.currencyapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException


class MainRepositoryImpl(val api: CurrencyApi) : MainRepository {


    override fun getSymbols(accessKey: String): Flow<Resource<SymbolsResponse>> = flow {
        emit(Resource.Loading)

        try {
            var res = api.getSupportedSymbols(accessKey)
            if (res.isSuccessful)
                emit(Resource.Success(res.body()!!))
            else
                emit(Resource.Fail(res.message()))

        } catch (e: HttpException) {
            emit(Resource.Fail(e.localizedMessage ?: "An expected error occurred"))
        } catch (e: IOException) {
            emit(Resource.Fail("Couldn't reach server. check your internet connection"))
        }
    }

}