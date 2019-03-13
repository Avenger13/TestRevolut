package ru.interview.revoluttest.data.net.model.response

import com.google.gson.JsonObject

data class RatesDTO(
    val base: String,
    val date: String,
    val rates: JsonObject
)