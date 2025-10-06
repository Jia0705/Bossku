package com.team.bossku.ui.manage.base

import androidx.lifecycle.ViewModel
import com.team.bossku.data.repo.CategoriesRepo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseManageCategoryViewModel(
    protected val repo: CategoriesRepo
) : ViewModel() {

    val name = MutableStateFlow("")
    val color = MutableStateFlow("")

    protected val _finish = MutableSharedFlow<Unit>()
    val finish = _finish.asSharedFlow()
    protected val _error = MutableSharedFlow<String>()
    val error: SharedFlow<String> = _error

    abstract fun submit(
        name: String,
        color: String
    )
}