package com.team.bossku.ui.ticket

import androidx.lifecycle.ViewModel
import com.team.bossku.data.model.Ticket
import com.team.bossku.data.model.TicketStatus
import com.team.bossku.data.repo.TicketsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class TicketViewModel(
    private val ticketsRepo: TicketsRepo = TicketsRepo.getInstance(),
) : ViewModel() {
    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    val tickets: StateFlow<List<Ticket>> = _tickets

    fun loadTickets() {
        val list = ticketsRepo.getTickets()
            .filter { it.status == TicketStatus.SAVED }
            .sortedByDescending { it.createdAt }
        _tickets.value = list
    }

    fun deleteTicket(id: Int) {
        ticketsRepo.deleteTicket(id)
        loadTickets()
    }
}