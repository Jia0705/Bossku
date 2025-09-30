package com.team.bossku.data.model

data class Ticket(
    val id: Int?= null,
    val createdAt: Long,
    val status: TicketStatus = TicketStatus.OPEN,
    val closedAt: Long? = null,
    val items: List<TicketDetail> = emptyList()
) {
    val total: Double get() = items.sumOf { it.total }
}

enum class TicketStatus { OPEN, CLOSED }