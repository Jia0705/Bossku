package com.team.bossku.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.team.bossku.data.model.TicketDetail
import kotlinx.coroutines.flow.Flow

@Dao
interface TicketDetailsDao {
    @Query("SELECT * FROM TicketDetail")
    fun getAllTicketDetails(): Flow<List<TicketDetail>>

    @Query("SELECT * FROM TicketDetail WHERE id = :id")
    suspend fun getTicketDetailById(id: Int): TicketDetail?

    @Insert
    suspend fun addTicketDetail(ticketDetail: TicketDetail)

    @Update
    suspend fun updateTicketDetail(ticketDetail: TicketDetail)

    @Query("DELETE FROM TicketDetail WHERE id = :id")
    suspend fun deleteTicketDetail(id: Int)

    @Query("SELECT * FROM TicketDetail WHERE ticketId = :ticketId")
    fun getCartItems(ticketId: Int): Flow<List<TicketDetail>>

    @Query("DELETE FROM TicketDetail WHERE ticketId = :ticketId")
    suspend fun clearCart(ticketId: Int)
}