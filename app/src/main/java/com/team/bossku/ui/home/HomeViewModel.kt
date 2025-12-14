package com.team.bossku.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.team.bossku.MyApp
import com.team.bossku.data.ds.Grid
import com.team.bossku.data.model.Category
import com.team.bossku.data.model.Item
import com.team.bossku.data.model.Ticket
import com.team.bossku.data.model.TicketDetail
import com.team.bossku.data.model.TicketStatus
import com.team.bossku.data.repo.TicketDetailsRepo
import com.team.bossku.data.repo.CategoriesRepo
import com.team.bossku.data.repo.ItemsRepo
import com.team.bossku.data.repo.TicketsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

enum class Mode { ITEMS, CATEGORIES }

class HomeViewModel(
    private val itemsRepo: ItemsRepo,
    private val categoriesRepo: CategoriesRepo,
    private val ticketDetailsRepo: TicketDetailsRepo,
    private val ticketsRepo: TicketsRepo,
    private val grid: Grid
) : ViewModel() {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    private val _categories = MutableStateFlow<List<Category>>(emptyList())

    private val _mode = MutableStateFlow(Mode.ITEMS)
    val mode: StateFlow<Mode> = _mode

    private val _search = MutableStateFlow("")
    private val _sort = MutableStateFlow(true)
    val sortAscending: StateFlow<Boolean> = _sort

    private val _rvItems = MutableStateFlow<List<Item>>(emptyList())
    val rvItems: StateFlow<List<Item>> = _rvItems

    private val _rvCats = MutableStateFlow<List<Category>>(emptyList())
    val rvCategories: StateFlow<List<Category>> = _rvCats

    private val _isEmpty = MutableStateFlow(true)
    val isEmpty: StateFlow<Boolean> = _isEmpty

    private val _addToTicketItem = MutableStateFlow<Int?>(null)
    val addToTicketItem: StateFlow<Int?> = _addToTicketItem

    private val _cartItemCount = MutableStateFlow(0)
    val cartItemCount: StateFlow<Int> = _cartItemCount

    private val _selectedCategoryId = MutableStateFlow<Int?>(null)

    private val _manualOrder = MutableStateFlow<List<Int>>(emptyList())
    private val _manualCatOrder = MutableStateFlow<List<Int>>(emptyList())

    private var fakeTicketId: Int? = null

    init {
        // Restore fake ticket if it exists 
        viewModelScope.launch {
            val existingFakeTicket = ticketsRepo.getFakeTicket()
            fakeTicketId = existingFakeTicket?.id
            // Monitor cart items
            fakeTicketId?.let { ticketId ->
                ticketDetailsRepo.getItems(ticketId).collect { items ->
                    _cartItemCount.value = items.sumOf { it.qty }
                }
            }
        }

        viewModelScope.launch {
            grid.manualOrder.collect { orderString ->
                _manualOrder.value = if (orderString.isBlank()) emptyList()
                else orderString.split(",").mapNotNull { it.toIntOrNull() }
                applyFilters()
            }
        }

        viewModelScope.launch {
            grid.manualCategoryOrder.collect { orderString ->
                _manualCatOrder.value = if (orderString.isBlank()) emptyList()
                else orderString.split(",").mapNotNull { it.toIntOrNull() }
                applyFilters()
            }
        }

        viewModelScope.launch {
            grid.sortAscending.collect { asc ->
                _sort.value = asc
                applyFilters()
            }
        }

        viewModelScope.launch {
            itemsRepo.getItems().collectLatest {
                _items.value = it
                applyFilters()
            }
        }

        viewModelScope.launch {
            categoriesRepo.getCategories().collectLatest {
                _categories.value = it
                applyFilters()
            }
        }
    }

    fun refresh() = applyFilters()

    fun switchMode(newMode: Mode) {
        if (_mode.value != newMode) {
            _mode.value = newMode
            if (newMode == Mode.ITEMS) {
                _selectedCategoryId.value = null
            }
            applyFilters()
        }
    }

    fun clearSelectedCategory() {
        _selectedCategoryId.value = null
        applyFilters()
    }

    fun setSearch(text: String) {
        _search.value = text
        applyFilters()
    }

    fun setSort() {
        viewModelScope.launch {
            val newSort = !_sort.value
            _sort.value = newSort
            grid.saveSortAscending(newSort)

            if (_mode.value == Mode.ITEMS) {
                _manualOrder.value = emptyList()
                grid.saveManualOrder(emptyList())
            } else {
                _manualCatOrder.value = emptyList()
                grid.saveManualCategoryOrder(emptyList())
            }
            applyFilters()
        }
    }

    fun move(from: Int, to: Int) {
        if (_mode.value == Mode.ITEMS) {
            val list = _rvItems.value.toMutableList()
            val item = list.removeAt(from)
            list.add(to, item)
            _rvItems.value = list

            viewModelScope.launch {
                _manualOrder.value = list.mapNotNull { it.id }
                grid.saveManualOrder(_manualOrder.value)
            }
        } else {
            val list = _rvCats.value.toMutableList()
            val cat = list.removeAt(from)
            list.add(to, cat)
            _rvCats.value = list

            viewModelScope.launch {
                _manualCatOrder.value = list.mapNotNull { it.id }
                grid.saveManualCategoryOrder(_manualCatOrder.value)
            }
        }
    }

    private fun applyFilters() {
        val search = _search.value.trim().lowercase()
        val sortAsc = _sort.value
        val categoryId = _selectedCategoryId.value

        // Items
        var filteredItems = _items.value
        if (categoryId != null) filteredItems = filteredItems.filter { it.categoryId == categoryId }

        // Search
        if (search.isNotEmpty()) {
            filteredItems = filteredItems.filter { it.name.lowercase().contains(search) }
        }

        filteredItems = if (_manualOrder.value.isNotEmpty()) {
            val map = filteredItems.associateBy { it.id }
            _manualOrder.value.mapNotNull { map[it] } + filteredItems.filter { it.id !in _manualOrder.value }
        } else {
            if (sortAsc) filteredItems.sortedBy { it.name.lowercase() } else filteredItems.sortedByDescending { it.name.lowercase() }
        }
        _rvItems.value = filteredItems

        //  Categories
        var filteredCats = if (search.isEmpty()) _categories.value else _categories.value.filter { it.name.lowercase().contains(search) }

        filteredCats = if (_manualCatOrder.value.isNotEmpty()) {
            val map = filteredCats.associateBy { it.id }
            _manualCatOrder.value.mapNotNull { map[it] } + filteredCats.filter { it.id !in _manualCatOrder.value }
        } else {
            if (sortAsc) filteredCats.sortedBy { it.name.lowercase() } else filteredCats.sortedByDescending { it.name.lowercase() }
        }
        _rvCats.value = filteredCats

        _isEmpty.value = if (_mode.value == Mode.ITEMS) _rvItems.value.isEmpty() else _rvCats.value.isEmpty()
    }

    fun getSelectedCategoryName(): String? = _categories.value.find { it.id == _selectedCategoryId.value }?.name

    fun selectCategory(categoryId: Int?) {
        _selectedCategoryId.value = categoryId
        _mode.value = Mode.ITEMS
        applyFilters()
    }

    suspend fun addItemToCart(itemId: Int, increment: Int = 1) {
        val item = _items.value.firstOrNull { it.id == itemId } ?: throw IllegalStateException("Item does not exist")

        if (fakeTicketId == null) {
            val fakeTicket = Ticket(id = null, name = "__TEMP__", total = 0.0)
            val newTicketId = ticketsRepo.addTicket(fakeTicket) ?: throw IllegalStateException("Failed to create ticket")
            fakeTicketId = newTicketId
        }
        val ticketId = fakeTicketId!!
        val existingItems = ticketDetailsRepo.getItems(ticketId).firstOrNull() ?: emptyList()
        val existingDetail = existingItems.firstOrNull { it.itemId == itemId }

        if (existingDetail != null) {
            val updatedDetail = existingDetail.copy(qty = existingDetail.qty + increment)
            ticketDetailsRepo.updateItem(updatedDetail)
        } else {
            val detail = TicketDetail(
                id = null,
                ticketId = ticketId,
                itemId = item.id!!,
                name = item.name,
                price = item.price,
                qty = increment
            )
            ticketDetailsRepo.addItem(detail)
        }

        // Update ticket total
        val allItems = ticketDetailsRepo.getItems(ticketId).firstOrNull() ?: emptyList()
        val newTotal = allItems.sumOf { it.price * it.qty }
        val ticket = ticketsRepo.getTicketById(ticketId)
        if (ticket != null) {
            ticketsRepo.updateTicket(ticket.copy(total = newTotal))
        }

        // Update cart count
        _cartItemCount.value = allItems.sumOf { it.qty }
        _addToTicketItem.value = itemId
    }

    fun clearAddToTicketEvent() {
        _addToTicketItem.value = null
    }

    suspend fun isCartEmpty(): Boolean {
        val cartItems = fakeTicketId?.let { ticketDetailsRepo.getItems(it).firstOrNull() } ?: emptyList()
        return cartItems.isEmpty()
    }

    suspend fun getOccupiedTables(): List<String> {
        val allTickets = ticketsRepo.getTickets().firstOrNull() ?: emptyList()
        val occupiedTables = mutableListOf<String>()
        
        for (ticket in allTickets) {
            if (ticket.status == TicketStatus.SAVED && ticket.name != "__TEMP__") {
                val items = ticketDetailsRepo.getItems(ticket.id!!).firstOrNull() ?: emptyList()
                if (items.isNotEmpty()) {
                    occupiedTables.add(ticket.name)
                }
            }
        }
        
        return occupiedTables
    }

    suspend fun saveCartAsTicket(name: String): Boolean {
        val ticketId = fakeTicketId ?: return false
        val cartItems = ticketDetailsRepo.getItems(ticketId).firstOrNull() ?: emptyList()
        if (cartItems.isEmpty()) return false

        // Check if ticket with same name already exists
        val existingTickets = ticketsRepo.getTickets().firstOrNull() ?: emptyList()
        val existingTicket = existingTickets.find { it.name == name && it.status == TicketStatus.SAVED }

        val targetTicketId: Int = if (existingTicket != null) {
            // Add to existing ticket
            existingTicket.id!!
        } else {
            // Create new ticket
            val ticket = Ticket(id = null, name = name, total = 0.0)
            ticketsRepo.addTicket(ticket)
        }

        // Add cart items to target ticket (merge duplicates)
        val existingItems = ticketDetailsRepo.getItems(targetTicketId).firstOrNull() ?: emptyList()
        
        cartItems.forEach { cartItem ->
            val existingItem = existingItems.find { it.itemId == cartItem.itemId }
            if (existingItem != null) {
                // Item already exists, increase quantity
                ticketDetailsRepo.updateItem(existingItem.copy(qty = existingItem.qty + cartItem.qty))
            } else {
                // New item, add it
                ticketDetailsRepo.addItem(cartItem.copy(id = null, ticketId = targetTicketId))
            }
        }

        // Update ticket total
        val allItems = ticketDetailsRepo.getItems(targetTicketId).firstOrNull() ?: emptyList()
        val newTotal = allItems.sumOf { it.price * it.qty }
        val ticket = ticketsRepo.getTicketById(targetTicketId)
        if (ticket != null) {
            ticketsRepo.updateTicket(ticket.copy(total = newTotal))
        }

        ticketDetailsRepo.clearCart(ticketId)
        ticketsRepo.deleteTicket(ticketId)
        fakeTicketId = null
        _cartItemCount.value = 0
        return true
    }

    fun generateTicketName(): String {
        val timestamp = java.text.SimpleDateFormat("yyyyMMdd-HHmmss", java.util.Locale.getDefault()).format(java.util.Date())
        return "Order-$timestamp"
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as MyApp
                HomeViewModel(
                    itemsRepo = app.itemsRepo,
                    categoriesRepo = app.categoriesRepo,
                    ticketDetailsRepo = app.ticketDetailsRepo,
                    ticketsRepo = app.ticketsRepo,
                    grid = app.grid
                )
            }
        }
    }
}