package com.example.currencyapp.utils


sealed class ResultState<out R> {
    object Loading : ResultState<Nothing>()
    class Success<out T>(val data: T) : ResultState<T>()
    class Fail(val message: String) : ResultState<Nothing>()
}
