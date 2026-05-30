package com.cec.launcher

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.cec.launcher.databinding.FragmentAppDrawerBinding

class AppDrawerFragment : Fragment() {

    private var _binding: FragmentAppDrawerBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AppGridAdapter
    private var allApps: List<AppInfo> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppDrawerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        loadApps()
        setupSearch()
    }

    private fun setupRecyclerView() {
        adapter = AppGridAdapter { appInfo ->
            val intent = requireContext().packageManager
                .getLaunchIntentForPackage(appInfo.packageName)
            intent?.let { startActivity(it) }
        }
        binding.appGrid.layoutManager = GridLayoutManager(requireContext(), 4)
        binding.appGrid.adapter = adapter
    }

    private fun loadApps() {
        val pm = requireContext().packageManager
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        allApps = pm.queryIntentActivities(intent, PackageManager.MATCH_ALL)
            .filter { it.activityInfo.packageName != requireContext().packageName }
            .map { ri ->
                AppInfo(
                    label = ri.loadLabel(pm).toString(),
                    packageName = ri.activityInfo.packageName,
                    icon = ri.loadIcon(pm)
                )
            }
            .sortedBy { it.label.lowercase() }

        adapter.submitList(allApps)
    }

    private fun setupSearch() {
        binding.searchField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, st: Int, c: Int, a: Int) {}
            override fun onTextChanged(s: CharSequence?, st: Int, b: Int, c: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s?.toString()?.lowercase() ?: ""
                val filtered = if (query.isEmpty()) allApps
                else allApps.filter { it.label.lowercase().contains(query) }
                adapter.submitList(filtered)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
