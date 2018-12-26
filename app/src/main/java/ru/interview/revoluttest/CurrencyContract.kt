package ru.interview.revoluttest

import android.widget.EditText
import kotlinx.coroutines.Deferred
import ru.interview.revoluttest.net.RCurs

object CurrencyContract {
    interface View {
        fun showError(s: String)

    }

    interface CurrenciesView {
        fun showRates(rates: List<Rate>)
        fun updateRatesWithMap(currenciesMap: Map<String, Rate>)
        fun moveToTop(position: Int)
        fun recalculateCurrencies()
        fun clearBaseEditText()
        fun isEmpty(): Boolean
    }

    interface Presenter {
        fun resume()
        fun pause()
        fun onBaseInputChanged(s: CharSequence?)
        fun getBaseInputNumber(): Double?
        fun setCurrenciesView(currenciesView: CurrenciesView)
        fun onRateClick(position: Int, rate: Rate, inputText: String)
    }

    interface Repository{
        fun getCurrencies(base:String):Deferred<RCurs>
    }
}