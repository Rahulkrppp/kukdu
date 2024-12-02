package com.kukdudelivery.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val cuttype: String,
    val product_id: String,
    val product_name: String,
    val product_price: String,
    val product_total: String,
    val quantity: String,
    val regular_price: String,
    val sell_price: String,
    val weight: String
) : Parcelable