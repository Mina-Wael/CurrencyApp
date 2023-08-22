package com.example.currencyapp.domain.repository

import com.example.currencyapp.domain.pojo.SymbolsResponse
import com.example.currencyapp.utils.Resource
import kotlinx.coroutines.flow.Flow


interface MainRepository {

    fun getSymbols(accessKey: String): Flow<Resource<SymbolsResponse>>

}