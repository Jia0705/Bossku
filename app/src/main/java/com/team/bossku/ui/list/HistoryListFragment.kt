package com.team.bossku.ui.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.team.bossku.MyApp
import com.team.bossku.R
import com.team.bossku.data.model.Ticket
import com.team.bossku.data.model.TicketDetail
import com.team.bossku.data.repo.TicketsRepo
import com.team.bossku.ui.adapter.TicketsAdapter
import com.team.bossku.ui.manage.base.BaseManageListFragment
import kotlinx.coroutines.launch

data class TicketWithItems(
    val ticket: Ticket,
    val items: List<TicketDetail>
)

class HistoryListFragment : BaseManageListFragment() {
    private lateinit var adapter: TicketsAdapter
    private lateinit var repo: TicketsRepo

    override fun title() = R.string.history
    override fun emptyText() = R.string.empty_history
    override fun showFab() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repo = (requireActivity().application as MyApp).ticketsRepo

        lifecycleScope.launch {
            repo.getTicketsWithDetails().collect { ticketsWithDetails ->
                val ticketsList: List<TicketWithItems> = ticketsWithDetails.map { twd ->
                    TicketWithItems(twd.ticket, twd.items)
                }
                viewModel.loadAll = { ticketsList.map { it as Any } }
                viewModel.refresh()
            }
        }

        viewModel.matches = { any, query ->
            val twi = any as TicketWithItems
            val q = query.trim().lowercase()
            if (q.isEmpty()) true
            else {
                val idText = (twi.ticket.id ?: -1).toString()
                val itemsMatch = twi.items.any { it.name.lowercase().contains(q) }
                idText.contains(q) || itemsMatch
            }
        }

        viewModel.sortKey = { any ->
            val twi = any as TicketWithItems
            (twi.ticket.paidAt ?: twi.ticket.createdAt).toString()
        }
    }

    override fun onFabClicked() { }

    override fun listAdapter(): RecyclerView.Adapter<*>? {
        adapter = TicketsAdapter(emptyList()) { ticket ->
            ticket.id?.let { id ->
                val action = HistoryListFragmentDirections.actionHistoryListFragmentToHistoryDetailFragment(id)
                findNavController().navigate(action)
            }
        }
        return adapter
    }

    override fun submitList(data: List<Any>) {
        val tickets = data.map { (it as TicketWithItems).ticket }
        adapter.setTickets(tickets)
    }
}