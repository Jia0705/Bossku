package com.team.bossku.ui.manage.add

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.team.bossku.R
import com.team.bossku.ui.manage.base.BaseManageCategoryFragment

class AddCategoryFragment : BaseManageCategoryFragment() {
    override val viewModel: AddCategoryViewModel by viewModels{
        AddCategoryViewModel.Factory
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvHeader.setText(R.string.add_new_category)
        binding.ibBack.setOnClickListener { findNavController().popBackStack() }
        setupColorListeners()

        // Save
        binding.mbSave.setOnClickListener {
            val name = binding.etName.text?.toString().orEmpty()
            val color = viewModel.color.value.ifBlank { "#FFFFFF" }
            viewModel.submit(name, color)
        }

        binding.mbDelete.visibility = View.GONE
    }
}
