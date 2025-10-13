package com.team.bossku.ui.ticket_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.team.bossku.MyApp
import com.team.bossku.data.model.TicketStatus
import com.team.bossku.data.model.TicketWithDetails
import com.team.bossku.data.repo.TicketsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TicketDetailViewModel(
    private val ticketsRepo: TicketsRepo
) : ViewModel() {
    private val _ticket = MutableStateFlow<TicketWithDetails?>(null)
    val ticket: StateFlow<TicketWithDetails?> = _ticket

    fun loadTicket(ticketId: Int) {
        viewModelScope.launch {
            ticketsRepo.getTicketWithDetailsById(ticketId).collectLatest { ticket ->
                _ticket.value = ticket
            }
        }
    }

    fun updateQuantity(position: Int, qty: Int) {
        val current = _ticket.value ?: return
        val ticket = current.ticket
        if (ticket.status != TicketStatus.SAVED) return
        if (qty <= 0) return

        val items = current.items.toMutableList()
        if (position < 0 || position >= items.size) return

        val line = items[position].copy(qty = qty)
        items[position] = line

        viewModelScope.launch {
            ticketsRepo.updateTicketDetail(line)

            val newTotal = items.sumOf { it.price * it.qty }
            ticketsRepo.updateTicket(ticket.copy(total = newTotal))
        }

        _ticket.value = current.copy(items = items)
    }

    fun removeItem(position: Int) {
        val current = _ticket.value ?: return
        val ticket = current.ticket
        if (ticket.status != TicketStatus.SAVED) return

        val items = current.items.toMutableList()
        if (position < 0 || position >= items.size) return

        val removedItem = items.removeAt(position)

        viewModelScope.launch {
            removedItem.id?.let {
                ticketsRepo.deleteTicketDetail(it)
            }

            val newTotal = items.sumOf { it.price * it.qty }
            ticketsRepo.updateTicket(ticket.copy(total = newTotal))
        }

        _ticket.value = current.copy(items = items)
    }

    fun markAsPaid() {
        val current = _ticket.value ?: return
        val id = current.ticket.id ?: return
        if (current.ticket.status != TicketStatus.SAVED) return

        viewModelScope.launch {
            ticketsRepo.markAsPaid(id)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as MyApp).ticketsRepo
                TicketDetailViewModel(ticketsRepo = myRepository)
            }
        }
    }
}