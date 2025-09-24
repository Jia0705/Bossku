package com.team.bossku.data.model

data class Item(
    val id: Int?= null,
    val name: String,
    val categoryId: Int,
    val price: Double,
    val cost: Double,
    val barcode: String? = null,
    val color: String

)