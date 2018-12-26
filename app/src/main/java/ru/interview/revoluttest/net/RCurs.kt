package ru.interview.revoluttest.net

import com.google.gson.JsonObject

data class RCurs(
    val base: String,
    val date: String,
    val rates: JsonObject
)