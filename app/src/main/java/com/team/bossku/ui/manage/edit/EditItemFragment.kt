package com.team.bossku.ui.manage.edit

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.team.bossku.MyApp
import com.team.bossku.R
import com.team.bossku.data.model.Category
import com.team.bossku.ui.manage.base.BaseManageItemFragment
import com.team.bossku.ui.popup.DeletePopFragment
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class EditItemFragment : BaseManageItemFragment() {
    override val viewModel: EditItemViewModel by viewModels{
        EditItemViewModel.Factory
    }
    private val args: EditItemFragmentArgs by navArgs()
    private var storeCategoryId: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvHeader.setText(R.string.edit_item)
        binding.ibBack.setOnClickListener { findNavController().popBackStack() }
        binding.mbSave.setOnClickListener { saveItem() }
        binding.mbDelete.visibility = View.VISIBLE
        binding.mbDelete.setOnClickListener {
            val dialog = DeletePopFragment()
            dialog.setListener(object : DeletePopFragment.Listener {
                override fun onClickCancel() { }
                override fun onClickDelete() { viewModel.deleteItem() }
            })
            dialog.show(parentFragmentManager, "confirm_delete_item")
        }

        val app = requireActivity().application as MyApp

        lifecycleScope.launch {
            viewModel.loadItemById(args.itemId)
            viewModel.item.collect { selectedItem ->
                selectedItem?.let { item ->
                    binding.etName.setText(item.name)
                    binding.etPrice.setText(item.price.toString())
                    binding.etCost.setText(item.cost.toString())
                    binding.etBarcode.setText(item.barcode.orEmpty())
                    viewModel.color.value = item.color

                    val categories = app.categoriesRepo.getCategories().firstOrNull() ?: emptyList()
                    val cat = categories.find { it.id == item.categoryId }
                    if (cat != null) {
                        storeCategoryId = cat.id
                        binding.acCategory.setText(cat.name, false)
                    } else {
                        storeCategoryId = null
                        binding.acCategory.setText(getString(R.string.no_category), false)
                    }
                }
            }
        }

        val repo = app.categoriesRepo
        lifecycleScope.launch {
            repo.getCategories().collect { cats ->
                setupCategoryDropdown(cats)
            }
        }

        // Colors
        binding.c1.setOnClickListener { viewModel.color.value = "#FFFF0000" }
        binding.c2.setOnClickListener { viewModel.color.value = "#FFFFA500" }
        binding.c3.setOnClickListener { viewModel.color.value = "#FFFFFF00" }
        binding.c4.setOnClickListener { viewModel.color.value = "#FF00FF00" }
        binding.c5.setOnClickListener { viewModel.color.value = "#FF0000FF" }

        //  When back from Add Category -> rebuild and select the new one
        setFragmentResultListener("manage_category") { _, _ ->
            lifecycleScope.launch {
                repo.getCategories().collect { cats ->
                    setupCategoryDropdown(cats, newCategory = true)
                }
            }
        }
    }

    private fun setupCategoryDropdown(cats: List<Category>, newCategory: Boolean = false) {
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
            (binding.acCategory.adapter as ArrayAdapter<*>).filter.filter(null)
            binding.acCategory.showDropDown()
        }

        binding.acCategory.setOnItemClickListener { _, _, pos, _ ->
            when (pos) {
                0 -> { // No category
                    storeCategoryId = null
                    binding.acCategory.setText(getString(R.string.no_category), false)
                }
                display.lastIndex -> { // Add new category
                    storeCategoryId = null
                    binding.acCategory.setText(getString(R.string.no_category), false)
                    findNavController().navigate(R.id.addCategoryFragment)
                }
                else -> {
                    val cat = cats[pos - 1]
                    storeCategoryId = cat.id
                    binding.acCategory.setText(cat.name, false)
                }
            }
        }

        if (newCategory && cats.isNotEmpty()) {
            val last = cats.last()
            storeCategoryId = last.id
            binding.acCategory.setText(last.name, false)
        }

        if (storeCategoryId != null && cats.none { it.id == storeCategoryId }) {
            storeCategoryId = null
            binding.acCategory.setText(getString(R.string.no_category), false)
        }

        if (binding.acCategory.text.isNullOrBlank()) {
            binding.acCategory.setText(getString(R.string.no_category), false)
        }
    }

    private fun saveItem() {
        val name = binding.etName.text?.toString().orEmpty()
        val price = binding.etPrice.text?.toString()?.toDoubleOrNull() ?: 0.0
        val cost = binding.etCost.text?.toString()?.toDoubleOrNull() ?: 0.0
        val barcode = binding.etBarcode.text?.toString().orEmpty()
        val color = viewModel.color.value.ifBlank { "#FFFFFF" }

        viewModel.submit(
            name = name,
            categoryId = storeCategoryId ?: -1,
            price = price,
            cost = cost,
            barcode = barcode,
            color = color
        )
    }
}