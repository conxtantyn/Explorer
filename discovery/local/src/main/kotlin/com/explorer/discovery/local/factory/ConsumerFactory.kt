package com.explorer.discovery.local.factory

import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import javax.inject.Inject

interface ConsumerFactory {
    fun settings(): ScanSettings.Builder
    
    fun filter(): ScanFilter.Builder

    class Delegate @Inject constructor() : ConsumerFactory {
        override fun settings(): ScanSettings.Builder {
            return ScanSettings.Builder()
        }

        override fun filter(): ScanFilter.Builder {
            return ScanFilter.Builder()
        }
    }
}
