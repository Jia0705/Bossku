package com.team.bossku.data.repo

import com.team.bossku.data.db.TicketDetailsDao
import com.team.bossku.data.model.TicketDetail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TicketDetailsRepo(
    private val dao: TicketDetailsDao
) {
    suspend fun addItem(item: TicketDetail) {
        dao.addTicketDetail(item)
    }

    fun getItems(ticketId: Int): Flow<List<TicketDetail>> {
        return dao.getCartItems(ticketId)
    }

    suspend fun updateItem(item: TicketDetail) {
        dao.updateTicketDetail(item)
    }

    suspend fun clearCart(ticketId: Int) {
        dao.clearCart(ticketId)
    }

    fun isEmpty(ticketId: Int): Flow<Boolean> {
        return dao.getCartItems(ticketId).map { it.isEmpty() }
    }
}