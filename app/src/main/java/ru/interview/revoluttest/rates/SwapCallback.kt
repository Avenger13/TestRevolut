package ru.interview.revoluttest.rates

import androidx.recyclerview.widget.DiffUtil


class SwapCallback(val from: Int, val to: Int) : DiffUtil.Callback() {
    override fun areItemsTheSame(p0: Int, p1: Int): Boolean {
//        CurrencyPresenter.log("areItemsTheSame $p0, $p1")
//        return (p0 == from && p1 == to) || (p1 == from && p0 == to)
        return !(p0 == from || p0 == to || p1 == from || p1 == to)
    }

    override fun getOldListSize(): Int = from + 1

    override fun getNewListSize(): Int = oldListSize

    override fun areContentsTheSame(p0: Int, p1: Int): Boolean {
//        CurrencyPresenter.log("areContentsTheSame $p0, $p1")
        return (p0 != from && p1 != to) && (p1 != from && p0 != to)

//        return !(p0 == from || p0 == to || p1 == from || p1 == to)
    }

}