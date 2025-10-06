package com.team.bossku.ui.manage.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team.bossku.R
import com.team.bossku.databinding.FragmentBaseManageListBinding
import kotlinx.coroutines.launch

open class BaseManageListFragment : Fragment() {

    protected lateinit var binding: FragmentBaseManageListBinding
    protected val viewModel = BaseManageListViewModel()

    // child fragments override all of these
    protected open fun title(): Int = R.string.header         // title
    protected open fun emptyText(): Int = R.string.empty      // empty view
    protected open fun showFab(): Boolean = true              // show FAB visible or not
    protected open fun onFabClicked() {}                      // FAB action
    protected open fun listAdapter(): RecyclerView.Adapter<*>? = null
    protected open fun submitList(data: List<Any>) {}         // pass data to adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseManageListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar
        binding.tvHeader.setText(title())
        binding.ibBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // RecyclerView
        binding.rvList.adapter = listAdapter()
        binding.rvList.layoutManager = LinearLayoutManager(requireContext())

        // Search
        binding.etSearch.addTextChangedListener { text ->
            viewModel.setSearch(text?.toString().orEmpty())
        }

        // Sort A to Z
        binding.ibSort.setOnClickListener {
            viewModel.setSort()
            val asc = viewModel.sortAscending.value
            Toast.makeText(
                requireContext(),
                if (asc) getString(R.string.asc) else getString(R.string.des),
                Toast.LENGTH_LONG
            ).show()
        }

        // FAB
        binding.fabAdd.isVisible = showFab()
        binding.fabAdd.setOnClickListener { onFabClicked() }

        lifecycleScope.launch {
            viewModel.list.collect { data ->
                val empty = data.isEmpty()
                binding.llEmpty.isVisible = empty
                binding.rvList.isVisible = !empty
                if (empty) binding.tvEmpty.setText(emptyText())
                submitList(data)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }
}
