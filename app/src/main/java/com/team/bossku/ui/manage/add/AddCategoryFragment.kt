package com.team.bossku.ui.manage.add

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.team.bossku.R
import com.team.bossku.ui.manage.base.BaseManageCategoryFragment

class AddCategoryFragment : BaseManageCategoryFragment() {
    override val viewModel: AddCategoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvHeader.setText(R.string.add_new_category)
        binding.ibBack.setOnClickListener { findNavController().popBackStack() }

        // Color
        binding.c1.setOnClickListener { viewModel.color.value = "#FFFF0000" }
        binding.c2.setOnClickListener { viewModel.color.value = "#FFFFA500" }
        binding.c3.setOnClickListener { viewModel.color.value = "#FFFFFF00" }
        binding.c4.setOnClickListener { viewModel.color.value = "#FF00FF00" }
        binding.c5.setOnClickListener { viewModel.color.value = "#FF0000FF" }

        // Save
        binding.mbSave.setOnClickListener {
            val name  = binding.etName.text?.toString().orEmpty()
            val color = viewModel.color.value.ifBlank { "#FFFFFF" }
            viewModel.submit(name, color)
        }
    }
}
