package com.example.multible.view


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.multible.databinding.ItemScanBinding
import com.polidea.rxandroidble2.scan.ScanResult

class ScanAdapter : ListAdapter<ScanResult, ScanAdapter.ScanViewHolder>(diffUtil) {
    var scanList = listOf<ScanResult>()

    class ScanViewHolder(
        private val binding: ItemScanBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(scanResult: ScanResult) {
            binding.name.text = scanResult.bleDevice.name
            binding.address.text = scanResult.bleDevice.macAddress
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        return ScanViewHolder(
            ItemScanBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setList(list: List<ScanResult>) {
        this.scanList = list
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ScanResult>() {
            override fun areItemsTheSame(oldItem: ScanResult, newItem: ScanResult): Boolean {
                return oldItem.bleDevice.name == newItem.bleDevice.name
            }

            override fun areContentsTheSame(oldItem: ScanResult, newItem: ScanResult): Boolean {
                return oldItem.equals(newItem)
            }
        }
    }


}