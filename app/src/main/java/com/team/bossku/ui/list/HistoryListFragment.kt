package com.team.bossku.ui.list

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.team.bossku.R
import com.team.bossku.data.model.Ticket
import com.team.bossku.data.repo.TicketsRepo
import com.team.bossku.ui.adapter.TicketsAdapter
import com.team.bossku.ui.manage.base.BaseManageListFragment

class HistoryListFragment : BaseManageListFragment() {
    private lateinit var adapter: TicketsAdapter

    override fun title() = R.string.history
    override fun emptyText() = R.string.empty_history
    override fun showFab() = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = TicketsRepo.getInstance()

        viewModel.loadAll = {
            repo.getTickets().map { it as Any }
        }

        //    empty query -> show all
        //    else, match ticket id text OR any line item name
        viewModel.matches = { any, query ->
            val ticket = any as Ticket
            val q = query.trim().lowercase()
            if (q.isEmpty()) {
                true
            } else {
                val idText = (ticket.id ?: -1).toString().lowercase()
                val itemsMatch = ticket.items.any { it.name.lowercase().contains(q) }
                idText.contains(q) || itemsMatch
            }
        }

        // sort by id text
        viewModel.sortKey = { any ->
            val ticket = any as Ticket
            (ticket.id ?: -1).toString()
        }

        viewModel.refresh()
    }

    override fun onFabClicked() {}

    override fun listAdapter(): RecyclerView.Adapter<*>? {
        adapter = TicketsAdapter(emptyList()) { ticket ->
            val id = ticket.id
            if (id != null) {
                val action = HistoryListFragmentDirections.actionHistoryListFragmentToHistoryFragment(id)
                findNavController().navigate(action)
            }
        }
        return adapter
    }

    override fun submitList(data: List<Any>) {
        val tickets = data.map { it as Ticket }
        adapter.setTickets(tickets)
    }
}
