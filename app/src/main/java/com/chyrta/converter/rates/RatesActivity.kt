package com.chyrta.converter.rates

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chyrta.converter.R
import com.chyrta.converter.util.addFragmentToActivity

class RatesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rates)

        if (supportFragmentManager.findFragmentById(R.id.contentFrame) == null) {
            addFragmentToActivity(supportFragmentManager, RatesFragment.invoke(), R.id.contentFrame)
        }
    }
}
