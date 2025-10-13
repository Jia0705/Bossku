package com.team.bossku.data.ds

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("grid")

class Grid(
    private val context: Context
) {
    companion object {
        val MANUAL_ORDER_KEY = stringPreferencesKey("manual_order")
        val SORT_ASC_KEY = booleanPreferencesKey("sort_asc")
        val CATEGORY_ORDER_KEY = stringPreferencesKey("category_order")
    }

    val manualOrder: Flow<String> = context.dataStore.data.map { it[MANUAL_ORDER_KEY] ?: "" }

    suspend fun saveManualOrder(id: List<Int>) {
        context.dataStore.edit { it[MANUAL_ORDER_KEY] = id.joinToString(",") }
    }

    val manualCategoryOrder: Flow<String> = context.dataStore.data.map { it[CATEGORY_ORDER_KEY] ?: "" }

    suspend fun saveManualCategoryOrder(id: List<Int>) {
        context.dataStore.edit { it[CATEGORY_ORDER_KEY] = id.joinToString(",") }
    }

    val sortAscending: Flow<Boolean> = context.dataStore.data.map { it[SORT_ASC_KEY] ?: true }

    suspend fun saveSortAscending(asc: Boolean) {
        context.dataStore.edit { it[SORT_ASC_KEY] = asc }
    }
}