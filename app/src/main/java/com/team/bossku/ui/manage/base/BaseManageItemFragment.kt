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
import com.team.bossku.databinding.FragmentBaseManageItemBinding
import kotlinx.coroutines.launch

abstract class BaseManageItemFragment : Fragment() {
    protected abstract val viewModel: BaseManageItemViewModel
    protected lateinit var binding: FragmentBaseManageItemBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentBaseManageItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.finish.collect {
                setFragmentResult("manage_item", Bundle().apply { putBoolean("refresh", true) })

                val nav = findNavController()
                val home = nav.popBackStack()
                if (!home) {
                    nav.navigate(R.id.homeFragment)
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

    protected fun updateColorBorder(selectedColor: String) {
        // Reset all borders to unselected
        binding.border1.setBackgroundResource(R.drawable.color_selector_unselected)
        binding.border2.setBackgroundResource(R.drawable.color_selector_unselected)
        binding.border3.setBackgroundResource(R.drawable.color_selector_unselected)
        binding.border4.setBackgroundResource(R.drawable.color_selector_unselected)
        binding.border5.setBackgroundResource(R.drawable.color_selector_unselected)

        // Set selected border
        when (selectedColor.uppercase()) {
            "#FFFF0000" -> binding.border1.setBackgroundResource(R.drawable.color_selector_selected)
            "#FFFFA500" -> binding.border2.setBackgroundResource(R.drawable.color_selector_selected)
            "#FFFFFF00" -> binding.border3.setBackgroundResource(R.drawable.color_selector_selected)
            "#FF00FF00" -> binding.border4.setBackgroundResource(R.drawable.color_selector_selected)
            "#FF0000FF" -> binding.border5.setBackgroundResource(R.drawable.color_selector_selected)
        }
    }

    protected fun setupColorListeners() {
        binding.c1.setOnClickListener { 
            viewModel.color.value = "#FFFF0000"
            updateColorBorder("#FFFF0000")
        }
        binding.c2.setOnClickListener { 
            viewModel.color.value = "#FFFFA500"
            updateColorBorder("#FFFFA500")
        }
        binding.c3.setOnClickListener { 
            viewModel.color.value = "#FFFFFF00"
            updateColorBorder("#FFFFFF00")
        }
        binding.c4.setOnClickListener { 
            viewModel.color.value = "#FF00FF00"
            updateColorBorder("#FF00FF00")
        }
        binding.c5.setOnClickListener { 
            viewModel.color.value = "#FF0000FF"
            updateColorBorder("#FF0000FF")
        }
        binding.btnResetColor.setOnClickListener { 
            viewModel.color.value = "#FFFFFF"
            updateColorBorder("#FFFFFF")
        }
    }
}
