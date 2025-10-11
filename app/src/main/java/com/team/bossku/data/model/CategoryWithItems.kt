package com.team.bossku.data.model

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithItems(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "categoryId"
    )
    val items: List<Item>
)