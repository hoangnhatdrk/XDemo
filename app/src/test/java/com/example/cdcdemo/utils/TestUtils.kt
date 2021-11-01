package com.example.cdcdemo

import com.example.cdcdemo.data.CurrencyInfo

fun sampleCurrencyInfo(id: Int, name: String? = null, symbol: String? = null) =
    CurrencyInfo(
        "SAMPLE$id",
        name ?: "Sample currency $id",
        symbol ?: "SAMPLE$id")