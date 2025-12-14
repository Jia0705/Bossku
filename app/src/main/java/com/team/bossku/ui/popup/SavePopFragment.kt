package com.team.bossku.ui.popup

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.GridLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.team.bossku.R

class SavePopFragment: DialogFragment() {

    interface Listener {
        fun onClickSave(name: String)
    }

    private var listener: Listener?= null
    private var selectedTable: String = ""
    private var occupiedTables: List<String> = emptyList()

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setOccupiedTables(tables: List<String>) {
        occupiedTables = tables
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.fragment_save_pop, null)

        val etName = view.findViewById<EditText>(R.id.etTicketName)
        val btnSave = view.findViewById<MaterialButton>(R.id.mbSave)
        val gridTables = view.findViewById<GridLayout>(R.id.gridTables)

        val dialog = AlertDialog.Builder(requireContext()).setView(view).create()

        // Create table buttons 1-6
        for (i in 1..6) {
            val tableBtn = MaterialButton(requireContext(), null, com.google.android.material.R.attr.materialButtonOutlinedStyle)
            val tableName = "Table $i"
            tableBtn.text = i.toString()
            tableBtn.textSize = 18f
            
            val params = GridLayout.LayoutParams()
            params.width = 0
            params.height = GridLayout.LayoutParams.WRAP_CONTENT
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            params.setMargins(8, 8, 8, 8)
            tableBtn.layoutParams = params

            // Show occupied tables in blue
            if (occupiedTables.contains(tableName)) {
                tableBtn.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.blue))
                tableBtn.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            }

            tableBtn.setOnClickListener {
                selectedTable = tableName
                etName.setText(selectedTable)
            }
            
            gridTables.addView(tableBtn)
        }

        // Save with selected table or custom name
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