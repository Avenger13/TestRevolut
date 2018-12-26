package ru.interview.revoluttest

import ru.interview.revoluttest.net.Api

class CurrencyRepository : CurrencyContract.Repository {
    override fun getCurrencies(base: String) = Api.api.curs(base)
}