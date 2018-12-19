package ru.interview.revoluttest

import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import kotlinx.android.synthetic.main.item_rate.view.*

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*

val df = DecimalFormat("0.###", DecimalFormatSymbols.getInstance().also { it.decimalSeparator = '.' })

class CursAdapter(val onBaseChanged: () -> Unit) : RecyclerView.Adapter<CursAdapter.CursVH>() {
    private val list: ArrayList<Rate> = ArrayList()
    private lateinit var recyclerView: RecyclerView
    private val currencyChangeListener: CurrencyChangeListener = CurrencyChangeListener()

    var curInput: Double? = null
    var baseRate = Rate("EUR", 1.0)


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateViewHolder(p: ViewGroup, ivt: Int) =
        CursVH(LayoutInflater.from(p.context).inflate(R.layout.item_rate, p, false))


    override fun getItemCount() = list.size


    override fun onBindViewHolder(vh: CursVH, p: Int) {
        val rate = list[p]

        vh.itemView.eRate.apply {
            setOnFocusChangeListener { _, b ->
                if (!b) return@setOnFocusChangeListener

                if (vh.adapterPosition != 0) {
                    moveToTop(vh.adapterPosition)

                    rate.rate = 1.0
                    baseRate = rate
                    curInput = text.toString().toDoubleOrNull()

                    onBaseChanged()
                    currencyChangeListener.setEditText(this)
                }
            }

            if (p == 0)
                currencyChangeListener.setEditText(this)

        }

        vh.bind(rate, curInput)

    }

    override fun onViewRecycled(holder: CursVH) {
        super.onViewRecycled(holder)
        if (holder.adapterPosition == 0) {
            currencyChangeListener.setEditText(null)
        }
    }


    inner class CurrencyChangeListener : TextWatcher {
        private var editText: EditText? = null

        fun setEditText(e: EditText?): Unit {
            editText?.removeTextChangedListener(this)
            e?.addTextChangedListener(this)
            editText = e
        }

        override fun afterTextChanged(s: Editable?) {}

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            curInput = s.toString().toDoubleOrNull()
            if (s?.length != 0 && curInput == null) {
                editText?.text = null
                return
            }

            recalculateCurrencies()
        }
    }


    fun updateRatesWithMap(data: Map<String, Rate>) {
        val values = data.values

        if (list.isEmpty()) {
            list.add(baseRate)
            list.addAll(values)
            notifyItemRangeInserted(0, values.size)
            return
        }

        list.forEach { r -> r.rate = data[r.name]?.rate ?: 1.0 }
        recalculateCurrencies()
    }


    private fun recalculateCurrencies() =
        list.drop(1)
            .forEachIndexed { i, r ->
                recyclerView.findViewHolderForAdapterPosition(i + 1)
                    .also { vh ->
                        if (vh is CursVH)
                            vh.bind(r, curInput)
                    }
            }


    private fun moveToTop(pos: Int) {
        val rate = list[pos]
        list.removeAt(pos)
        list.add(0, rate)
        notifyItemMoved(pos, 0)
    }

    class CursVH(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(e: Rate, cur: Double?) {
            itemView.tName.text = e.name
            val calculated = e.rate * (cur ?: 0.0)
            itemView.eRate.setText(if (calculated == 0.0) "" else df.format(calculated))
        }
    }
}