package ru.interview.revoluttest.rates

import ru.interview.revoluttest.business.interactor.IRatesInteractor
import ru.interview.revoluttest.business.mapping.RatesDtoToModelConverter
import ru.interview.revoluttest.business.model.RatesModel
import ru.interview.revoluttest.data.repository.IRatesRepository

class RatesInteractor(private val repository: IRatesRepository) : IRatesInteractor {
    override suspend fun getRates(base: String): RatesModel {
        repository.getRates(base)
            .let { return RatesDtoToModelConverter(base, true).convert(it) }


    }

}