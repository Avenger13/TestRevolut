package ru.interview.revoluttest.business.interactor

import ru.interview.revoluttest.business.model.RatesModel


interface IRatesInteractor{
    suspend fun getRates(base:String): RatesModel
}