package com.team.bossku.data.model

data class TicketItem(
    val id: Int?= null,
    val itemId: Int,
    val name: String,
    val price: Double,
    val qty: Int
) {
    val total: Double get() = price * qty
}