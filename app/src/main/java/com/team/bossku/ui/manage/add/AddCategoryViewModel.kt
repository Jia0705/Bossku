package com.team.bossku.ui.manage.add

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.team.bossku.MyApp
import com.team.bossku.data.model.Category
import com.team.bossku.data.repo.CategoriesRepo
import com.team.bossku.ui.manage.base.BaseManageCategoryViewModel
import kotlinx.coroutines.launch

class AddCategoryViewModel(
    private val categoriesRepo: CategoriesRepo
) : BaseManageCategoryViewModel(categoriesRepo) {

    override fun submit(
        name: String,
        color: String
    ) {
        viewModelScope.launch {
            try {
                require(name.isNotBlank()) { "Name cannot be blank" }

                val default = color.ifBlank { "#FFFFFF" }

                val category = Category(
                    name = name,
                    color = default
                )

                categoriesRepo.addCategory(category)
                _finish.emit(Unit)
            } catch (e: Exception) {
                _error.emit(e.message.toString())
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as MyApp).categoriesRepo
                AddCategoryViewModel(categoriesRepo = myRepository)
            }
        }
    }
}