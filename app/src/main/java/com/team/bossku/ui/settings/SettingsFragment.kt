package com.team.bossku.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.team.bossku.MainActivity
import com.team.bossku.MyApp
import com.team.bossku.R
import com.team.bossku.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch
import java.util.Locale

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val app = requireActivity().application as MyApp

        binding.llManageItems.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_itemListFragment)
        }

        binding.llManageCategories.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_categoryListFragment)
        }

        binding.llHistory.setOnClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_historyFragment)
        }

        // Language
        val languages = listOf("English", "Bahasa Melayu", "中文")
        val langTags = listOf("en", "ms", "zh")

        val langAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            languages
        )
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spLanguage.adapter = langAdapter

        fun currentLang(): Int {
            val tag = AppCompatDelegate.getApplicationLocales().toLanguageTags().ifBlank { Locale.getDefault().language }
            return when {
                tag.startsWith("ms") -> 1
                tag.startsWith("zh") -> 2
                else -> 0
            }
        }

        binding.spLanguage.setSelection(currentLang(), false)

        binding.spLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, v: View?, pos: Int, id: Long) {
                val currentTag = AppCompatDelegate.getApplicationLocales().toLanguageTags().ifBlank { Locale.getDefault().language }

                val newTag = langTags[pos]
                if (currentTag.startsWith(newTag)) return     // do nothing
                (requireActivity() as MainActivity).changeLang(newTag) // restart activity
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Theme
        val themes = listOf(getString(R.string.light), getString(R.string.dark))
        val themeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            themes
        )
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spTheme.adapter = themeAdapter

        lifecycleScope.launch {
            app.theme.isDarkMode.collect { isDark ->
                val themeIndex = if (isDark) 1 else 0
                if (binding.spTheme.selectedItemPosition != themeIndex) {
                    binding.spTheme.setSelection(themeIndex, false)
                }
            }
        }

        binding.spTheme.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, v: View?, pos: Int, id: Long) {
                lifecycleScope.launch {
                    val enableDarkMode = pos == 1
                    app.theme.setDarkMode(enableDarkMode)
                    AppCompatDelegate.setDefaultNightMode(
                        if (enableDarkMode) AppCompatDelegate.MODE_NIGHT_YES
                        else AppCompatDelegate.MODE_NIGHT_NO
                    )
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) { }
        }
    }
}