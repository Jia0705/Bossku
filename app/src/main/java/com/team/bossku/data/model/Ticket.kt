package com.team.bossku.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Ticket(
    @PrimaryKey(true)
    val id: Int?= null,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val paidAt: Long?= null,
    val status: TicketStatus = TicketStatus.SAVED,
    val total: Double
)

enum class TicketStatus { SAVED, PAID }