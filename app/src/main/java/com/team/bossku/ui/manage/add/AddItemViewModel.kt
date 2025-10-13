package com.team.bossku.ui.manage.add

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
import kotlin.toString

class AddItemViewModel(
    private val itemsRepo: ItemsRepo
) : BaseManageItemViewModel(itemsRepo) {

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

                val item = Item(
                    name = name,
                    categoryId = if (categoryId == -1) null else categoryId,
                    price = price,
                    cost = cost,
                    barcode = barcode,
                    color = default
                )

                itemsRepo.addItem(item)
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
                AddItemViewModel(itemsRepo = myRepository)
            }
        }
    }
}