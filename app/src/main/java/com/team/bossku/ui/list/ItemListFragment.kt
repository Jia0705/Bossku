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
import androidx.lifecycle.lifecycleScope
import com.team.bossku.MyApp
import kotlinx.coroutines.launch

class ItemListFragment : BaseManageListFragment() {
    private lateinit var adapter: ItemsAdapter
    private lateinit var repo: ItemsRepo

    override fun title() = R.string.items
    override fun emptyText() = R.string.empty_item
    override fun showFab() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repo = (requireActivity().application as MyApp).itemsRepo

        lifecycleScope.launch {
            repo.getItems().collect { itemsList ->
                viewModel.loadAll = { itemsList.map { it as Any } }
                viewModel.refresh()
            }
        }

        viewModel.matches = { any, query ->
            val item = any as Item
            val q = query.trim().lowercase()
            if (q.isEmpty()) true
            else item.name.lowercase().contains(q)
        }

        viewModel.sortKey = { any -> (any as Item).name.lowercase() }
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