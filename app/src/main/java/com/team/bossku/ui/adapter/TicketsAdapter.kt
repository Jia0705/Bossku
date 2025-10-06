package com.team.bossku.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.bossku.data.model.Ticket
import com.team.bossku.data.model.TicketStatus
import com.team.bossku.databinding.ItemLayoutTicketBinding
import java.text.DateFormat
import java.util.Date

class TicketsAdapter(
    private var tickets: List<Ticket> = emptyList(),
    private val onLongClick: ((Ticket) -> Unit)? = null,
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
            binding.tvTicketId.text = ticket.name
            binding.tvTime.text = if (ticket.status == TicketStatus.PAID && ticket.paidAt != null) {
                DateFormat.getDateTimeInstance().format(Date(ticket.paidAt))
            } else {
                DateFormat.getDateTimeInstance().format(Date(ticket.createdAt))
            }
            binding.tvTotal.text = "RM " + String.format("%.2f", ticket.total)
            binding.clTicket.setOnClickListener { onClick(ticket) }

            binding.clTicket.setOnLongClickListener {
                if (onLongClick != null) {
                    onLongClick(ticket)
                    true
                } else {
                    false
                }
            }
        }
    }
}
