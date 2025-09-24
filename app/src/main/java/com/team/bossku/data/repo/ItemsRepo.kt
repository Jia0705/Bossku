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

//    init {
//        createRandomItems(10)
//    }
//
//    fun createRandomItems(n: Int) {
//        var count = 0
//        while (count < n) {
//            val item = Item(
//                id = null,
//                name = "Item $count",
//                categoryId = 0,
//                price = 0.0,
//                cost = 0.0,
//                barcode = null
//            )
//            addItem(item)
//            count++
//        }
//    }


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