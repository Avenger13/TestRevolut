package ru.interview.revoluttest.rates

import ru.interview.revoluttest.data.net.Api
import ru.interview.revoluttest.data.repository.IRatesRepository

class RatesRepository : IRatesRepository {
    override suspend fun getRates(base: String) = Api.revolut.curs(base).await()
}