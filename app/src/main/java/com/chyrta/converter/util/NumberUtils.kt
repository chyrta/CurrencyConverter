package com.chyrta.converter.util

import java.util.*

fun Float.format(): String = String.format(Locale.getDefault(), "%.2f", this)
