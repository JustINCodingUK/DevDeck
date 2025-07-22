package dev.justincodinguk.devdeck.util

sealed class Result<out T> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    data object Loading : Result<Nothing>()
}

fun <T> kotlin.Result<T>.toLiveResult() : Result<T> {
    return if(isSuccess) {
        Result.Success(getOrNull()!!)
    } else {
        Result.Error(exceptionOrNull()!!)
    }
}