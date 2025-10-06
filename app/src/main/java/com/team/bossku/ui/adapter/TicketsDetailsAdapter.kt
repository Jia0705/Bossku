package com.team.bossku.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.bossku.data.model.TicketDetail
import com.team.bossku.databinding.ItemLayoutTicketDetailBinding

class TicketsDetailsAdapter(
    private var details: List<TicketDetail> = emptyList(),
    private val onEditQty: (Int, TicketDetail) -> Unit,
    private val onLongPressDelete: ((Int, TicketDetail) -> Unit)? = null
) : RecyclerView.Adapter<TicketsDetailsAdapter.DetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val binding = ItemLayoutTicketDetailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(details[position], position)
    }

    override fun getItemCount(): Int = details.size

    fun setDetails(list: List<TicketDetail>) {
        details = list
        notifyDataSetChanged()
    }

    inner class DetailViewHolder(
        private val binding: ItemLayoutTicketDetailBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: TicketDetail, position: Int) {
            binding.tvName.text = detail.name
            binding.tvQty.text = detail.qty.toString()
            binding.tvTotal.text = "RM " + String.format("%.2f", detail.subtotal)

            // Click to edit quantity
            binding.llDetail.setOnClickListener {
                onEditQty(position, detail)
            }

            // Long-press to delete item
            binding.llDetail.setOnLongClickListener {
                if (onLongPressDelete == null) {
                    false
                } else {
                    onLongPressDelete(position, detail)
                    true
                }
            }
        }
    }
}
