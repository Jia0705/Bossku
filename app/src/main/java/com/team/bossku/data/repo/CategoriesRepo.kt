package com.team.bossku.data.repo

import com.team.bossku.data.db.CategoriesDao
import com.team.bossku.data.model.Category
import kotlinx.coroutines.flow.Flow

class CategoriesRepo(
    private val dao: CategoriesDao
) {
    suspend fun addCategory(category: Category) {
        dao.addCategory(category)
    }

    suspend fun getCategoryById(id: Int): Category? {
        return dao.getCategoryById(id)
    }

    fun getCategories(): Flow<List<Category>> {
        return dao.getAllCategories()
    }

    suspend fun updateCategory(category: Category) {
        dao.updateCategory(category)
    }

    suspend fun deleteCategory(id: Int) {
        dao.deleteCategory(id)
    }
}