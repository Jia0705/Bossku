package com.team.bossku.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.team.bossku.data.model.Category
import com.team.bossku.databinding.ItemLayoutCategoryBinding

class CategoriesAdapter(
    private var categories: List<Category> = emptyList(),
    private val onClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemLayoutCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    fun setCategories(list: List<Category>) {
        categories = list
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(
        private val binding: ItemLayoutCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: Category) {
            binding.tvCategory.text = category.name
            binding.llCategory.setOnClickListener { onClick(category) }
        }
    }
}
