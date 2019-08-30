package com.wmapp.networking

/**
 * Class for holding network processing error.
 */
data class NetworkProcessingError(val errorMessage:String): Throwable(errorMessage)