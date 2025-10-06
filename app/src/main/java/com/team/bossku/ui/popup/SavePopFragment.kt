package com.team.bossku.ui.popup

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.team.bossku.R

class SavePopFragment: DialogFragment() {

    interface Listener {
        fun onClickSave(name: String)
    }

    private var listener: Listener?= null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_save_pop, null)

        val etName = view.findViewById<EditText>(R.id.etTicketName)
        val btnSave = view.findViewById<MaterialButton>(R.id.mbSave)

        val dialog = AlertDialog.Builder(requireContext()).setView(view).create()

        btnSave.setOnClickListener {
            val name = etName.text?.toString()?.trim().orEmpty()
            if (name.isNotEmpty()) {
                listener?.onClickSave(name)
                dialog.dismiss()
            } else {
                etName.error = getString(R.string.enter_ticket_name)
            }
        }
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }
}