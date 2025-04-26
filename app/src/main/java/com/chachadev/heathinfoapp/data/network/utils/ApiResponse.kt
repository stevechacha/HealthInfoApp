package com.chachadev.heathinfoapp.data.network.utils



data class ApiResponse<T>(
    val status: String,
    val message: String,
    val data: T?
)