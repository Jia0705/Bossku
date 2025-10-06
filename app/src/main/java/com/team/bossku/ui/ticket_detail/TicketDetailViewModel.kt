package com.team.bossku.ui.ticket_detail

import androidx.lifecycle.ViewModel
import com.team.bossku.data.model.Ticket
import com.team.bossku.data.model.TicketStatus
import com.team.bossku.data.repo.TicketsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TicketDetailViewModel(
    private val ticketsRepo: TicketsRepo = TicketsRepo.getInstance()
) : ViewModel() {
    private val _ticket = MutableStateFlow<Ticket?>(null)
    val ticket: StateFlow<Ticket?> = _ticket

    fun loadTicket(ticketId: Int) {
        _ticket.value = ticketsRepo.getTicketById(ticketId)
    }

    fun updateQuantity(position: Int, qty: Int) {
        val current = _ticket.value ?: return
        if (current.status != TicketStatus.SAVED) return
        if (qty <= 0) return

        val items = current.items.toMutableList()
        if (position < 0 || position >= items.size) return

        val line = items[position]
        items[position] = line.copy(qty = qty)

        val id = current.id ?: return
        ticketsRepo.updateTicket(id, current.copy(items = items))
        _ticket.value = ticketsRepo.getTicketById(id)
    }

    fun removeItem(position: Int) {
        val current = _ticket.value ?: return
        if (current.status != TicketStatus.SAVED) return

        val items = current.items.toMutableList()
        if (position < 0 || position >= items.size) return

        items.removeAt(position)

        val id = current.id ?: return
        ticketsRepo.updateTicket(id, current.copy(items = items))
        _ticket.value = ticketsRepo.getTicketById(id)
    }

    fun markAsPaid() {
        val current = _ticket.value ?: return
        if (current.status != TicketStatus.SAVED) return

        val id = current.id ?: return
        ticketsRepo.markAsPaid(id)
        _ticket.value = ticketsRepo.getTicketById(id)
    }
}