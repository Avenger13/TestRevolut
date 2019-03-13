package ru.interview.revoluttest.data.repository

import ru.interview.revoluttest.data.net.model.response.RatesDTO

interface IRatesRepository {
    suspend fun getRates(base: String): RatesDTO
}