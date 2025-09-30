package com.team.bossku.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.bossku.data.model.TicketDetail
import com.team.bossku.databinding.ItemLayoutTicketDetailBinding

class DetailsAdapter(
    private val ticketDetails: MutableList<TicketDetail> = mutableListOf(),
    private val onClick: (TicketDetail) -> Unit
) : RecyclerView.Adapter<DetailsAdapter.DetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val binding = ItemLayoutTicketDetailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(ticketDetails[position])
    }

    override fun getItemCount(): Int = ticketDetails.size

    fun setDetails(list: List<TicketDetail>) {
        ticketDetails.clear()
        ticketDetails.addAll(list)
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): TicketDetail = ticketDetails[position]

    fun removeAt(position: Int): TicketDetail {
        val removed = ticketDetails.removeAt(position)
        notifyItemRemoved(position)
        return removed
    }

    fun insertAt(position: Int, item: TicketDetail) {
        ticketDetails.add(position, item)
        notifyItemInserted(position)
    }

    inner class DetailViewHolder(
        private val binding: ItemLayoutTicketDetailBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(ticketDetail: TicketDetail) {
            binding.tvName.text = ticketDetail.name
            binding.tvQty.text = ticketDetail.qty.toString()
            binding.tvTotal.text = ticketDetail.total.toString()
            binding.llDetail.setOnClickListener { onClick(ticketDetail) }
        }
    }
}
