package com.example.domain.usecase


import com.example.domain.BleRepository
import com.polidea.rxandroidble2.scan.ScanFilter
import com.polidea.rxandroidble2.scan.ScanSettings
import javax.inject.Inject

class ScanDevicesUseCase @Inject constructor(private val repository: BleRepository){

    fun execute(scanSettings: ScanSettings, scanFilter: ScanFilter) = repository.scanDevices(scanSettings, scanFilter)

}