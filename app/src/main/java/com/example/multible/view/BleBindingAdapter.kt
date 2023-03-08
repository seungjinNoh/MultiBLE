package com.example.multible.view

import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableArrayMap
import androidx.recyclerview.widget.RecyclerView
import com.polidea.rxandroidble2.scan.ScanResult
import timber.log.Timber

object BleBindingAdapter {

    @JvmStatic
    @BindingAdapter("scanList")
    fun RecyclerView.bindScanList(scanList: ObservableArrayMap<String, ScanResult>) {
        Timber.d("scanList: ${scanList.size}")
        val scanAdapter = ScanAdapter()
        this.adapter = scanAdapter
        scanAdapter.submitList(scanList.values.toList())
    }
}