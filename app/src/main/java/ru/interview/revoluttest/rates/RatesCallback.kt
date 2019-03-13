package ru.interview.revoluttest.rates

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import ru.interview.revoluttest.presentation.model.RateVM

class RatesCallback(val old: List<RateVM>, val new: List<RateVM>) : DiffUtil.Callback() {
    override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
        return old[p0].name == new[p1].name
    }

    override fun getOldListSize(): Int = old.size

    override fun getNewListSize(): Int = new.size

    override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
        return old[p0].rate == new[p1].rate
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return Bundle().apply {
            if (old[oldItemPosition].name != new[newItemPosition].name)
                putBoolean(CurrencyAdapter.PL_NAME, true)
            if (old[oldItemPosition].rate != new[newItemPosition].rate)
                putString(CurrencyAdapter.PL_RATE, new[newItemPosition].rate)
        }
    }

}