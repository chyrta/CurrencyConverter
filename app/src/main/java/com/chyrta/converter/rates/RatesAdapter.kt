package com.chyrta.converter.rates

import androidx.recyclerview.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.chyrta.converter.R
import com.chyrta.converter.data.model.Rate
import com.chyrta.converter.util.format
import com.chyrta.converter.util.getCurrencyFlagResId
import com.chyrta.converter.util.getCurrencyNameResId
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.*
import kotlin.collections.ArrayList

class RatesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val amountChangedSubject: PublishSubject<Rate> = PublishSubject.create<Rate>()
    private val baseCurrencyChangedSubject: PublishSubject<String> = PublishSubject.create<String>()

    val amountChangedObservable: Observable<Rate>
        get() = amountChangedSubject

    val baseCurrencyChangedObservable: Observable<String>
        get() = baseCurrencyChangedSubject

    var symbolRate = HashMap<String, Rate>()
    var symbolPosition: ArrayList<String> = ArrayList()

    var base: String = "EUR"
        set(value) {
            if (field != value) {
                baseCurrencyChangedSubject.onNext(value)
            }
            field = value
        }

    var amount: Float = 1.00F
        set(value) {
            if (field != value) {
                field = value
            }
        }

    fun notifyAdapter() {
        notifyItemRangeChanged(0, symbolPosition.size - 1, amount)
    }

    inner class RateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var ivCurrencyFlag: ImageView = itemView.findViewById(R.id.iv_currency_flag)
        var tvCurrencySymbol: TextView = itemView.findViewById(R.id.tv_currency_symbol)
        var tvCurrencyName: TextView = itemView.findViewById(R.id.tv_currency_name)
        var etCurrencyAmount: EditText = itemView.findViewById(R.id.et_currency_amount)
        var symbol: String = ""

        private val textWatcher: TextWatcher = object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(charSequence: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (etCurrencyAmount.isFocused) {
                    if (charSequence!!.isNotEmpty()) {
                        baseCurrencyChangedSubject.onNext(symbol.toUpperCase())
                        val amount = Rate(symbol, charSequence.toString().toFloat())
                        amountChangedSubject.onNext(amount)
                    }
                }
            }
        }

        private val focusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            var pickedCurrencyAmount = etCurrencyAmount.text.toString()
            if (pickedCurrencyAmount.isEmpty()) pickedCurrencyAmount = "0"
            baseCurrencyChangedSubject.onNext(symbol)
            amountChangedSubject.onNext(Rate(symbol, pickedCurrencyAmount.toFloat()))

            if (!hasFocus) {
                return@OnFocusChangeListener
            }

            layoutPosition.takeIf { it > 0 }?.also { currentPosition ->
                symbolPosition.removeAt(currentPosition).also { symbolPosition.add(0, it) }
                notifyItemMoved(currentPosition, 0)
            }
        }

        fun bind(rate: Rate) {
            if (symbol != rate.symbol) {
                initView(rate)
                this.symbol = rate.symbol
            }
            if (!etCurrencyAmount.isFocused) {
                etCurrencyAmount.setText(calculateRates(amount, rate.rate))
            }
        }

        fun initView(rate: Rate) {
            val symbol = rate.symbol
            val currencyName = getCurrencyNameResId(itemView.context, symbol)
            val currencyIcon = getCurrencyFlagResId(itemView.context, symbol)

            tvCurrencyName.setText(currencyName)
            tvCurrencySymbol.setText(symbol)
            ivCurrencyFlag.setImageResource(currencyIcon)
            etCurrencyAmount.addTextChangedListener(textWatcher)
            etCurrencyAmount.setOnFocusChangeListener(focusChangeListener)
        }
    }

    private fun getItem(position: Int): Rate {
        return symbolRate[symbolPosition[position]]!!
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): RecyclerView.ViewHolder {
        return RateViewHolder(
                LayoutInflater
                        .from(parent.context)
                        .inflate(R.layout.item_currency_conversion, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return symbolPosition.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RateViewHolder).bind(getItem(position))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: List<Any>) {
        if (payloads.isNotEmpty()) {
            (holder as RateViewHolder).bind(getItem(position))
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    private fun calculateRates(amount: Float, rate: Float): String {
        return (amount * rate).format()
    }
}
