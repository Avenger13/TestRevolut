package ru.interview.revoluttest.rates

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import kotlinx.coroutines.*
import ru.interview.revoluttest.business.model.Rate
import ru.interview.revoluttest.core.Scopes
import ru.interview.revoluttest.presentation.model.RateVM
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.fixedRateTimer

class CurrencyPresenter(val interactor: RatesInteractor) : CurrencyContract.Presenter {

    companion object {
        const val TAG = "MYLOGGER"

        @JvmStatic
        fun log(msg: String) = Log.d(TAG, msg)
    }


    private var view: CurrencyContract.View? = null

    private var timer: Timer? = null
    private var baseInputNumber: Double? = 1.0
    private var baseRate = "EUR"
    private var isRequesting = false
    private var isAnimating = false
    private var listVM: List<RateVM> = ArrayList()
    private var list: List<Rate> = ArrayList()
    private val ioScope = Scopes.IO + Job()
    private val mainScope = Scopes.Main + Job()


    //CurrencyContract.Presenter methods begin
    override fun attachView(view: CurrencyContract.View) {
        this.view = view
    }

    override fun resume() {
        timer = fixedRateTimer(period = 1000L, action = {

            if (isRequestAllowed()) {
                isRequesting = true
                ioScope.launch {

                    val tBaseInputNumber = baseInputNumber
                    val tBaseRate = baseRate
//                    log("base input = $baseInputNumber")
                    list = interactor.getRates(tBaseRate).list

                    if (isAnimating || tBaseRate != baseRate || tBaseInputNumber != baseInputNumber) {
                        isRequesting = false
                        return@launch
                    }

                    val ratesVM = createRatesVM(tBaseInputNumber, list)
//                    log("start diff calc")
//                    var start = Date().time
//                    log("end diff calc ${Date().time - start}")

                    withContext(mainScope.coroutineContext) {
                        //                        log("start dispatch")
//                        start = Date().time
//                        log("dispatch update")
                        dispatch(ratesVM)
//                        log("end dispatch ${Date().time - start}")
                        isRequesting = false
                    }

                }
            }
        })

    }

    override fun pause() {
        cancelRequest()
    }

    override fun onAnimationStart() {
        isAnimating = true
    }

    override fun onAnimationEnd() {
        isAnimating = false
    }

    override fun onBaseInputChanged(s: CharSequence?) {
        log("onBaseInputChanged $s")
        baseInputNumber = s.toString().toDoubleOrNull()
        dispatch(createRatesVM(baseInputNumber, list))
    }

    override fun onRateClick(position: Int, rate: RateVM, inputText: String) {
        if (isAnimating) return

        baseRate = rate.name
        baseInputNumber = inputText.toDoubleOrNull()

        val tList = ArrayList(listVM)
        tList.removeAt(position)
        tList.add(0, rate)
//        log("dispatch on rate")
        dispatch(tList)
    }

    private fun dispatch(new: List<RateVM>) {
        DiffUtil.calculateDiff(RatesCallback(listVM, new)).also { listVM = new; view?.dispatchUpdates(it) }
    }

    override fun getItemCount() = listVM.size


    override fun getItem(p: Int): RateVM {
        return listVM[p]
    }

    //private methods begin


    //CurrencyContract.Presenter methods end
    private fun createRatesVM(inputNumber: Double?, list: List<Rate>): List<RateVM> {
        return list.map {
            RateVM.fromRate(it, inputNumber)
        }
    }


    private fun cancelRequest() {
        timer?.cancel()
        timer = null
        ioScope.coroutineContext.cancelChildren()
    }

    private fun isRequestAllowed() = !isRequesting && (listVM.isEmpty() || baseInputNumber != null)

    //private methods end

}
