package com.team.bossku.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.team.bossku.MyApp
import com.team.bossku.data.model.Ticket
import com.team.bossku.data.model.TicketStatus
import com.team.bossku.data.repo.TicketsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repo: TicketsRepo
) : ViewModel() {
    private val _allPaid = MutableStateFlow<List<Ticket>>(emptyList())

    // Search + Sort
    private val _search = MutableStateFlow("")
    private val _sortAscending = MutableStateFlow(false)   // false = desc by default
    private val _sortByName = MutableStateFlow(true)       // true = name, false = date
    private val _history = MutableStateFlow<List<Ticket>>(emptyList())
    val history: StateFlow<List<Ticket>> = _history

    init {
        viewModelScope.launch {
            repo.getTickets().collect { tickets ->
                _allPaid.value = tickets
                    .filter { it.status == TicketStatus.PAID }
                    .sortedByDescending { it.paidAt ?: it.createdAt }
                applyFilters()
            }
        }
    }

    fun setSearch(text: String) {
        _search.value = text
        applyFilters()
    }

    fun setSortAscending(asc: Boolean) {
        _sortAscending.value = asc
        applyFilters()
    }

    fun setSortByName(byName: Boolean) {
        _sortByName.value = byName
        applyFilters()
    }

    private fun applyFilters() {
        val searchText = _search.value.trim().lowercase()
        val sortAscending = _sortAscending.value
        val sortByName = _sortByName.value

        var filteredList = _allPaid.value

        if (searchText.isNotEmpty()) {
            filteredList = filteredList.filter { ticket ->
                ticket.name.lowercase().contains(searchText)
            }
        }

        filteredList = if (sortByName) {
            if (sortAscending) {
                filteredList.sortedBy { it.name.lowercase() }
            } else {
                filteredList.sortedByDescending { it.name.lowercase() }
            }
        } else {
            if (sortAscending) {
                filteredList.sortedBy { it.paidAt ?: it.createdAt }
            } else {
                filteredList.sortedByDescending { it.paidAt ?: it.createdAt }
            }
        }
        _history.value = filteredList
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as MyApp).ticketsRepo
                HistoryViewModel(repo = myRepository)
            }
        }
    }
}