package com.team.bossku.data.repo

import com.team.bossku.data.db.TicketDetailsDao
import com.team.bossku.data.db.TicketsDao
import com.team.bossku.data.model.Ticket
import com.team.bossku.data.model.TicketDetail
import com.team.bossku.data.model.TicketStatus
import com.team.bossku.data.model.TicketWithDetails
import kotlinx.coroutines.flow.Flow

class TicketsRepo(
    private val dao: TicketsDao,
    private val ticketDetailsDao: TicketDetailsDao
) {
    suspend fun addTicket(ticket: Ticket): Int {
        return dao.addTicket(ticket).toInt()
    }

    fun getTickets(): Flow<List<Ticket>> {
        return dao.getAllTickets()
    }

    suspend fun updateTicket(ticket: Ticket) {
        dao.updateTicket(ticket)
    }

    suspend fun deleteTicket(id: Int) {
        dao.deleteTicket(id)
    }

    fun getTicketsWithDetails(): Flow<List<TicketWithDetails>> {
        return dao.getAllTicketsWithDetails()
    }

    fun getTicketWithDetailsById(id: Int): Flow<TicketWithDetails?> {
        return dao.getTicketWithDetailsById(id)
    }

    suspend fun updateTicketDetail(detail: TicketDetail) {
        ticketDetailsDao.updateTicketDetail(detail)
    }

    suspend fun deleteTicketDetail(id: Int) {
        ticketDetailsDao.deleteTicketDetail(id)
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
}