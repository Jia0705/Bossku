package com.team.bossku.ui.home

import androidx.lifecycle.ViewModel
import com.team.bossku.data.model.Category
import com.team.bossku.data.model.Item
import com.team.bossku.data.repo.CategoriesRepo
import com.team.bossku.data.repo.ItemsRepo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// Switch mode for Home screen
enum class Mode { ITEMS, CATEGORIES }

class HomeViewModel(
    private val itemsRepo: ItemsRepo = ItemsRepo.getInstance(),
    private val categoriesRepo: CategoriesRepo = CategoriesRepo.getInstance()
) : ViewModel() {
    private val _items = MutableStateFlow<List<Item>>(emptyList())
    private val _categories = MutableStateFlow<List<Category>>(emptyList())

    // Mode
    private val _mode = MutableStateFlow(Mode.ITEMS)
    val mode: StateFlow<Mode> = _mode

    // Search and sort (true = A to Z, false = Z to A)
    private val _search = MutableStateFlow("")
    private val _sort = MutableStateFlow(true)
    val sortAscending: StateFlow<Boolean> = _sort

    // The lists the RecyclerView will display
    private val _rvItems = MutableStateFlow<List<Item>>(emptyList())
    val rvItems: StateFlow<List<Item>> = _rvItems

    private val _rvCats = MutableStateFlow<List<Category>>(emptyList())
    val rvCategories: StateFlow<List<Category>> = _rvCats

    // Empty view
    private val _isEmpty = MutableStateFlow(true)
    val isEmpty: StateFlow<Boolean> = _isEmpty

    // Add to ticket
    private val _addToTicketItem = MutableStateFlow<Int?>(null)
    val addToTicketItem: StateFlow<Int?> = _addToTicketItem

    init {
        refresh()
    }

    // Reload everything from repos, then apply search/sort
    fun refresh() {
        _items.value = itemsRepo.getItems()
        _categories.value = categoriesRepo.getCategories()
        applyFilters()
    }

    // Switch Item / Category mode
    fun switchMode(newMode: Mode) {
        if (_mode.value != newMode) {
            _mode.value = newMode
            applyFilters()
        }
    }

    // Search
    fun setSearch(text: String) {
        _search.value = text
        applyFilters()
    }

    // Sort A to Z / Z to A
    fun setSort() {
        _sort.value = !_sort.value
        applyFilters()
    }

    // Swap grid
    fun move(from: Int, to: Int) {
        if (_mode.value == Mode.ITEMS) {
            val list = _rvItems.value.toMutableList()
            if (from in list.indices && to in list.indices) {
                val moved = list.removeAt(from)
                list.add(to, moved)
                _rvItems.value = list
            }
        } else {
            val list = _rvCats.value.toMutableList()
            if (from in list.indices && to in list.indices) {
                val moved = list.removeAt(from)
                list.add(to, moved)
                _rvCats.value = list
            }
        }
    }

    // Click item then add to ticket
    fun addItemToTicket(id: Int) {
        _addToTicketItem.value = id
    }
    fun clearAddToTicketEvent() {
        _addToTicketItem.value = null
    }

    // SEARCH + SORT
    private fun applyFilters() {
        val search = _search.value.trim().lowercase()
        val sort = _sort.value

        // Items: filter by name, then sort
        val itemsFiltered = if (search.isEmpty()) _items.value
        else _items.value.filter { it.name.lowercase().contains(search) }
        _rvItems.value = if (sort)
            itemsFiltered.sortedBy { it.name.lowercase() }
        else
            itemsFiltered.sortedByDescending { it.name.lowercase() }

        // Categories: filter by name, then sort
        val catsFiltered = if (search.isEmpty()) _categories.value
        else _categories.value.filter { it.name.lowercase().contains(search) }
        _rvCats.value = if (sort)
            catsFiltered.sortedBy { it.name.lowercase() }
        else
            catsFiltered.sortedByDescending { it.name.lowercase() }

        // Update empty state for current mode
        _isEmpty.value = if (_mode.value == Mode.ITEMS)
            _rvItems.value.isEmpty()
        else
            _rvCats.value.isEmpty()
    }
}
