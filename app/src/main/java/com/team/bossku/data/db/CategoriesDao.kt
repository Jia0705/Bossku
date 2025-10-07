package com.team.bossku.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.team.bossku.data.model.Category
import com.team.bossku.data.model.CategoryWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM Category")
    fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM Category WHERE id = :id")
    suspend fun getCategoryById(id: Int): Category?

    @Insert
    suspend fun addCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Query("DELETE FROM Category WHERE id = :id")
    suspend fun deleteCategory(id: Int)

    @Transaction
    @Query("SELECT * FROM Category")
    fun getAllCategoriesWithItems(): Flow<List<CategoryWithItems>>

    @Transaction
    @Query("SELECT * FROM Category WHERE id = :id LIMIT 1")
    fun getCategoryWithItemsById(id: Int): Flow<CategoryWithItems?>
}