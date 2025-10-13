package com.team.bossku.ui.popup

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RadioGroup
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.team.bossku.R

class SortPopFragment : DialogFragment() {

    interface Listener {
        fun onClickDone()
        fun onSort(isAscending: Boolean)
        fun onSortBy(isName: Boolean)
    }

    private var listener: Listener?= null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_sort_pop, null)

        val rgSort = view.findViewById<RadioGroup>(R.id.rgSort)
        val rgSortBy = view.findViewById<RadioGroup>(R.id.rgSortBy)
        val btnDone = view.findViewById<MaterialButton>(R.id.mbDone)

        val dialog = AlertDialog.Builder(requireContext()).setView(view).create()

        var isSortByName = true

        rgSortBy.setOnCheckedChangeListener { _, checkedId ->
            isSortByName = checkedId == R.id.rbTitle
            listener?.onSortBy(isSortByName)
        }

        rgSort.setOnCheckedChangeListener { _, checkedId ->
            val isAscSelected = checkedId == R.id.rbAsc

            val finalSort = if (isSortByName) {
                isAscSelected
            } else {
                isAscSelected
            }

            listener?.onSort(finalSort)
        }

        btnDone.setOnClickListener {
            listener?.onClickDone()
            dialog.dismiss()
        }
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }
}