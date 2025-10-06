package com.team.bossku.ui.list

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.team.bossku.R
import com.team.bossku.data.model.Category
import com.team.bossku.data.repo.CategoriesRepo
import com.team.bossku.ui.adapter.CategoriesAdapter
import com.team.bossku.ui.manage.base.BaseManageListFragment

class CategoryListFragment : BaseManageListFragment() {
    private lateinit var adapter: CategoriesAdapter

    override fun title() = R.string.categories
    override fun emptyText() = R.string.empty_category
    override fun showFab() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = CategoriesRepo.getInstance()

        // provide data to BaseViewModel
        viewModel.loadAll = {
            repo.getCategories().map { it as Any }
        }

        // search by name
        viewModel.matches = { any, query ->
            val category = any as Category
            val q = query.trim().lowercase()
            if (q.isEmpty()) {
                true
            } else {
                category.name.lowercase().contains(q)
            }
        }

        // sort A to Z
        viewModel.sortKey = { any ->
            (any as Category).name.lowercase()
        }

        viewModel.refresh()
    }

    override fun onFabClicked() {
        findNavController().navigate(R.id.action_categoryListFragment_to_addCategoryFragment)
    }

    override fun listAdapter(): RecyclerView.Adapter<*>? {
        adapter = CategoriesAdapter(emptyList()) { category ->
            val id = category.id
            if (id != null) {
                val action = CategoryListFragmentDirections.actionCategoryListFragmentToEditCategoryFragment(id)
                findNavController().navigate(action)
            }
        }
        return adapter
    }

    override fun submitList(data: List<Any>) {
        val categories = data.map { it as Category }
        adapter.setCategories(categories)
    }
}
