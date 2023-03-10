package com.example.domain.usecase

import com.example.domain.BleRepository
import javax.inject.Inject

class DeviceConnectionEventUseCase @Inject constructor(private val repository: BleRepository) {

    fun execute() = repository.deviceConnectionEvent

}