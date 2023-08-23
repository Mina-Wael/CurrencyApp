package com.example.currencyapp.data.remote

import com.example.currencyapp.domain.pojo.SymbolsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface CurrencyApi {
    @GET("symbols")
    suspend fun getSupportedSymbols(@Query("access_key") access_key: String): Response<SymbolsResponse>

    @GET("convert")
    suspend fun convertCurrency(
        @Query("access_key") access_key: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: Double
    )

    @GET("{date}")
    suspend fun getHistorical(
        @Path("date") date: String,
        @Query("access_key") access_key: String,
        @Query("base") base: String,
        @Query("symbols") symbols: List<String>
    )
}