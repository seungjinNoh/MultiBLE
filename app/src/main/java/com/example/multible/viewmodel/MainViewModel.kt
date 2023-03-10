package com.example.multible.viewmodel

import androidx.databinding.ObservableArrayMap
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.ConnectDeviceUseCase
import com.example.domain.usecase.DeviceConnectionEventUseCase
import com.example.domain.usecase.DisconnectDeviceUseCase
import com.example.domain.usecase.ScanDevicesUseCase
import com.example.domain.usecase.TestUseCase
import com.example.domain.util.DeviceEvent
import com.example.multible.base.BaseViewModel
import com.polidea.rxandroidble2.RxBleDevice
import com.polidea.rxandroidble2.exceptions.BleScanException
import com.polidea.rxandroidble2.scan.ScanFilter
import com.polidea.rxandroidble2.scan.ScanResult
import com.polidea.rxandroidble2.scan.ScanSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import java.util.*
import kotlin.concurrent.schedule

@HiltViewModel
class MainViewModel @Inject constructor(
    private val scanDevicesUseCase: ScanDevicesUseCase,
    private val connectDeviceUseCase: ConnectDeviceUseCase,
    private val disconnectDeviceUseCase: DisconnectDeviceUseCase,
    deviceConnectionEventUseCase: DeviceConnectionEventUseCase,
    testUseCase: TestUseCase
) : BaseViewModel() {

    private var scanSubscription: Disposable? = null
    var scanResults = ObservableArrayMap<String, ScanResult>()
    val isScanning = ObservableBoolean(false)

    val deviceConnectionEvent: SharedFlow<DeviceEvent<Boolean>> =
        deviceConnectionEventUseCase.execute().asSharedFlow()

    val testEvent: SharedFlow<String> =
        testUseCase.execute().asSharedFlow()

    private val _eventFlow = MutableSharedFlow<Event>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun startScan() {
        Timber.d("*")
        val settings: ScanSettings = ScanSettings.Builder().build()
        val scanFilter: ScanFilter = ScanFilter.Builder().build()

        scanResults.clear()

        isScanning.set(true)

        scanSubscription =
            scanDevicesUseCase.execute(settings, scanFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({ scanResult ->
                    addScanResult(scanResult)
                }, { throwable ->
                    if (throwable is BleScanException) {
                        event(Event.BleScanException(throwable.reason))
                    } else {
                        Timber.d("Unknown error")
                    }

                })

        Timer("scan",false).schedule(5000L) { stopScan() }

    }

    fun stopScan() {
        Timber.d("*")
        scanSubscription?.dispose()
        isScanning.set(false)
//        if (scanResults.isEmpty()) {
//            event(Event.ScanListUpdate(scanResults))
//        }
    }

    fun connectDevice(device: RxBleDevice) = connectDeviceUseCase.execute(device)

    private fun addScanResult(result: ScanResult) {
//        Timber.d("result: $result")
        val device = result.bleDevice
        val deviceAddress = device.macAddress
        scanResults[deviceAddress] = result
//        Timber.d("scanResult.size: ${scanResults.size}")
    }



    private fun event(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    sealed class Event {
        data class BleScanException(val reason: Int) : Event()
        data class ScanListUpdate(val scanResults: HashMap<String, ScanResult>) : Event()
    }
}