package com.team.bossku.ui.manage.add

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.team.bossku.R
import com.team.bossku.data.repo.CategoriesRepo
import com.team.bossku.ui.manage.base.BaseManageItemFragment

class AddItemFragment : BaseManageItemFragment() {
    override val viewModel: AddItemViewModel by viewModels()

    // store the chosen category id
    private var storeCategoryId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvHeader.setText(R.string.add_new_item)
        binding.ibBack.setOnClickListener { findNavController().popBackStack() }
        binding.mbSave.setOnClickListener { saveItem() }
        binding.mbDelete.visibility = View.GONE

        // Color
        binding.c1.setOnClickListener { viewModel.color.value = "#FFFF0000" }
        binding.c2.setOnClickListener { viewModel.color.value = "#FFFFA500" }
        binding.c3.setOnClickListener { viewModel.color.value = "#FFFFFF00" }
        binding.c4.setOnClickListener { viewModel.color.value = "#FF00FF00" }
        binding.c5.setOnClickListener { viewModel.color.value = "#FF0000FF" }

        // Category dropdown
        categoryDropdown(newCategory = false)

        // When back from Add Category, rebuild and select the new one
        setFragmentResultListener("manage_category") { _, _ ->
            categoryDropdown(newCategory = true)
        }
    }

    private fun categoryDropdown(newCategory: Boolean) {
        val repo = CategoriesRepo.getInstance()
        val cats = repo.getCategories()

        // [No category] + real categories + [Add new category]
        val display = mutableListOf(getString(R.string.no_category))
        display.addAll(cats.map { it.name })
        display.add(getString(R.string.add_new_category))

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            display
        )
        binding.acCategory.setAdapter(adapter)

        // Show full list when clicked
        binding.acCategory.setOnClickListener {
            // reset filter and show the full dropdown
            (binding.acCategory.adapter as ArrayAdapter<*>).filter.filter(null)
            binding.acCategory.showDropDown()
        }

        binding.acCategory.setOnItemClickListener { _, _, pos, _ ->
            when (pos) {
                0 -> { // No category
                    storeCategoryId = -1
                    binding.acCategory.setText(getString(R.string.no_category), false)
                }
                display.lastIndex -> { // Add new category
                    storeCategoryId = -1
                    binding.acCategory.setText(getString(R.string.no_category), false)
                    findNavController().navigate(R.id.addCategoryFragment)
                }
                else -> {
                    val cat = cats[pos - 1]
                    storeCategoryId = cat.id ?: -1
                    binding.acCategory.setText(cat.name, false)
                }
            }
        }

        //  after create a new category, auto-select the last real category
        if (newCategory && cats.isNotEmpty()) {
            val last = cats.last()
            storeCategoryId = last.id ?: -1
            binding.acCategory.setText(last.name, false)
        } else if (binding.acCategory.text.isNullOrBlank()) {
            binding.acCategory.setText(getString(R.string.no_category), false)
        }
    }

    private fun saveItem() {
        val name = binding.etName.text?.toString().orEmpty()
        val price = binding.etPrice.text?.toString()?.toDoubleOrNull() ?: 0.0
        val cost = binding.etCost.text?.toString()?.toDoubleOrNull() ?: 0.0
        val barcode = binding.etBarcode.text?.toString().orEmpty()

        // Default to white if no color chosen
        val color = viewModel.color.value.ifBlank { "#FFFFFF" }

        viewModel.submit(
            name = name,
            categoryId = storeCategoryId,
            price = price,
            cost = cost,
            barcode = barcode,
            color = color
        )
    }
}
