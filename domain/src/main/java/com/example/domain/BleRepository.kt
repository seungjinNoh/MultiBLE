package com.example.domain


import com.example.domain.util.DeviceEvent
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.scan.ScanFilter
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.*
import kotlinx.coroutines.flow.MutableSharedFlow

interface BleRepository {

    var deviceConnectionEvent: MutableSharedFlow<DeviceEvent<Boolean>>
    var testEvent: MutableSharedFlow<String>

    fun scanDevices(settings: ScanSettings, scanFilter: ScanFilter): Observable<ScanResult>
    fun connectBleDevice(device: RxBleDevice)
    fun disconnectBleDevice(device: RxBleDevice)

}