package com.explorer.discovery.local.factory

import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import javax.inject.Inject

interface AdvertiserFactory {
    fun settings(): AdvertiseSettings.Builder

    fun data(): AdvertiseData.Builder

    class Delegate @Inject constructor() : AdvertiserFactory {
        override fun settings(): AdvertiseSettings.Builder {
            return AdvertiseSettings.Builder()
        }

        override fun data(): AdvertiseData.Builder {
            return AdvertiseData.Builder()
        }
    }
}
