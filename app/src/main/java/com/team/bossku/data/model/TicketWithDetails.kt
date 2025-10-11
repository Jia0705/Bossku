package com.team.bossku.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class TicketWithDetails(
    @Embedded val ticket: Ticket,
    @Relation(
        parentColumn = "id",
        entityColumn = "ticketId"
    )
    val items: List<TicketDetail>
) {
    val total: Double get() = items.sumOf { it.subtotal }
}