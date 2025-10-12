package com.team.bossku

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.room.Room
import com.google.android.material.color.DynamicColors
import com.team.bossku.data.db.MyDatabase
import com.team.bossku.data.ds.Grid
import com.team.bossku.data.repo.TicketDetailsRepo
import com.team.bossku.data.repo.CategoriesRepo
import com.team.bossku.data.repo.ItemsRepo
import com.team.bossku.data.repo.TicketsRepo
import com.team.bossku.data.ds.Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyApp: Application() {
    lateinit var itemsRepo: ItemsRepo
    lateinit var categoriesRepo: CategoriesRepo
    lateinit var ticketDetailsRepo: TicketDetailsRepo
    lateinit var ticketsRepo: TicketsRepo
    lateinit var theme: Theme
    lateinit var grid: Grid

    override fun onCreate() {
        super.onCreate()
        DynamicColors.applyToActivitiesIfAvailable(this)

        val db = Room.databaseBuilder(
            this,
            MyDatabase::class.java,
            MyDatabase.NAME
        ) // .fallbackToDestructiveMigration()
            .build()

        itemsRepo = ItemsRepo(db.itemsDao())
        categoriesRepo = CategoriesRepo(db.categoriesDao())
        ticketDetailsRepo = TicketDetailsRepo(db.ticketDetailsDao())
        ticketsRepo = TicketsRepo(
            dao = db.ticketsDao(),
            ticketDetailsDao = db.ticketDetailsDao()
        )

        theme = Theme(this)
        grid = Grid(this)

        CoroutineScope(Dispatchers.Main).launch {
            theme.isDarkMode.collect { isDark ->
                AppCompatDelegate.setDefaultNightMode(
                    if (isDark) AppCompatDelegate.MODE_NIGHT_YES
                    else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        }
    }
}