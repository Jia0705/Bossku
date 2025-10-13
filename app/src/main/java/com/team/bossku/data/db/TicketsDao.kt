package com.team.bossku.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.team.bossku.data.model.Ticket
import com.team.bossku.data.model.TicketWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketsDao {
    @Query("SELECT * FROM Ticket")
    fun getAllTickets(): Flow<List<Ticket>>

    @Query("SELECT * FROM Ticket WHERE id = :id")
    suspend fun getTicketById(id: Int): Ticket?

    @Query("SELECT * FROM Ticket WHERE status = 'SAVED' LIMIT 1")
    suspend fun getCurrentCart(): Ticket?

    @Insert
    suspend fun addTicket(ticket: Ticket): Long

    @Update
    suspend fun updateTicket(ticket: Ticket)

    @Query("DELETE FROM Ticket WHERE id = :id")
    suspend fun deleteTicket(id: Int)

    @Transaction
    @Query("SELECT * FROM Ticket")
    fun getAllTicketsWithDetails(): Flow<List<TicketWithDetails>>

    @Transaction
    @Query("SELECT * FROM Ticket WHERE id = :id LIMIT 1")
    fun getTicketWithDetailsById(id: Int): Flow<TicketWithDetails?>
}