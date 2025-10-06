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

        val btnSort = view.findViewById<RadioGroup>(R.id.rgSort)
        val btnSortBy = view.findViewById<RadioGroup>(R.id.rgSortBy)
        val btnDone = view.findViewById<MaterialButton>(R.id.mbDone)

        val dialog = AlertDialog.Builder(requireContext()).setView(view).create()

        btnSort.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.rbAsc -> listener?.onSort(true)
                R.id.rbDes -> listener?.onSort(false)
            }
        }

        btnSortBy.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.rbTitle -> listener?.onSortBy(true)
                R.id.rbDatetime -> listener?.onSortBy(false)
            }
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