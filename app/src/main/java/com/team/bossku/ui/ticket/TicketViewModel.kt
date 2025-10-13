package com.team.bossku.ui.ticket

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.viewModelScope
import com.team.bossku.MyApp
import com.team.bossku.data.model.Ticket
import com.team.bossku.data.model.TicketStatus
import com.team.bossku.data.repo.TicketsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class TicketViewModel(
    private val ticketsRepo: TicketsRepo
) : ViewModel() {
    private val _tickets = MutableStateFlow<List<Ticket>>(emptyList())
    val tickets: StateFlow<List<Ticket>> = _tickets

    init {
        viewModelScope.launch {
            ticketsRepo.getTickets().collectLatest { list ->
                _tickets.value = list
                    .filter { it.status == TicketStatus.SAVED }
                    .sortedByDescending { it.createdAt }
            }
        }
    }

    fun deleteTicket(id: Int) {
        viewModelScope.launch {
            ticketsRepo.deleteTicket(id)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as MyApp).ticketsRepo
                TicketViewModel(ticketsRepo = myRepository)
            }
        }
    }
}