package com.team.bossku.ui.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.team.bossku.databinding.FragmentHistoryBinding
import com.team.bossku.ui.adapter.TicketsAdapter
import com.team.bossku.ui.popup.DeletePopFragment
import com.team.bossku.ui.popup.SortPopFragment
import kotlinx.coroutines.launch

class HistoryFragment : Fragment() {
    private val viewModel: HistoryViewModel by viewModels{
        HistoryViewModel.Factory
    }
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: TicketsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TicketsAdapter(
            onClick = { ticket ->
                val id = ticket.id
                if (id != null) {
                    val action = HistoryFragmentDirections.actionHistoryFragmentToHistoryDetailFragment(id)
                    findNavController().navigate(action)
                }
            },
            onLongClick = { ticket ->
                val dialog = DeletePopFragment()
                dialog.setListener(object : DeletePopFragment.Listener {
                    override fun onClickCancel() { }
                    override fun onClickDelete() {
                        val id = ticket.id ?: return
                        viewModel.deleteHistoryTicket(id)
                    }
                })
                dialog.show(parentFragmentManager, "confirm_delete_history_ticket")
            }
        )

        binding.ibBack.setOnClickListener { findNavController().popBackStack() }
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHistory.adapter = adapter

        // Search
        binding.etSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.setSearch(text?.toString().orEmpty())
        }

        // Sort with SortPopFragment
        binding.ibSort.setOnClickListener {
            val dialog = SortPopFragment()
            dialog.setListener(object : SortPopFragment.Listener {
                override fun onClickDone() { }
                override fun onSort(isAscending: Boolean) {
                    viewModel.setSortAscending(isAscending)
                }
                override fun onSortBy(isName: Boolean) {
                    viewModel.setSortByName(isName)
                }
            })
            dialog.show(parentFragmentManager, "history_sort")
        }

        lifecycleScope.launch {
            viewModel.history.collect { list ->
                adapter.setTickets(list)
                binding.llEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }
}