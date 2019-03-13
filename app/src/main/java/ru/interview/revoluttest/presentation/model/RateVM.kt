package ru.interview.revoluttest.presentation.model

import ru.interview.revoluttest.business.model.Rate
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

data class RateVM(val name: String, val rate: String) {
    companion object {
        @JvmStatic
        private val df = DecimalFormat("0.###", DecimalFormatSymbols.getInstance().also { it.decimalSeparator = '.' })

        @JvmStatic
        fun fromRate(r: Rate, baseInput: Double?): RateVM {
            return RateVM(r.name, formatRate(baseInput, r.rate))
        }

        @JvmStatic
        private fun formatRate(baseInput: Double?, rate: Double): String {
            (rate * (baseInput ?: 0.0))
                .let { res -> return if (res == 0.0) "" else df.format(res) }
        }
    }
}