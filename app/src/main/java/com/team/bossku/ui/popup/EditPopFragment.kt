package com.team.bossku.ui.popup

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.team.bossku.R

class EditPopFragment(
    private val oldQty: Int? = null
) : DialogFragment() {

    interface Listener {
        fun onClickSave(newQty: Int)
    }

    private var listener: Listener?= null

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_edit_pop, null)

        val etQty = view.findViewById<EditText>(R.id.etQty)
        val btnSave = view.findViewById<MaterialButton>(R.id.mbSave)

        etQty.setText(oldQty?.toString() ?: "")

        val dialog = AlertDialog.Builder(requireContext()).setView(view).create()

        btnSave.setOnClickListener {
            val qty = etQty.text?.toString()?.trim()?.toIntOrNull()
            if (qty != null && qty >= 0) {
                btnSave.isEnabled = false
                listener?.onClickSave(qty)
                dialog.dismiss()
            } else {
                etQty.error = getString(R.string.enter_quantity)
            }
        }
        return dialog
    }

    override fun onDestroy() {
        super.onDestroy()
        listener = null
    }
}