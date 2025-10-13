package com.team.bossku.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index("categoryId")]
)

data class Item(
    @PrimaryKey(true)
    val id: Int?= null,
    val name: String,
    val categoryId: Int?= null,
    val price: Double,
    val cost: Double,
    val barcode: String?= null,
    val color: String
)