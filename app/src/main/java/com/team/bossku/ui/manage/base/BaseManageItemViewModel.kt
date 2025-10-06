package com.team.bossku.ui.manage.base

import androidx.lifecycle.ViewModel
import com.team.bossku.data.repo.ItemsRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseManageItemViewModel(
    protected val repo: ItemsRepo
) : ViewModel() {

    val name = MutableStateFlow("")
    val categoryId = MutableStateFlow<Int?>(null)
    val price = MutableStateFlow("")
    val cost = MutableStateFlow("")
    val barcode = MutableStateFlow("")
    val color = MutableStateFlow("")

    protected val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()
    protected val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    abstract fun submit(
        name: String,
        categoryId: Int,
        price: Double,
        cost: Double,
        barcode: String,
        color: String
    )
}
