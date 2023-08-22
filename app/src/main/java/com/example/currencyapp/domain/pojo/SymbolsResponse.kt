package com.example.currencyapp.domain.pojo

import com.google.gson.internal.LinkedTreeMap


data class SymbolsResponse(
    val success: Boolean?,
    val symbols: LinkedTreeMap<String, String>
)
