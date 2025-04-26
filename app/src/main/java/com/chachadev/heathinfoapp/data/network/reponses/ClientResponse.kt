package com.chachadev.heathinfoapp.data.network.reponses

data class ClientResponse(
    val id: Int,
    val name: String,
    val age: Int,
    val gender: String,
    val programIds: List<Int>
)

