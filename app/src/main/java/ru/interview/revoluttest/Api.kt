package ru.interview.revoluttest

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object Api {
    private val client = OkHttpClient.Builder().build()

    private val revolut = Retrofit.Builder()
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl("https://revolut.duckdns.org/")
        .build()

    val api: Service = revolut.create(Service::class.java)


    interface Service {
        @GET("latest")
        fun curs(@Query("base") base: String): Deferred<RCurs>
    }
}