package com.team.bossku.ui.history

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.team.bossku.databinding.FragmentTicketDetailBinding
import com.team.bossku.ui.adapter.TicketsDetailsAdapter
import com.team.bossku.ui.ticket_detail.TicketDetailViewModel
import kotlinx.coroutines.launch

class HistoryDetailFragment : Fragment() {
    private lateinit var binding: FragmentTicketDetailBinding
    private val viewModel: TicketDetailViewModel by viewModels{
        TicketDetailViewModel.Factory
    }
    private lateinit var adapter: TicketsDetailsAdapter
    private val args: HistoryDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTicketDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = TicketsDetailsAdapter(
            onEditQty = { _, _ ->  }   // no operation in history detail
        )

        binding.ibBack.setOnClickListener { findNavController().popBackStack() }
        binding.rvDetails.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDetails.adapter = adapter

        lifecycleScope.launch {
            viewModel.ticket.collect { ticket ->
                if (ticket != null) {
                    binding.tvHeader.text = ticket.ticket.name
                    binding.tvTotal.text = String.format("RM %.2f", ticket.total)
                    adapter.setDetails(ticket.items)
                    binding.mbSave.visibility = View.GONE
                }
            }
        }

        viewModel.loadTicket(args.ticketId)
    }
}