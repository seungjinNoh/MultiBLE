package com.example.domain


import com.polidea.rxandroidble2.scan.ScanFilter
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.*

interface BleRepository {

    fun scanDevices(settings: ScanSettings, scanFilter: ScanFilter): Observable<ScanResult>
}