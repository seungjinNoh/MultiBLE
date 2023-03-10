package com.example.domain.usecase

import com.example.domain.BleRepository
import javax.inject.Inject

class TestUseCase @Inject constructor(private val repository: BleRepository) {

    fun execute() = repository.testEvent

}