package com.cec.launcher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cec.launcher.databinding.ItemAppBinding

class AppGridAdapter(
    private val onAppClick: (AppInfo) -> Unit
) : ListAdapter<AppInfo, AppGridAdapter.AppViewHolder>(AppDiff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppViewHolder {
        val binding = ItemAppBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AppViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AppViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AppViewHolder(private val binding: ItemAppBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(app: AppInfo) {
            binding.appIcon.setImageDrawable(app.icon)
            binding.appLabel.text = app.label
            binding.root.setOnClickListener { onAppClick(app) }
        }
    }

    object AppDiff : DiffUtil.ItemCallback<AppInfo>() {
        override fun areItemsTheSame(old: AppInfo, new: AppInfo) =
            old.packageName == new.packageName
        override fun areContentsTheSame(old: AppInfo, new: AppInfo) =
            old.label == new.label
    }
}
