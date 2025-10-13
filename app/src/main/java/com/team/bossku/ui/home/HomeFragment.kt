package com.team.bossku.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.team.bossku.R
import com.team.bossku.databinding.FragmentHomeBinding
import com.team.bossku.ui.adapter.CategoriesAdapter
import com.team.bossku.ui.adapter.ItemsAdapter
import com.team.bossku.ui.popup.SavePopFragment
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels{
        HomeViewModel.Factory
    }
    private lateinit var itemsAdapter: ItemsAdapter
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()

        binding.toolbar.setOnLongClickListener {
            val switch = if (viewModel.mode.value == Mode.ITEMS) Mode.CATEGORIES else Mode.ITEMS
            viewModel.switchMode(switch)
            true
        }

        binding.ibSort.setOnClickListener {
            viewModel.setSort()
            Toast.makeText(
                requireContext(),
                if (viewModel.sortAscending.value) getString(R.string.asc) else getString(R.string.des),
                Toast.LENGTH_LONG
            ).show()
        }

        binding.ibCart.setOnClickListener {
            lifecycleScope.launch {
                if (!viewModel.isCartEmpty()) {
                    showSaveTicketDialog()
                } else {
                    findNavController().navigate(R.id.ticketFragment)
                }
            }
        }

        binding.fabAdd.setOnClickListener {
            if (viewModel.mode.value == Mode.ITEMS) {
                findNavController().navigate(R.id.addItemFragment)
            } else {
                findNavController().navigate(R.id.addCategoryFragment)
            }
        }

        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.setSearch(text?.toString().orEmpty())
        }

        setFragmentResultListener("manage_item") { _, result ->
            if (result.getBoolean("refresh")) viewModel.refresh()
        }

        setFragmentResultListener("manage_category") { _, result ->
            if (result.getBoolean("refresh")) viewModel.refresh()
        }

        lifecycleScope.launch {
            viewModel.mode.collect { mode ->
                binding.rvItems.adapter = if (mode == Mode.ITEMS) itemsAdapter else categoriesAdapter
                updateEmptyVisibility()
                updateTexts()
            }
        }

        lifecycleScope.launch {
            viewModel.rvItems.collect {
                itemsAdapter.setItems(it);
                updateEmptyVisibility()
            }
        }

        lifecycleScope.launch {
            viewModel.rvCategories.collect {
                categoriesAdapter.setCategories(it);
                updateEmptyVisibility()
            }
        }

        lifecycleScope.launch {
            viewModel.isEmpty.collect {
                updateEmptyVisibility()
            }
        }

        lifecycleScope.launch {
            viewModel.addToTicketItem.collect { id ->
                if (id != null) {
                    Toast.makeText(requireContext(), getString(R.string.added_ticket), Toast.LENGTH_LONG).show()
                    viewModel.clearAddToTicketEvent()
                }
            }
        }
        updateTexts()
        updateEmptyVisibility()
    }

    private fun setupAdapter() {
        itemsAdapter = ItemsAdapter(emptyList()) { item ->
            lifecycleScope.launch {
                item.id?.let {
                    viewModel.addItemToCart(it) }
            }
        }

        categoriesAdapter = CategoriesAdapter(emptyList()) { cat ->
            viewModel.selectCategory(cat.id)
        }

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.run {
            rvItems.adapter = itemsAdapter
            rvItems.layoutManager = layoutManager
        }

        val swap = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT,
            0
        ) {
            override fun onMove(
                rv: RecyclerView,
                vh: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val from = vh.bindingAdapterPosition
                val to = target.bindingAdapterPosition
                viewModel.move(from, to)
                rv.adapter?.notifyItemMoved(from, to)
                return true
            }
            override fun onSwiped(vh: RecyclerView.ViewHolder, direction: Int) {  }
            override fun isLongPressDragEnabled() = true
        })
        swap.attachToRecyclerView(binding.rvItems)
    }

    private fun updateTexts() {
        val itemsMode = viewModel.mode.value == Mode.ITEMS
        val selectedCatName = viewModel.getSelectedCategoryName()

        binding.tvHeader.text = if (itemsMode) {
            selectedCatName ?: getString(R.string.items)
        } else {
            getString(R.string.categories)
        }
        binding.fabAdd.text = if (itemsMode) getString(R.string.add_new_item) else getString(R.string.add_new_category)
        binding.tvEmpty.text = if (itemsMode) getString(R.string.empty_item) else getString(R.string.empty_category)
    }

    private fun updateEmptyVisibility() {
        val empty = viewModel.isEmpty.value
        binding.llEmpty.visibility = if (empty) View.VISIBLE else View.GONE
        binding.rvItems.visibility = if (empty) View.GONE else View.VISIBLE
    }

    private fun showSaveTicketDialog() {
        val dialog = SavePopFragment()
        dialog.setListener(object : SavePopFragment.Listener {
            override fun onClickSave(name: String) {
                lifecycleScope.launch {
                    val saved = viewModel.saveCartAsTicket(name)
                    if (saved) {
                        dialog.dismiss()
                        findNavController().navigate(R.id.ticketFragment)
                    } else {
                        Toast.makeText(requireContext(), getString(R.string.empty), Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
        dialog.show(parentFragmentManager, "save_ticket")
    }
}