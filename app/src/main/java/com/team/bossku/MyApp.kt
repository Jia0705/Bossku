package com.team.bossku

import android.app.Application
import androidx.room.Room
import com.google.android.material.color.DynamicColors
import com.team.bossku.data.db.MyDatabase
import com.team.bossku.data.repo.CartItemsRepo
import com.team.bossku.data.repo.CategoriesRepo
import com.team.bossku.data.repo.ItemsRepo
import com.team.bossku.data.repo.TicketsRepo

class MyApp: Application() {
    lateinit var itemsRepo: ItemsRepo
    lateinit var categoriesRepo: CategoriesRepo
    lateinit var cartItemsRepo: CartItemsRepo
    lateinit var ticketsRepo: TicketsRepo

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)

        val db = Room.databaseBuilder(
            this,
            MyDatabase::class.java,
            MyDatabase.NAME
        )
            // .fallbackToDestructiveMigration()
            .build()

        itemsRepo = ItemsRepo(db.itemsDao())
        categoriesRepo = CategoriesRepo(db.categoriesDao())
        cartItemsRepo = CartItemsRepo(db.ticketDetailsDao())
        ticketsRepo = TicketsRepo(db.ticketsDao())
    }
}