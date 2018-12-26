package ru.interview.revoluttest


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_cur.*


class CurrencyActivity : AppCompatActivity(), CurrencyContract.View {
    private lateinit var presenter: CurrencyPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cur)

        presenter = CurrencyPresenter(this)
        rvCurs.adapter = CursAdapter(presenter).also { presenter.setCurrenciesView(it) }
    }

    override fun onPause() {
        presenter.pause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }

    override fun showError(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

}