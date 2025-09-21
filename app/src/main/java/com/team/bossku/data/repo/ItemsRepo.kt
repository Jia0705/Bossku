package com.team.bossku.data.repo

import com.team.bossku.data.model.Item

class ItemsRepo private constructor() {
    val map = mutableMapOf<Int, Item>()
    var counter = 0

    fun addItem(item: Item) {
        counter = counter + 1
        map[counter] = item.copy(id = counter)
    }

    fun getItemById(id: Int): Item? {
        return map[id]
    }

    fun getItems() = map.values.toList()

    fun updateItem(id: Int, item: Item) {
        map[id] = item.copy(id = id)
    }

    fun deleteItem(id: Int) {
        map.remove(id)
    }

    companion object {
        private var instance: ItemsRepo? = null

        fun getInstance(): ItemsRepo {
            if (instance == null) {
                instance = ItemsRepo()
            }
            return instance!!
        }
    }
}