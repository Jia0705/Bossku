package com.team.bossku.ui.list

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.team.bossku.R
import com.team.bossku.data.model.Item
import com.team.bossku.data.repo.ItemsRepo
import com.team.bossku.ui.adapter.ItemsAdapter
import com.team.bossku.ui.manage.base.BaseManageListFragment

class ItemListFragment : BaseManageListFragment() {
    private lateinit var adapter: ItemsAdapter

    override fun title() = R.string.items
    override fun emptyText() = R.string.empty_item
    override fun showFab() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = ItemsRepo.getInstance()

        viewModel.loadAll = { repo.getItems().map { it as Any } }

        viewModel.matches = { any, query ->
            val item = any as Item
            val q = query.trim().lowercase()
            if (q.isEmpty()) {
                true
            } else {
                item.name.lowercase().contains(q)
            }
        }

        viewModel.sortKey = { any -> (any as Item).name.lowercase() }

        viewModel.refresh()
    }

    override fun onFabClicked() {
        findNavController().navigate(R.id.action_itemListFragment_to_addItemFragment)
    }

    override fun listAdapter(): RecyclerView.Adapter<*>? {
        adapter = ItemsAdapter(emptyList()) { item ->
            val id = item.id
            if (id != null) {
                val action = ItemListFragmentDirections.actionItemListFragmentToEditItemFragment(id)
                findNavController().navigate(action)
            }
        }
        return adapter
    }

    override fun submitList(data: List<Any>) {
        val items = data.map { it as Item }
        adapter.setItems(items)
    }
}
