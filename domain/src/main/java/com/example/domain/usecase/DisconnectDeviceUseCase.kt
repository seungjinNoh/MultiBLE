package com.example.domain.usecase

import com.example.domain.BleRepository
import com.polidea.rxandroidble2.RxBleDevice
import javax.inject.Inject

class DisconnectDeviceUseCase @Inject constructor(private val repository: BleRepository) {

    fun execute(device: RxBleDevice) = repository.disconnectBleDevice(device)

}