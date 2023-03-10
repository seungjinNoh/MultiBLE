package com.example.domain.util

class DeviceEvent<out T> private constructor(val deviceAddress: String, val data: T) {

    companion object {
        fun <T> deviceConnectionEvent(deviceAddress: String, data: T) = DeviceEvent(deviceAddress, data)
    }

}