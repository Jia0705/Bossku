package com.team.bossku.data.repo

import com.team.bossku.data.model.Category

class CategoriesRepo private constructor() {
    val map = mutableMapOf<Int, Category>()
    var counter = 0

    fun addCategory(category: Category) {
        counter = counter + 1
        map[counter] = category.copy(id = counter)
    }

    fun getCategoryById(id: Int): Category? {
        return map[id]
    }

    fun getCategories() = map.values.toList()

    fun updateCategory(id: Int, category: Category) {
        map[id] = category.copy(id = id)
    }

    fun deleteCategory(id: Int) {
        map.remove(id)
    }

    companion object {
        private var instance: CategoriesRepo? = null

        fun getInstance(): CategoriesRepo {
            if (instance == null) {
                instance = CategoriesRepo()
            }
            return instance!!
        }
    }
}