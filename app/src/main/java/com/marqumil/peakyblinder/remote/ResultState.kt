package com.marqumil.peakyblinder.remote

sealed class ResultState<out T>{
    data class Success<out T>(val value: T): ResultState<T>()
    data class Failure(val throwable: Throwable): ResultState<Nothing>()
    object Loading : ResultState<Nothing>()

}