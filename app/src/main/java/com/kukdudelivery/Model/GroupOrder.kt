package com.kukdudelivery.Model

data class GroupOrder(
    val GroupOrders: ArrayList<GroupOrderX> = arrayListOf(),
    val ResponseCode: Int,
    val ResponseMsg: String,
    val Result: String,
    val ServerTime: String
)