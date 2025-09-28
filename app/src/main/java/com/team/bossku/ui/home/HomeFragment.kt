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
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
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

        // LONG CLICK to switch mode
        binding.toolbar.setOnLongClickListener {
            val switch = if (viewModel.mode.value == Mode.ITEMS) Mode.CATEGORIES else Mode.ITEMS
            viewModel.switchMode(switch)
            true
        }

        // Sort
        binding.ibSort.setOnClickListener {
            viewModel.setSort()
            val asc = viewModel.sortAscending.value
            Toast.makeText(
                requireContext(),
                if (asc) getString(R.string.asc) else getString(R.string.des),
                Toast.LENGTH_LONG
            ).show()
        }

        // Ticket
        binding.ibCart.setOnClickListener {
            findNavController().navigate(R.id.ticketFragment)
        }

        // Add item / Category
        binding.fabAdd.setOnClickListener {
            if (viewModel.mode.value == Mode.ITEMS) {
                findNavController().navigate(R.id.addItemFragment)
            } else {
                findNavController().navigate(R.id.addCategoryFragment)
            }
        }

        // Search
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

                if (mode == Mode.ITEMS) {
                    itemsAdapter.setItems(viewModel.rvItems.value)
                } else {
                    categoriesAdapter.setCategories(viewModel.rvCategories.value)
                }

                updateTexts()
                updateEmptyVisibility()
            }
        }

        lifecycleScope.launch {
            viewModel.rvItems.collect { list ->
                itemsAdapter.setItems(list)
                updateEmptyVisibility()
            }
        }

        lifecycleScope.launch {
            viewModel.rvCategories.collect { list ->
                categoriesAdapter.setCategories(list)
                updateEmptyVisibility()
            }
        }

        lifecycleScope.launch {
            viewModel.isEmpty.collect { updateEmptyVisibility() }
        }

        lifecycleScope.launch {
            viewModel.addToTicketItem.collect { id ->
                if (id != null) {
                    Toast.makeText(requireContext(), getString(R.string.added_ticket), Toast.LENGTH_LONG).show()
                    viewModel.clearAddToTicketEvent()
                }
            }
        }
    }

    private fun setupAdapter() {
        itemsAdapter = ItemsAdapter(emptyList()) { item ->
            item.id?.let { viewModel.addItemToTicket(it) }
        }

        categoriesAdapter = CategoriesAdapter(emptyList()) { cat ->
            Toast.makeText(requireContext(), cat.name, Toast.LENGTH_LONG).show()
        }

        val layoutManager = GridLayoutManager(requireContext(), 2)
        binding.run {
            rvItems.adapter = itemsAdapter
            rvItems.layoutManager = layoutManager
        }

        // Swap grid
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
        updateTexts()
        updateEmptyVisibility()
    }

    // Update text in different mode
    private fun updateTexts() {
        val itemsMode = viewModel.mode.value == Mode.ITEMS
        // header
        binding.tvHeader.text = if (itemsMode) getString(R.string.items) else getString(R.string.categories)
        // add button
        binding.fabAdd.text = if (itemsMode) getString(R.string.add_new_item) else getString(R.string.add_new_category)
        // empty view
        binding.tvEmpty.text = if (itemsMode) getString(R.string.empty_item) else getString(R.string.empty_category)
    }

    private fun updateEmptyVisibility() {
        val empty = viewModel.isEmpty.value
        binding.llEmpty.visibility = if (empty) View.VISIBLE else View.GONE
        binding.rvItems.visibility = if (empty) View.GONE else View.VISIBLE
    }
}
