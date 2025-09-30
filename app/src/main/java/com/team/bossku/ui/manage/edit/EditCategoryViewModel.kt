package com.team.bossku.ui.manage.edit

import androidx.lifecycle.viewModelScope
import com.team.bossku.data.model.Category
import com.team.bossku.data.repo.CategoriesRepo
import com.team.bossku.ui.manage.base.BaseManageCategoryViewModel
import kotlinx.coroutines.launch

class EditCategoryViewModel(
    private val categoriesRepo: CategoriesRepo = CategoriesRepo.getInstance()
) : BaseManageCategoryViewModel(categoriesRepo) {

    lateinit var category: Category

    fun loadCategoryById(id: Int) {
        category = categoriesRepo.getCategoryById(id) ?: throw IllegalArgumentException("Category not found")
        color.value = category.color
    }

    fun deleteCategory() {
        try {
            val id = category.id ?: throw IllegalArgumentException("Invalid item id")
            categoriesRepo.deleteCategory(id)
            viewModelScope.launch { _finish.emit(Unit) }
        } catch (e: Exception) {
            viewModelScope.launch { _error.emit(e.message.orEmpty()) }
        }
    }

    override fun submit(
        name: String,
        color: String
    ) {
        try {
            require(name.isNotBlank()) { "Name cannot be blank" }

            val default = color.ifBlank { "#FFFFFF" }

            val newCategory = category.copy(
                name = name,
                color = default
            )
            val id = requireNotNull(newCategory.id) { "Invalid category id" }
            categoriesRepo.updateCategory(id, newCategory)

            viewModelScope.launch {
                _finish.emit(Unit)
            }
        } catch (e: Exception) {
            viewModelScope.launch { _error.emit(e.message.toString()) }
        }
    }
}