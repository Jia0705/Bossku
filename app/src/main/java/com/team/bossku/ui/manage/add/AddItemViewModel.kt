package com.team.bossku.ui.manage.add

import androidx.lifecycle.viewModelScope
import com.team.bossku.data.model.Item
import com.team.bossku.data.repo.ItemsRepo
import com.team.bossku.ui.manage.base.BaseManageItemViewModel
import kotlinx.coroutines.launch

class AddItemViewModel(
    private val itemsRepo: ItemsRepo = ItemsRepo.getInstance()
) : BaseManageItemViewModel(itemsRepo) {

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

            val item = Item(
                name = name,
                categoryId = categoryId,
                price = price,
                cost = cost,
                barcode = barcode,
                color = default
            )

            itemsRepo.addItem(item)

            viewModelScope.launch {
                _finish.emit(Unit)
            }
        } catch (e: Exception) {
            viewModelScope.launch { _error.emit(e.message.toString()) }
        }
    }
}

