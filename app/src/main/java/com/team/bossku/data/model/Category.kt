package com.team.bossku.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category (
    @PrimaryKey(true)
    val id: Int?= null,
    val name: String,
    val color: String
)