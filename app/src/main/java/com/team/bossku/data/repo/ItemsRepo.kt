package com.team.bossku.data.repo

import com.team.bossku.data.db.ItemsDao
import com.team.bossku.data.model.Item
import kotlinx.coroutines.flow.Flow

class ItemsRepo(
    private val dao: ItemsDao
) {
    suspend fun addItem(item: Item) {
        dao.addItem(item)
    }

    suspend fun getItemById(id: Int): Item? {
        return dao.getItemById(id)
    }

    fun getItems(): Flow<List<Item>> {
        return dao.getAllItems()
    }

    suspend fun updateItem(item: Item) {
        dao.updateItem(item)
    }

    suspend fun deleteItem(id: Int) {
        dao.deleteItem(id)
    }
}