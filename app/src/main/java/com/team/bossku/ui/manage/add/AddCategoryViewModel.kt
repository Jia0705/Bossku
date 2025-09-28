package com.team.bossku.ui.manage.add

import androidx.lifecycle.viewModelScope
import com.team.bossku.data.model.Category
import com.team.bossku.data.repo.CategoriesRepo
import com.team.bossku.ui.manage.base.BaseManageCategoryViewModel
import kotlinx.coroutines.launch

class AddCategoryViewModel(
    private val categoriesRepo: CategoriesRepo = CategoriesRepo.getInstance()
) : BaseManageCategoryViewModel(categoriesRepo) {

    override fun submit(
        name: String,
        color: String
    ) {
        try {
            require(name.isNotBlank()) { "Name cannot be blank" }

            val default = color.ifBlank { "#FFFFFF" }

            val category = Category(
                name = name,
                color = default
            )

            categoriesRepo.addCategory(category)

            viewModelScope.launch {
                _finish.emit(Unit)
            }
        } catch (e: Exception) {
            viewModelScope.launch { _error.emit(e.message.toString()) }
        }
    }
}