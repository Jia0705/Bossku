package com.team.bossku.data.repo

import com.team.bossku.data.db.TicketsDao
import com.team.bossku.data.model.Ticket
import com.team.bossku.data.model.TicketStatus
import kotlinx.coroutines.flow.Flow

class TicketsRepo(
    private val dao: TicketsDao
) {
    suspend fun addTicket(ticket: Ticket) {
        dao.addTicket(ticket)
    }

    suspend fun getTicketById(id: Int): Ticket? {
        return dao.getTicketById(id)
    }

    fun getTickets(): Flow<List<Ticket>> {
        return dao.getAllTickets()
    }

    suspend fun updateTicket(ticket: Ticket) {
        dao.updateTicket(ticket)
    }

    suspend fun markAsPaid(id: Int) {
        val ticket = dao.getTicketById(id) ?: return
        if (ticket.status == TicketStatus.SAVED) {
            val updatedTicket = ticket.copy(
                status = TicketStatus.PAID,
                paidAt = System.currentTimeMillis()
            )
            dao.updateTicket(updatedTicket)
        }
    }

    suspend fun deleteTicket(id: Int) {
        dao.deleteTicket(id)
    }
}