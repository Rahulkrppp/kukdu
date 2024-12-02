package com.kukdudelivery.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class GroupOrderX(
    val group_order_id: String,
    val group_order_start_time: String,
    val group_order_status: String,
    val order_delivery_charges: String,
    val order_delivery_charges_high: String,
    val order_delivery_person_id: String,
    val order_distance: String,
    val order_pickup_address: ArrayList<OrderPickupAddres> = arrayListOf(),
    val orders: ArrayList<Order> = arrayListOf()
) : Parcelable