package com.team.bossku.ui.manage.edit

import androidx.lifecycle.viewModelScope
import com.team.bossku.data.model.Item
import com.team.bossku.data.repo.ItemsRepo
import com.team.bossku.ui.manage.base.BaseManageItemViewModel
import kotlinx.coroutines.launch

class EditItemViewModel(
    private val itemsRepo: ItemsRepo = ItemsRepo.getInstance()
) : BaseManageItemViewModel(itemsRepo) {

    lateinit var item: Item

    fun loadItemById(id: Int) {
        item = itemsRepo.getItemById(id) ?: throw IllegalArgumentException("Item not found")
        color.value = item.color
    }

    fun deleteItem() {
        try {
            val id = item.id ?: throw IllegalArgumentException("Invalid item id")
            itemsRepo.deleteItem(id)
            viewModelScope.launch { _finish.emit(Unit) }
        } catch (e: Exception) {
            viewModelScope.launch { _error.emit(e.message.orEmpty()) }
        }
    }

    override fun submit(
        name: String,
        categoryId: Int,
        price: Double,
        cost: Double,
        barcode: String,
        color: String
    ) {
        try {
            require(name.isNotBlank()) { "Name cannot be blank" }
            require(price > 0) { "Price must be greater than 0" }

            val default = color.ifBlank { "#FFFFFF" }

            val newItem = item.copy(
                name = name,
                categoryId = categoryId,
                price = price,
                cost = cost,
                barcode = barcode,
                color = default
            )
            val id = requireNotNull(newItem.id) { "Invalid item id" }
            itemsRepo.updateItem(id, newItem)

            viewModelScope.launch {
                _finish.emit(Unit)
            }
        } catch (e: Exception) {
            viewModelScope.launch { _error.emit(e.message.toString()) }
        }
    }
}

