package com.example.device

import com.example.domain.BleRepository
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.scan.ScanFilter
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.Observable

class BleRepositoryImpl(private val rxBleClient: RxBleClient) : BleRepository {

    override fun scanDevices(
        settings: ScanSettings,
        scanFilter: ScanFilter
    ): Observable<ScanResult> = rxBleClient.scanBleDevices(settings, scanFilter)


}