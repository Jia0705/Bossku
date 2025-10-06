package com.team.bossku.data.model

data class Ticket(
    val id: Int?= null,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val paidAt: Long?= null,
    val status: TicketStatus = TicketStatus.SAVED,
    val items: List<TicketDetail> = emptyList()
) {
    val total: Double get() = items.sumOf { it.subtotal }
}

enum class TicketStatus { SAVED, PAID }