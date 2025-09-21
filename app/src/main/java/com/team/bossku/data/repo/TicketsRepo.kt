package com.team.bossku.data.repo

import com.team.bossku.data.model.Ticket

class TicketsRepo private constructor() {
    val map = mutableMapOf<Int, Ticket>()
    var counter = 0

    fun addTicket(ticket: Ticket) {
        counter = counter + 1
        map[counter] = ticket.copy(id = counter)
    }

    fun getTicketById(id: Int): Ticket? {
        return map[id]
    }

    fun getTickets() = map.values.toList()

    fun updateTicket(id: Int, ticket: Ticket) {
        map[id] = ticket.copy(id = id)
    }

    fun deleteTicket(id: Int) {
        map.remove(id)
    }

    companion object {
        private var instance: TicketsRepo? = null

        fun getInstance(): TicketsRepo {
            if (instance == null) {
                instance = TicketsRepo()
            }
            return instance!!
        }
    }
}