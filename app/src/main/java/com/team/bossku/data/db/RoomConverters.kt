package com.team.bossku.data.db

import androidx.room.TypeConverter
import com.team.bossku.data.model.TicketStatus

class RoomConverters {
    @TypeConverter
    fun fromTicketStatus(status: TicketStatus): String = status.name

    @TypeConverter
    fun toTicketStatus(value: String): TicketStatus = TicketStatus.valueOf(value)
}