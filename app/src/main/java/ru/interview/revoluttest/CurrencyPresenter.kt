package ru.interview.revoluttest

import android.os.Handler
import android.util.Log
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import ru.interview.revoluttest.net.Api
import ru.interview.revoluttest.net.RCurs
import java.io.IOException

class CurrencyPresenter(
    val view: CurrencyContract.View?
) : CurrencyContract.Presenter {

    companion object {
        const val TAG = "MYLOGGER"

        @JvmStatic
        fun log(msg: String) = Log.d(TAG, msg)
    }

    private val repository: CurrencyContract.Repository = CurrencyRepository()
    private val handler = Handler()
    private val requestRunnable: Runnable = RequestRunnable()
    private val mainScope = CoroutineScope(Dispatchers.Main)

    private var baseInputNumber: Double? = null
    private var currenciesView: CurrencyContract.CurrenciesView? = null
    private var baseRate = "EUR"


    //CurrencyContract.Presenter methods begin
    override fun resume() {
        handler.post(requestRunnable)
    }

    override fun pause() {
        cancelRequest()
    }

    override fun onBaseInputChanged(s: CharSequence?) {
        baseInputNumber = s.toString().toDoubleOrNull()
        if (s?.length != 0 && baseInputNumber == null) {
            currenciesView?.clearBaseEditText()
            return
        }

        currenciesView?.recalculateCurrencies()
    }


    override fun onRateClick(position: Int, rate: Rate, inputText: String) {
        if (position != 0) {
            rate.rate = 1.0
            baseRate = rate.name
            currenciesView?.moveToTop(position)
            baseInputNumber = inputText.toDoubleOrNull()
        }
    }

    override fun getBaseInputNumber() = baseInputNumber

    override fun setCurrenciesView(currenciesView: CurrencyContract.CurrenciesView) {
        this.currenciesView = currenciesView
    }
    //CurrencyContract.Presenter methods end


    //private methods begin


    private fun cancelRequest(): Unit {
        handler.removeCallbacks(requestRunnable)
        mainScope.coroutineContext.cancelChildren()
    }

    //private methods end

    inner class RequestRunnable : Runnable {
        override fun run() {
            mainScope.launch {
                if (isRequestAllowed()) {
                    try {
                        val rCurs: RCurs? = repository.getCurrencies(baseRate).await()
                        val currenciesMap = parseCurrenciesMap(rCurs?.rates ?: JsonObject())

                        if (currenciesView?.isEmpty() == true) {
                            val rates = mutableListOf(Rate(baseRate, 1.0)).also { it.addAll(currenciesMap.values) }
                            currenciesView?.showRates(rates)
                        } else {
                            currenciesView?.updateRatesWithMap(currenciesMap)
                            currenciesView?.recalculateCurrencies()
                        }
                    } catch (e: IOException) {
                        view?.showError("net error ${e.message}")
                    }
                }

                handler.postDelayed(this@RequestRunnable, 1000L)
            }
        }

        private fun isRequestAllowed() = currenciesView?.isEmpty() == true || baseInputNumber != null

        private fun parseCurrenciesMap(json: JsonObject): Map<String, Rate> {
            return json.entrySet()
                .map { e -> Rate(e.key, e.value.asDouble) }
                .associateBy { r -> r.name }
        }
    }


}