package com.dicoding.sahabatgula.ui.navigation_ui.scan

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.sahabatgula.R
import com.dicoding.sahabatgula.data.remote.response.PredictionItem
import com.dicoding.sahabatgula.databinding.ItemScanBinding

class ScanAdapter : ListAdapter<PredictionItem, ScanAdapter.ScanViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val binding = ItemScanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ScanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        val item = getItem(position)
        Log.d("ScanAdapter", "Binding data at position $position: $item")
        holder.bind(item)

        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, DataScanActivity::class.java).apply {
                putExtra("name", item.name)
                putExtra("karbo", item.karbohidrat)
                putExtra("lemak", item.lemak)
                putExtra("gula", item.gula)
                putExtra("protein", item.protein)
            }
            context.startActivity(intent)
        }
    }

    class ScanViewHolder(binding: ItemScanBinding) : RecyclerView.ViewHolder(binding.root) {
        private val tvProductFound: TextView = binding.productFound
        private val tvPredictPercentage: TextView = binding.predictPercentage
        private val tvGulaTotalFound: TextView = binding.gulaTotalFound
        private val tvProteinTotalFound: TextView = binding.proteinTotalFound
        private val tvLemakTotalFound: TextView = binding.lemakTotalFound


        fun bind(result: PredictionItem) {
            tvProductFound.text = result.name
            tvPredictPercentage.text = result.prediction
            tvGulaTotalFound.text = result.gula.toString()
            tvProteinTotalFound.text = result.protein.toString()
            tvLemakTotalFound.text = result.lemak.toString()
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<PredictionItem>() {
        override fun areItemsTheSame(oldItem: PredictionItem, newItem: PredictionItem): Boolean {
            return oldItem.label == newItem.label
        }

        override fun areContentsTheSame(oldItem: PredictionItem, newItem: PredictionItem): Boolean {
            return oldItem == newItem
        }
    }
}
