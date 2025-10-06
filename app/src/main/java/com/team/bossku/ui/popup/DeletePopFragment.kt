package com.team.bossku.ui.popup

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.team.bossku.R

class DeletePopFragment : DialogFragment() {

    interface Listener {
        fun onClickCancel()
        fun onClickDelete()
    }

    private var listener: Listener?= null

    fun setListener(listener: Listener) {
        this.listener =listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_delete_pop, null)

        val btnCancel = view.findViewById<MaterialButton>(R.id.mbCancel)
        val btnDelete = view.findViewById<MaterialButton>(R.id.mbDelete)

        val dialog = AlertDialog.Builder(requireContext()).setView(view).create()

        btnCancel.setOnClickListener {
            listener?.onClickCancel()
            dialog.dismiss()
        }

        btnDelete.setOnClickListener {
            listener?.onClickDelete()
            dialog.dismiss()
        }
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }
}