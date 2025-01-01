package com.explorer.discovery.local.datasource

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import com.explorer.discovery.data.datasource.ConsumerDatasource
import com.explorer.discovery.domain.model.Device
import com.explorer.discovery.local.factory.ConsumerFactory
import com.explorer.discovery.local.BuildConfig
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

internal class ConsumerDatasourceDelegateTest {
    private lateinit var filterBuilder: ScanFilter.Builder
    private lateinit var settingsBuilder: ScanSettings.Builder
    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var bluetoothLeScanner: BluetoothLeScanner
    private lateinit var listener: ConsumerDatasource.Listener
    private lateinit var adapter: ConsumerDatasource
    private lateinit var callback: ScanCallback

    @Before
    fun setup() {
        filterBuilder = mockk(relaxed = true)
        settingsBuilder = mockk(relaxed = true)
        bluetoothManager = mockk()
        listener = mockk()
        bluetoothAdapter = mockk<BluetoothAdapter>()
        bluetoothLeScanner = mockk<BluetoothLeScanner>()

        every { bluetoothManager.adapter } returns bluetoothAdapter
        every { bluetoothAdapter.bluetoothLeScanner } returns bluetoothLeScanner

        adapter = ConsumerDatasourceDelegate(
            object : ConsumerFactory {
                override fun settings(): ScanSettings.Builder = settingsBuilder

                override fun filter(): ScanFilter.Builder = filterBuilder
            },
            bluetoothManager,
            listener
        ).also { callback = it }
    }

    @Test
    fun `start() should begin scanning when not already started`() {
        val identifier = "0000180A-0000-1000-8000-00805F9B34FB"
        val scanSettings = mockk<ScanSettings>()
        val filters = listOf(mockk<ScanFilter>())

        every { settingsBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) } returns settingsBuilder
        every { settingsBuilder.build() } returns scanSettings
        every { filterBuilder.setServiceUuid(any()) } returns filterBuilder
        every { filterBuilder.build() } returns filters.first()
        every { bluetoothLeScanner.startScan(any(), any(), any<ScanCallback>()) } returns Unit

        adapter.start(identifier)

        verify { bluetoothLeScanner.startScan(filters, scanSettings, any<ScanCallback>()) }
    }

    @Test
    fun `onScanResult() should call onDiscover with correct device when manufacturer data is present`() {
        val address = "00:11:22:33:44:55"
        val mockResult = mockk<ScanResult>()
        val mockDevice = mockk<BluetoothDevice>()
        val manufacturerData = "Test Manufacturer"

        every { mockResult.device } returns mockDevice
        every { mockResult.scanRecord?.getManufacturerSpecificData(BuildConfig.MANUFACTURER) } returns manufacturerData.toByteArray()
        every { mockDevice.address } returns address
        every { listener.onDiscover(any()) } returns Unit

        callback.onScanResult(0, mockResult)

        verify { listener.onDiscover(Device(manufacturerData, address)) }
    }

    @Test
    fun `onScanResult() should not call onDiscover when manufacturer data is not present`() {
        val address = "00:11:22:33:44:55"
        val mockResult = mockk<ScanResult>()
        val mockDevice = mockk<BluetoothDevice>()

        every { mockResult.device } returns mockDevice
        every { mockResult.scanRecord?.getManufacturerSpecificData(BuildConfig.MANUFACTURER) } returns null
        every { mockDevice.address } returns address

        callback.onScanResult(0, mockResult)

        verify(exactly = 0) { listener.onDiscover(any()) }
    }

    @Test
    fun `stop() should stop scanning and mark as not started`() {
        every { bluetoothLeScanner.stopScan(any<ScanCallback>()) } returns Unit
        adapter.stop()
        verify { bluetoothLeScanner.stopScan(any<ScanCallback>()) }
    }
}
