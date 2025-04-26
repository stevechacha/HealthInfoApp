package com.chachadev.heathinfoapp.data.network.utils

class ApiException(
    val statusCode: Int = 0,
    val statusMessage: String
) : Throwable(statusMessage)