package com.example.domain.usecase

import com.example.domain.BleRepository
import com.polidea.rxandroidble2.RxBleDevice
import javax.inject.Inject

class ConnectDeviceUseCase @Inject constructor(private val repository: BleRepository)  {

    fun execute(device: RxBleDevice) = repository.connectBleDevice(device)

}