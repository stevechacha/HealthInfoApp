package com.chachadev.heathinfoapp.data.network.utils

sealed class Environment(val url: String) {
    data object Main : Environment("http://127.0.0.1:8000/")
}
