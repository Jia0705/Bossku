package com.team.bossku.ui.ticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.team.bossku.databinding.FragmentTicketBinding
import com.team.bossku.ui.adapter.TicketsAdapter
import com.team.bossku.ui.popup.DeletePopFragment
import kotlinx.coroutines.launch

class TicketFragment : Fragment() {
    private lateinit var binding: FragmentTicketBinding
    private val viewModel: TicketViewModel by viewModels{
        TicketViewModel.Factory
    }
    private lateinit var adapter: TicketsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTicketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()

        lifecycleScope.launch {
            viewModel.tickets.collect { list ->
                adapter.setTickets(list)
                binding.llEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            }
        }
    }

    private fun setupAdapter() {
        adapter = TicketsAdapter(
            onClick = { ticket ->
                val action = TicketFragmentDirections.actionTicketFragmentToTicketDetailFragment(ticket.id!!)
                findNavController().navigate(action)
            },
            onLongClick = { ticket ->
                val dialog = DeletePopFragment()
                dialog.setListener(object : DeletePopFragment.Listener {
                    override fun onClickCancel() { }
                    override fun onClickDelete() {
                        val id = ticket.id ?: return
                        viewModel.deleteTicket(id)
                    }
                })
                dialog.show(parentFragmentManager, "confirm_delete_ticket")
            }
        )

        binding.ibBack.setOnClickListener { findNavController().popBackStack() }
        binding.rvTickets.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTickets.adapter = adapter
    }
}