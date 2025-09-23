package com.team.bossku.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.bossku.data.model.Detail
import com.team.bossku.databinding.ItemLayoutDetailBinding

class DetailsAdapter(
    private val details: MutableList<Detail> = mutableListOf(),
    private val onClick: (Detail) -> Unit
) : RecyclerView.Adapter<DetailsAdapter.DetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val binding = ItemLayoutDetailBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return DetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(details[position])
    }

    override fun getItemCount(): Int = details.size

    fun setDetails(list: List<Detail>) {
        details.clear()
        details.addAll(list)
        notifyDataSetChanged()
    }

    fun getItemAt(position: Int): Detail = details[position]

    fun removeAt(position: Int): Detail {
        val removed = details.removeAt(position)
        notifyItemRemoved(position)
        return removed
    }

    fun insertAt(position: Int, item: Detail) {
        details.add(position, item)
        notifyItemInserted(position)
    }

    inner class DetailViewHolder(
        private val binding: ItemLayoutDetailBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(detail: Detail) {
            binding.tvName.text = detail.name
            binding.tvQty.text = detail.qty.toString()
            binding.tvTotal.text = detail.total.toString()
            binding.llDetail.setOnClickListener { onClick(detail) }
        }
    }
}
