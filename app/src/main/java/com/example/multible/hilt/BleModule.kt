package com.example.multible.hilt

import android.content.Context
import bleshadow.javax.inject.Singleton
import com.example.device.BleRepositoryImpl
import com.example.domain.BleRepository
import com.polidea.rxandroidble2.RxBleClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BleModule {

    @Provides
    @Singleton
    fun provideRxBleClient(@ApplicationContext context: Context): RxBleClient {
        return RxBleClient.create(context)
    }

    @Provides
    @Singleton
    fun provideBleRepository(rxBleClient: RxBleClient): BleRepository {
        return BleRepositoryImpl(rxBleClient)
    }

}