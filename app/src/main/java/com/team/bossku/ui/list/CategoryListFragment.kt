package com.team.bossku.ui.list

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.team.bossku.MyApp
import com.team.bossku.R
import com.team.bossku.data.model.Category
import com.team.bossku.data.repo.CategoriesRepo
import com.team.bossku.ui.adapter.CategoriesAdapter
import com.team.bossku.ui.manage.base.BaseManageListFragment
import kotlinx.coroutines.launch

class CategoryListFragment : BaseManageListFragment() {
    private lateinit var adapter: CategoriesAdapter
    private lateinit var repo: CategoriesRepo

    override fun title() = R.string.categories
    override fun emptyText() = R.string.empty_category
    override fun showFab() = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repo = (requireActivity().application as MyApp).categoriesRepo

        lifecycleScope.launch {
            repo.getCategories().collect { categories ->
                viewModel.loadAll = { categories.map { it as Any } }
                viewModel.refresh()
            }
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
    }

    override fun onFabClicked() {
        findNavController().navigate(R.id.action_categoryListFragment_to_addCategoryFragment)
    }

    override fun listAdapter(): RecyclerView.Adapter<*>? {
        adapter = CategoriesAdapter(emptyList()) { category ->
            category.id?.let { id ->
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