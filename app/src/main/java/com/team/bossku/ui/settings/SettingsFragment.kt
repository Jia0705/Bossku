package com.team.bossku.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.team.bossku.MainActivity
import com.team.bossku.R
import com.team.bossku.databinding.FragmentSettingsBinding
import java.util.Locale

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.llManageItems.setOnClickListener {

        }

        binding.llManageCategories.setOnClickListener {

        }

        binding.llHistory.setOnClickListener {

        }

        // Language
        val languages = listOf("English", "Bahasa Melayu", "中文")
        val langTags  = listOf("en", "ms", "zh")

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
        val themes = listOf(
            getString(R.string.light),
            getString(R.string.dark)
        )

        val themeAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            themes
        )
        themeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spTheme.adapter = themeAdapter

        fun currentTheme(): Int =
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) 1 else 0

        binding.spTheme.setSelection(currentTheme(), false)

        binding.spTheme.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, v: View?, pos: Int, id: Long) {
                val themeIndex = currentTheme()
                if (pos == themeIndex) return     // do nothing

                val mode = if (pos == 1)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO

                AppCompatDelegate.setDefaultNightMode(mode)
                requireActivity().recreate()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }
}
