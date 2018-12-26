package ru.interview.revoluttest

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.item_rate.view.*

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

val df = DecimalFormat("0.###", DecimalFormatSymbols.getInstance().also { it.decimalSeparator = '.' })

class CursAdapter(val presenter: CurrencyContract.Presenter) : RecyclerView.Adapter<CursAdapter.CursVH>(),
    CurrencyContract.CurrenciesView {

    private val list: ArrayList<Rate> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private val baseInputChangeListener: BaseInputChangeListener = BaseInputChangeListener(presenter)


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(p: ViewGroup, ivt: Int) =
        CursVH(LayoutInflater.from(p.context).inflate(R.layout.item_rate, p, false), presenter, baseInputChangeListener)

    override fun getItemCount() = list.size


    override fun onBindViewHolder(vh: CursVH, p: Int) = vh.bind(list[p], presenter.getBaseInputNumber()).also {
        baseInputChangeListener.onBindViewHolder(vh, p)
    }

    override fun onViewRecycled(holder: CursVH) {
        super.onViewRecycled(holder)
        baseInputChangeListener.onViewRecycled(holder.adapterPosition)
    }

    //CurrencyContract.CurrenciesView methods begin
    override fun isEmpty() = list.isEmpty()

    override fun showRates(rates: List<Rate>): Unit {
        list.addAll(rates)
        notifyItemRangeInserted(0, rates.size)
    }

    override fun updateRatesWithMap(currenciesMap: Map<String, Rate>) {
        list.forEach { r -> r.rate = currenciesMap[r.name]?.rate ?: 1.0 }
    }

    override fun clearBaseEditText() {
        recyclerView.findViewHolderForAdapterPosition(0).also { vh ->
            if (vh is CursVH) {
                vh.clearInput()
            }
        }
    }

    override fun recalculateCurrencies() =
        list.drop(1)
            .forEachIndexed { i, r ->
                recyclerView.findViewHolderForAdapterPosition(i + 1)
                    .also { vh ->
                        if (vh is CursVH)
                            vh.bind(r, presenter.getBaseInputNumber())
                    }
            }

    override fun moveToTop(position: Int) {
        val rate = list[position]
        list.removeAt(position)
        list.add(0, rate)
        notifyItemMoved(position, 0)
    }
    //CurrencyContract.CurrenciesView methods end


    class CursVH(
        view: View,
        private val presenter: CurrencyContract.Presenter,
        private val baseInputChangeListener: BaseInputChangeListener
    ) : RecyclerView.ViewHolder(view) {

        fun bind(e: Rate, cur: Double?) {
            itemView.tName.text = e.name
            val calculated = e.rate * (cur ?: 0.0)
            itemView.eRate.setText(if (calculated == 0.0) "" else df.format(calculated))

            itemView.eRate.setOnFocusChangeListener { _, b ->
                presenter.onRateClick(adapterPosition, e, itemView.eRate.text.toString())
                baseInputChangeListener.setBaseEditText(itemView.eRate)
            }


            itemView.setOnClickListener {
                it.eRate.requestFocus()
            }
        }

        fun clearInput() = itemView.eRate.setText(null)

        fun getEditText(): EditText? = itemView.eRate

    }
}