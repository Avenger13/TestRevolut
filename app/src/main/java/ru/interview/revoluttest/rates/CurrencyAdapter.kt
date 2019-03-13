package ru.interview.revoluttest.rates

import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_rate.view.*
import ru.interview.revoluttest.R
import ru.interview.revoluttest.presentation.model.RateVM


class CurrencyAdapter(val presenter: CurrencyContract.Presenter) : RecyclerView.Adapter<CurrencyAdapter.CursVH>(),
    CurrencyContract.View {

    companion object {
        const val PL_NAME = "PL_NAME"
        const val PL_RATE = "PL_RATE"
    }

    private lateinit var recyclerView: RecyclerView
    private val baseInputChangeListener: BaseInputChangeListener = BaseInputChangeListener(presenter)


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
        recyclerView.layoutAnimationListener = object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                presenter.onAnimationEnd()
            }

            override fun onAnimationStart(animation: Animation?) {
                presenter.onAnimationStart()
            }

        }
    }

    override fun onCreateViewHolder(p: ViewGroup, ivt: Int) =
        CursVH(
            LayoutInflater.from(p.context).inflate(R.layout.item_rate, p, false),
            presenter,
            baseInputChangeListener
            , recyclerView
        )

    override fun getItemCount() = presenter.getItemCount()


    override fun onBindViewHolder(holder: CursVH, position: Int) = onBindViewHolder(holder, position, mutableListOf())

    override fun onBindViewHolder(holder: CursVH, position: Int, payloads: MutableList<Any>) {
        val bundle = payloads.getOrNull(0) as Bundle?
        holder.bind(presenter.getItem(position), bundle).also {
            baseInputChangeListener.onBindViewHolder(holder, position)
        }
    }

    override fun onViewRecycled(holder: CursVH) {
        super.onViewRecycled(holder)
        baseInputChangeListener.onViewRecycled(holder.adapterPosition)
    }

    override fun dispatchUpdates(diffResult: DiffUtil.DiffResult) {
        recyclerView.post {
            diffResult.dispatchUpdatesTo(this)
        }
    }

    override fun isComLayout() = recyclerView.isComputingLayout

    override fun showError(s: String) {
        Toast.makeText(recyclerView.context, s, Toast.LENGTH_SHORT).show()
    }

    //CurrencyContract.View methods end


    class CursVH(
        view: View,
        private val presenter: CurrencyContract.Presenter,
        private val baseInputChangeListener: BaseInputChangeListener
        , private val recyclerView: RecyclerView
    ) : RecyclerView.ViewHolder(view) {

        fun bind(e: RateVM, payLoad: Bundle?) = with(itemView) {
            if (payLoad != null) {
                CurrencyPresenter.log("payload update")
                if (payLoad.containsKey(PL_NAME)) tName.text = e.name
                if (payLoad.containsKey(PL_RATE)) eRate.setText(payLoad.getString(PL_RATE))
            } else {
                CurrencyPresenter.log("hard update")
                tName.text = e.name
                eRate.setText(e.rate)
            }

            eRate.setOnFocusChangeListener { v, focus ->
                if (adapterPosition != RecyclerView.NO_POSITION
                    && adapterPosition != 0
//                    && !recyclerView.isComputingLayout
//                    && recyclerView.scrollState == RecyclerView.SCROLL_STATE_IDLE
                ) {//&& !recyclerView.isAnimating) {//NO NEED TO HANDLE CLICK ON TOP ITEM
                    presenter.onRateClick(adapterPosition, e, eRate.text.toString())
                    baseInputChangeListener.setBaseEditText(eRate)
                }
            }
            setOnClickListener { eRate.requestFocus() }
        }


        fun getEditText(): EditText? = itemView.eRate

    }
}