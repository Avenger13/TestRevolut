package ru.interview.revoluttest.data.net.api

import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query
import ru.interview.revoluttest.data.net.model.response.RatesDTO

interface Revolut {
    @GET("latest")
    fun curs(@Query("base") base: String): Deferred<RatesDTO>
}