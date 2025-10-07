package com.team.bossku.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.team.bossku.data.model.Item
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemsDao {
    @Query("SELECT * FROM Item")
    fun getAllItems(): Flow<List<Item>>

    @Query("SELECT * FROM Item WHERE id = :id")
    suspend fun getItemById(id: Int): Item?

    @Insert
    suspend fun addItem(item: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Query("DELETE FROM Item WHERE id = :id")
    suspend fun deleteItem(id: Int)
}