package ru.interview.revoluttest.rates

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class BaseInputChangeListener(private val presenter: CurrencyContract.Presenter) : TextWatcher {
    private var baseEditText: EditText? = null

    fun setBaseEditText(e: EditText?): Unit {
        baseEditText?.removeTextChangedListener(this)
        e?.addTextChangedListener(this)
        baseEditText = e
//        baseEditText?.setSelection(e?.text.toString().length)
    }

    fun onViewRecycled(p: Int) {
        if (p == 0) setBaseEditText(null)
    }

    fun onBindViewHolder(vh: CurrencyAdapter.CursVH, p: Int) {
        if (p == 0) setBaseEditText(vh.getEditText())
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        presenter.onBaseInputChanged(s)
    }
}