package com.team.bossku.ui.manage.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.team.bossku.R
import com.team.bossku.databinding.FragmentBaseManageCategoryBinding
import kotlinx.coroutines.launch

abstract class BaseManageCategoryFragment : Fragment() {
    protected abstract val viewModel: BaseManageCategoryViewModel
    protected lateinit var binding: FragmentBaseManageCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseManageCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.finish.collect {
                setFragmentResult("manage_category", Bundle())

                val nav = findNavController()
                val home = nav.popBackStack(R.id.homeFragment, false)
                if (!home) {
                    nav.popBackStack()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.error.collect {
                showError(it)
            }
        }
    }

    fun showError(msg: String) {
        val snackbar = Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(
            ContextCompat.getColor(requireContext(), R.color.red)
        )
        snackbar.show()
    }
}
