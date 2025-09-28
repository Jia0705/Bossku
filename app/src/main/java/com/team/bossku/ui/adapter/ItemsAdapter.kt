package com.team.bossku.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.bossku.data.model.Item
import com.team.bossku.databinding.ItemLayoutItemBinding
import androidx.core.graphics.toColorInt

class ItemsAdapter(
    private var items: List<Item> = emptyList(),
    private val onClick: (Item) -> Unit
) : RecyclerView.Adapter<ItemsAdapter.ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemLayoutItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setItems(list: List<Item>) {
        items = list
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(
        private val binding: ItemLayoutItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Item) {
            binding.tvName.text = item.name
            binding.llItem.setOnClickListener { onClick(item) }
            binding.cvItem.setCardBackgroundColor(item.color.toColorInt())
        }
    }
}
