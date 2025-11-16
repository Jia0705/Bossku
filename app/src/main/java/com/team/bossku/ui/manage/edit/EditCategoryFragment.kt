package com.team.bossku.ui.manage.edit

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.team.bossku.R
import com.team.bossku.ui.manage.base.BaseManageCategoryFragment
import com.team.bossku.ui.popup.DeletePopFragment
import kotlinx.coroutines.launch

class EditCategoryFragment : BaseManageCategoryFragment() {
    override val viewModel: EditCategoryViewModel by viewModels{
        EditCategoryViewModel.Factory
    }
    private val args: EditCategoryFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvHeader.setText(R.string.edit_category)
        binding.ibBack.setOnClickListener { findNavController().popBackStack() }

        lifecycleScope.launch {
            val cat = viewModel.loadCategoryById(args.categoryId)
            if (cat != null) {
                binding.etName.setText(cat.name)
                viewModel.color.value = cat.color
                updateColorBorder(cat.color)
            } else {
                findNavController().popBackStack()
            }
        }

        setupColorListeners()

        // Save
        binding.mbSave.setOnClickListener {
            val name = binding.etName.text?.toString().orEmpty()
            val color = viewModel.color.value.ifBlank { "#FFFFFF" }
            viewModel.submit(name, color)
        }

        // Delete
        binding.mbDelete.visibility = View.VISIBLE
        binding.mbDelete.setOnClickListener {
            val dialog = DeletePopFragment()
            dialog.setListener(object : DeletePopFragment.Listener {
                override fun onClickCancel() { }
                override fun onClickDelete() { viewModel.deleteCategory() }
            })
            dialog.show(parentFragmentManager, "confirm_delete_category")
        }
    }
}