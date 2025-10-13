package com.team.bossku.ui.manage.edit

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.team.bossku.MyApp
import com.team.bossku.data.model.Category
import com.team.bossku.data.repo.CategoriesRepo
import com.team.bossku.ui.manage.base.BaseManageCategoryViewModel
import kotlinx.coroutines.launch

class EditCategoryViewModel(
    private val categoriesRepo: CategoriesRepo
) : BaseManageCategoryViewModel(categoriesRepo) {

    var category: Category? = null

    suspend fun loadCategoryById(id: Int): Category? {
        return try {
            val cat = categoriesRepo.getCategoryById(id)
            if (cat != null) {
                category = cat
                color.value = cat.color
            }
            cat
        } catch (e: Exception) {
            _error.emit(e.message.orEmpty())
            null
        }
    }

    fun deleteCategory() {
        viewModelScope.launch {
            try {
                val id = category?.id ?: throw IllegalArgumentException("Invalid category id")
                categoriesRepo.deleteCategory(id)
                _finish.emit(Unit)
            } catch (e: Exception) {
                _error.emit(e.message.orEmpty())
            }
        }
    }

    override fun submit(
        name: String,
        color: String
    ) {
        viewModelScope.launch {
            try {
                require(name.isNotBlank()) { "Name cannot be blank" }
                val default = color.ifBlank { "#FFFFFF" }

                val newCategory = category?.copy(
                    name = name,
                    color = default
                ) ?: throw IllegalStateException("No Category")

                categoriesRepo.updateCategory(newCategory)
                _finish.emit(Unit)
            } catch (e: Exception) {
                _error.emit(e.message.orEmpty())
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myRepository = (this[APPLICATION_KEY] as MyApp).categoriesRepo
                EditCategoryViewModel(categoriesRepo = myRepository)
            }
        }
    }
}