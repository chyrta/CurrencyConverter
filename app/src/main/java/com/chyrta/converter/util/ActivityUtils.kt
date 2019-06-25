package com.chyrta.converter.util

import android.annotation.SuppressLint
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

@SuppressLint("CommitTransaction")
fun addFragmentToActivity(
  fragmentManager: FragmentManager,
  fragment: Fragment,
  frameId: Int
) {
    fragmentManager.beginTransaction().run {
        add(frameId, fragment)
        commit()
    }
}

fun getCurrencyNameResId(context: Context, symbol: String) =
        context.resources.getIdentifier("currency_" + symbol + "_name", "string",
                context.packageName)

fun getCurrencyFlagResId(context: Context, symbol: String) = context.resources.getIdentifier(
        "ic_" + symbol + "_flag", "drawable", context.packageName)
