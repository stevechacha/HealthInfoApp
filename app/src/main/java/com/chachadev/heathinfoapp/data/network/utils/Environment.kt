package com.chachadev.heathinfoapp.data.network.utils

sealed class Environment(val url: String) {
    data object Main : Environment("http://10.0.2.2:8000/")
}
