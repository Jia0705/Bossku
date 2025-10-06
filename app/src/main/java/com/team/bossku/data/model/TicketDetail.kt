package com.team.bossku.data.model

data class TicketDetail(
    val id: Int?= null,
    val itemId: Int,
    val name: String,
    val price: Double,
    val qty: Int
) {
    val subtotal: Double get() = price * qty
}