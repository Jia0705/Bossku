package com.team.bossku.ui.manage.edit

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.team.bossku.MyApp
import com.team.bossku.data.model.Item
import com.team.bossku.data.repo.ItemsRepo
import com.team.bossku.ui.manage.base.BaseManageItemViewModel
import kotlinx.coroutines.launch

class EditItemViewModel(
    private val itemsRepo: ItemsRepo
) : BaseManageItemViewModel(itemsRepo) {

    lateinit var item: Item

    fun loadItemById(id: Int) {
        viewModelScope.launch {
            try {
                val selectedItem = itemsRepo.getItemById(id) ?: throw IllegalArgumentException("Item not found")
                item = selectedItem
                color.value = selectedItem.color
            } catch (e: Exception) {
                _error.emit(e.message.orEmpty())
            }
        }
    }

    fun deleteItem() {
        viewModelScope.launch {
            try {
                val id = item.id ?: throw IllegalArgumentException("Invalid item id")
                itemsRepo.deleteItem(id)
                _finish.emit(Unit)
            } catch (e: Exception) {
                _error.emit(e.message.orEmpty())
            }
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
        viewModelScope.launch {
            try {
                require(name.isNotBlank()) { "Name cannot be blank" }
                require(price > 0) { "Price must be greater than 0" }

            val default = color.ifBlank { "#FFFFFF" }

                val newItem = item.copy(
                    name = name,
                    categoryId = if (categoryId == -1) null else categoryId,
                    price = price,
                    cost = cost,
                    barcode = barcode,
                    color = default
                )

                itemsRepo.updateItem(newItem)
                _finish.emit(Unit)
            } catch (e: Exception) {
                _error.emit(e.message.toString())
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as MyApp).itemsRepo
                EditItemViewModel(itemsRepo = myRepository)
            }
        }
    }
}