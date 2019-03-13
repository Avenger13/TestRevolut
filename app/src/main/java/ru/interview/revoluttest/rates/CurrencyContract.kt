package ru.interview.revoluttest.rates


import androidx.recyclerview.widget.DiffUtil
import ru.interview.revoluttest.presentation.model.RateVM

object CurrencyContract {

    interface View {
        fun dispatchUpdates(diffResult: DiffUtil.DiffResult)
        fun showError(s: String)
        fun isComLayout(): Boolean
    }

    interface Presenter {
        fun resume()
        fun pause()
        fun onBaseInputChanged(s: CharSequence?)
        fun onRateClick(position: Int, rate: RateVM, inputText: String)
        fun attachView(view: View)
        fun getItem(p: Int): RateVM
        fun getItemCount(): Int
        fun onAnimationEnd()
        fun onAnimationStart()
    }

}