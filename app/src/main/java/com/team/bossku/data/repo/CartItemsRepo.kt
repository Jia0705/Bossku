package com.team.bossku.data.repo

import com.team.bossku.data.model.Item

class CartItemsRepo private constructor() {
    val map = mutableMapOf<Int, Item>()
    val qty = mutableMapOf<Int, Int>()

    fun addItem(item: Item, addQty: Int = 1) {
        if (item.id != null) {
            map[item.id] = item
            val current = qty[item.id] ?: 0
            qty[item.id] = current + addQty
        }
    }

    fun getItems(): List<Item> {
        return map.values.toList()
    }

    fun getQty(itemId: Int): Int {
        val count = qty[itemId]
        if (count != null) {
            return count
        }
        return 0
    }

    fun clearCart() {
        map.clear()
        qty.clear()
    }

    fun isEmpty(): Boolean {
        return map.isEmpty()
    }

    companion object {
        private var instance: CartItemsRepo? = null

        fun getInstance(): CartItemsRepo {
            if (instance == null) {
                instance = CartItemsRepo()
            }
            return instance!!
        }
    }
}