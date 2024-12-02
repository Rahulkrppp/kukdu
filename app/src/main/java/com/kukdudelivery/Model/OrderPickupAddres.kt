package com.kukdudelivery.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OrderPickupAddres(
    val vendor_address: String,
    val vendor_mobile: String,
    val vendor_name: String
) : Parcelable