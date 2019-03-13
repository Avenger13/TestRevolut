package ru.interview.revoluttest.business.mapping

import com.google.gson.JsonElement
import ru.interview.revoluttest.business.model.Rate
import ru.interview.revoluttest.business.model.RatesModel
import ru.interview.revoluttest.core.IConverter
import ru.interview.revoluttest.data.net.model.response.RatesDTO

class RatesDtoToModelConverter(private val base: String, private val sort: Boolean) : IConverter<RatesDTO, RatesModel> {
    override fun convert(t: RatesDTO): RatesModel {
        val list = ArrayList<Rate>(t.rates.size() + 1)
            .also { it.add(Rate(base, 1.0)) }

        t.rates.entrySet()
            .let { mSet -> if (sort) mSet.sortedBy { it.key } else mSet }
            .forEach { entry -> list.add(Rate(entry.key, t.rates[entry.key].toString().toDoubleOrNull() ?: 0.0)) }
            .let { return RatesModel(list) }
    }
}