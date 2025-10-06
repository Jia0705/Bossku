package com.team.bossku.ui.manage.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BaseManageListViewModel : ViewModel() {
    var loadAll: (() -> List<Any>)? = null
    var matches: ((Any, String) -> Boolean)? = null
    var sortKey: ((Any) -> String)? = null

    private var all: List<Any> = emptyList()

    private val _list = MutableStateFlow<List<Any>>(emptyList())
    val list: StateFlow<List<Any>> = _list

    private val _search = MutableStateFlow("")
    private val _sort = MutableStateFlow(true)
    val sortAscending: StateFlow<Boolean> = _sort


    fun refresh() {
        val loader = loadAll ?: return
        all = loader()
        applyFilters()
    }

    fun setSearch(text: String) {
        _search.value = text
        applyFilters()
    }

    fun setSort() {
        _sort.value = !_sort.value
        applyFilters()
    }

    private fun applyFilters() {
        val search = _search.value.trim().lowercase()
        val filter = matches ?: { _, _ -> true }
        val key = sortKey ?: { "" }

        val filtered = if (search.isEmpty()) {
            all
        } else {
            all.filter { filter(it, search) }
        }

        _list.value = if (_sort.value) {
            filtered.sortedBy { key(it).lowercase() }
        } else {
            filtered.sortedByDescending { key(it).lowercase() }
        }
    }
}
