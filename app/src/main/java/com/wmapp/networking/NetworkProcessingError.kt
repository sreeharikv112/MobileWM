package com.wmapp.networking

data class NetworkProcessingError(val errorMessage:String): Throwable(errorMessage)