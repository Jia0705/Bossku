package com.team.bossku.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.team.bossku.data.model.Category
import com.team.bossku.data.model.Item
import com.team.bossku.data.model.Ticket
import com.team.bossku.data.model.TicketDetail

@Database(entities = [Item::class, Category::class, Ticket::class, TicketDetail::class], version = 1)
@TypeConverters(RoomConverters::class)
abstract class MyDatabase: RoomDatabase() {

    abstract fun itemsDao(): ItemsDao
    abstract fun categoriesDao(): CategoriesDao
    abstract fun ticketsDao(): TicketsDao
    abstract fun ticketDetailsDao(): TicketDetailsDao

    companion object {
        const val NAME = "my_database"
    }
}