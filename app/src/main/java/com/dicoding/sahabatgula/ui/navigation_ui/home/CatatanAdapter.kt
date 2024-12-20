package com.dicoding.sahabatgula.ui.navigation_ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.sahabatgula.databinding.ItemCatatanBinding
import com.dicoding.sahabatgula.helper.ScanData

class CatatanAdapter : ListAdapter<ScanData, CatatanAdapter.CatatanViewHolder>(CatatanDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatatanViewHolder {
        val binding = ItemCatatanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CatatanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CatatanViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class CatatanViewHolder(private val binding: ItemCatatanBinding) : RecyclerView.ViewHolder(binding.root) {

        var _kaloriJumlah = 0.0

        fun bind(catatan: ScanData) {
            binding.apply {
                productFoundCatatan.text = catatan.name
                gulaTotalFound.text = catatan.gula.toString()
                lemakTotalFound.text = catatan.lemak.toString()
                karboTotalFound.text = catatan.karbo.toString()
                proteinTotalFound.text = catatan.protein.toString()
                _kaloriJumlah = ((catatan.karbo).toDouble())*4.0 + ((catatan.protein).toDouble())*4.0 + (catatan.lemak)*9.0
                Log.d("KALORI", "$_kaloriJumlah")
                kaloriJumlah.text =_kaloriJumlah.toInt().toString()
            }
        }
    }

    class CatatanDiffCallback : DiffUtil.ItemCallback<ScanData>() {
        override fun areItemsTheSame(oldItem: ScanData, newItem: ScanData): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: ScanData, newItem: ScanData): Boolean {
            return oldItem == newItem
        }
    }
}
