package com.explorer.discovery.ui.view.consumer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.explorer.core.ui.base.UiBuilder
import com.explorer.core.ui.extention.builder
import com.explorer.discovery.ui.model.UiDevice
import com.explorer.discovery.ui.viewmodel.ConsumerViewModel

@Composable
fun ConsumerUiComposable(
    factory: UiBuilder.FactoryProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember {
        factory.builder(ConsumerUiBuilder::class.java).build(context)
    }
    ConsumerUiPage(component, viewModelStoreOwner)
}

@Composable
fun ConsumerUiPage(
    component: ConsumerUi.Component,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val viewModel = viewModel(
        modelClass = ConsumerViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.observeAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = "Nearby Devices",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        ConsumerUiController(
            loading = state?.loading == true,
            onStart = { viewModel.start() },
            onStop = { viewModel.stop() }
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (state?.loading == true) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(16.dp)
            )
        }
        state?.error?.let { error ->
            Text(
                text = "Error: ${error.message}",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
        state?.devices?.run { ConsumerUiDevices(this) }
    }
}

@Composable
fun ConsumerUiController(
    loading: Boolean,
    onStart: () -> Unit,
    onStop: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onStart,
            enabled = !loading
        ) { Text(text = "Start") }
        Button(
            onClick = onStop,
            enabled = loading
        ) { Text(text = "Stop") }
    }
}

@Composable
fun ConsumerUiDevice(device: UiDevice) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Name: ${device.name}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Address: ${device.address}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun ConsumerUiDevices(devices: List<UiDevice>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(devices.size) { device ->
            ConsumerUiDevice(device = devices.get(device))
        }
    }
}
