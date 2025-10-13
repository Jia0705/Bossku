package com.team.bossku.ui.ticket_detail

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.team.bossku.R
import com.team.bossku.data.model.TicketStatus
import com.team.bossku.databinding.FragmentTicketDetailBinding
import com.team.bossku.ui.adapter.TicketsDetailsAdapter
import com.team.bossku.ui.popup.EditPopFragment
import kotlinx.coroutines.launch

class TicketDetailFragment : Fragment() {

    private lateinit var binding: FragmentTicketDetailBinding
    private val viewModel: TicketDetailViewModel by viewModels{
        TicketDetailViewModel.Factory
    }
    private val args: TicketDetailFragmentArgs by navArgs()
    private lateinit var adapter: TicketsDetailsAdapter
    private var backTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTicketDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()

        lifecycleScope.launch {
            viewModel.ticket.collect { ticket ->
                if (ticket != null) {
                    binding.tvHeader.text = ticket.ticket.name
                    binding.tvTotal.text = "RM %.2f".format(ticket.total)
                    adapter.setDetails(ticket.items)

                    if (ticket.ticket.status == TicketStatus.PAID) {
                        binding.mbSave.text = getString(R.string.paid)
                        binding.mbSave.isEnabled = false
                    } else {
                        binding.mbSave.text = getString(R.string.pay)
                        binding.mbSave.isEnabled = true
                    }
                }
            }
        }

        binding.mbSave.setOnClickListener {
            viewModel.markAsPaid()

            backTimer?.cancel()
            backTimer = object : CountDownTimer(10_000, 1_000) {
                override fun onTick(millisUntilFinished: Long) { }
                override fun onFinish() {
                    if (isAdded) {
                        findNavController().popBackStack()
                    }
                }
            }.start()
        }
        viewModel.loadTicket(args.ticketId)
    }

    private fun setupAdapter() {
        adapter = TicketsDetailsAdapter(
            emptyList(),
            { position, detail ->
                val currentTicket = viewModel.ticket.value
                if (currentTicket != null && currentTicket.ticket.status != TicketStatus.PAID) {
                    val dialog = EditPopFragment(oldQty = detail.qty)
                    dialog.setListener(object : EditPopFragment.Listener {
                        override fun onClickSave(newQty: Int) {
                            viewModel.updateQuantity(position, newQty)
                        }
                    })
                    dialog.show(parentFragmentManager, "edit_qty")
                }
            },
            { position, _ ->
                val currentTicket = viewModel.ticket.value
                if (currentTicket != null && currentTicket.ticket.status != TicketStatus.PAID) {
                    viewModel.removeItem(position)
                }
            }
        )

        binding.ibBack.setOnClickListener { findNavController().popBackStack() }
        binding.rvDetails.layoutManager = LinearLayoutManager(requireContext())
        binding.rvDetails.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        backTimer?.cancel()
    }
}