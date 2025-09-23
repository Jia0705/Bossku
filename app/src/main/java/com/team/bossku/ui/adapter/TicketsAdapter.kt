package com.team.bossku.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.bossku.data.model.Ticket
import com.team.bossku.databinding.ItemLayoutTicketBinding
import java.util.Date

class TicketsAdapter(
    private var tickets: List<Ticket> = emptyList(),
    private val onClick: (Ticket) -> Unit
) : RecyclerView.Adapter<TicketsAdapter.TicketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val binding = ItemLayoutTicketBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return TicketViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        holder.bind(tickets[position])
    }

    override fun getItemCount(): Int = tickets.size

    fun setTickets(list: List<Ticket>) {
        tickets = list
        notifyDataSetChanged()
    }

    inner class TicketViewHolder(
        private val binding: ItemLayoutTicketBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ticket: Ticket) {
            binding.tvTicketId.text = "Ticket ${ticket.id ?: "-"}"    // Ticket 1, Ticket 2
            binding.tvTime.text = Date(ticket.createdAt).toString()
            binding.tvTotal.text = ticket.total.toString()
            binding.clTicket.setOnClickListener { onClick(ticket) }
        }
    }
}
