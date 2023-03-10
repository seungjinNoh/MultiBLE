package com.example.device

import android.util.Log
import com.example.domain.BleRepository
import com.example.domain.util.DeviceEvent
import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.RxBleConnection
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.scan.ScanFilter
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class BleRepositoryImpl(private val rxBleClient: RxBleClient) : BleRepository {

    private var rxBleConnectionMap = hashMapOf<String, RxBleConnection>()
    private var conStateDisposableMap = hashMapOf<String, Disposable>()
    private var connectSubscriptionMap = hashMapOf<String, Disposable>()
    private var connectedDeviceMap = hashMapOf<String, RxBleDevice>()

    override var deviceConnectionEvent = MutableSharedFlow<DeviceEvent<Boolean>>()
    override var testEvent = MutableSharedFlow<String>()

    override fun scanDevices(
        settings: ScanSettings,
        scanFilter: ScanFilter
    ): Observable<ScanResult> = rxBleClient.scanBleDevices(settings, scanFilter)

    override fun connectBleDevice(device: RxBleDevice) {
        conStateDisposableMap[device.macAddress] = device.observeConnectionStateChanges()
            .subscribe (
                { connectionState ->
//                    CoroutineScope(Dispatchers.IO).launch {
//                        Log.d("BleRepositoryImpl", "test CoroutineScope 안")
//                        testEvent.emit("emit test")
//                    }
                    connectionStateListener(device, connectionState)
                }
            ) { throwable ->
                throwable.printStackTrace()
            }
        connectSubscriptionMap[device.macAddress] = device.establishConnection(false)
            .flatMapSingle { _rxBleConnection ->
                rxBleConnectionMap[device.macAddress] = _rxBleConnection
                _rxBleConnection.discoverServices()
            }.subscribe({
                //service
            }, {

            })

    }

    private fun connectionStateListener(
        device: RxBleDevice,
        connectionState: RxBleConnection.RxBleConnectionState
    ) {
        when(connectionState) {
            RxBleConnection.RxBleConnectionState.CONNECTED -> {
                Log.d("BleRepositoryImpl", "connectionStateListener() CONNECTED device: ${device.macAddress}, Thread: ${Thread.currentThread().name}")
                CoroutineScope(Dispatchers.IO).launch {
                    Log.d("BleRepositoryImpl", "CoroutineScope 안 ${device.macAddress}, Thread: ${Thread.currentThread().name}")
                    deviceConnectionEvent.emit(DeviceEvent.deviceConnectionEvent(device.macAddress, true))
                    connectedDeviceMap[device.macAddress] = device
                    Log.d("BleRepositoryImpl", "CoroutineScope 안 끝 connectDeviceMap.size: ${connectedDeviceMap.size}")
                }
//                runBlocking {
//                    Log.d("BleRepositoryImpl", "CoroutineScope 안 ${device.macAddress}, Thread: ${Thread.currentThread().name}")
//                    val temp = DeviceEvent.deviceConnectionEvent(device.macAddress, true)
//                    Log.d("BleRepositoryImpl", "temp: $temp")
//                    deviceConnectionEvent.emit(temp)
//                    connectedDeviceMap[device.macAddress] = device
//                    Log.d("BleRepositoryImpl", "CoroutineScope 안 끝 connectDeviceMap.size: ${connectedDeviceMap.size}")
//                }
            }
            RxBleConnection.RxBleConnectionState.DISCONNECTED -> {
                conStateDisposableMap[device.macAddress]?.dispose()
                Log.d("BleRepositoryImpl", "connectionStateListener() DISCONNECTED device: ${device.macAddress}")
                CoroutineScope(Dispatchers.IO).launch {
                    deviceConnectionEvent.emit(DeviceEvent.deviceConnectionEvent(device.macAddress, false))
                    connectedDeviceMap.remove(device.macAddress)
                    Log.d("BleRepositoryImpl", "Disconnected CoroutineScope 안 끝 connectDeviceMap.size: ${connectedDeviceMap.size}")
                }
            }
            RxBleConnection.RxBleConnectionState.CONNECTING -> {}
            RxBleConnection.RxBleConnectionState.DISCONNECTING -> {}
        }
    }

    override fun disconnectBleDevice(device: RxBleDevice) {
        connectSubscriptionMap[device.macAddress]?.dispose()
    }
}