package com.example.currencyapp.utils


sealed class Resource<out R> {

    object Loading : Resource<Nothing>()
    class Success<out T>(val data:T) : Resource<T>()
    class Fail(val message:String) : Resource<Nothing>()
}
