package com.team.bossku.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Ticket::class,
            parentColumns = ["id"],
            childColumns = ["ticketId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("ticketId")]
)

data class TicketDetail(
    @PrimaryKey(true)
    val id: Int?= null,
    val ticketId: Int,  // foreign key that link to ticket
    val itemId: Int,
    val name: String,
    val price: Double,
    val qty: Int
) {
    val subtotal: Double get() = price * qty
}