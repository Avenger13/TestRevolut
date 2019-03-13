package ru.interview.revoluttest.rates


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_cur.*
import ru.interview.revoluttest.R


class CurrencyActivity : AppCompatActivity() {
    private lateinit var presenter: CurrencyPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cur)

        presenter = CurrencyPresenter(RatesInteractor(RatesRepository()))
        CurrencyAdapter(presenter).apply {
            rvCurs.adapter = this
            presenter.attachView(this)
            notifyDataSetChanged()
        }
    }

    override fun onPause() {
        presenter.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }
}