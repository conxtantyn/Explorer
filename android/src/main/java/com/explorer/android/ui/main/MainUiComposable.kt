package com.explorer.android.ui.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.explorer.android.BuildConfig
import com.explorer.android.ui.permission.Permissions
import com.explorer.core.ui.theme.UiTheme
import com.explorer.discovery.ui.view.discovery.DiscoveryUiComposable
import java.util.UUID

@Composable
fun MainUiComposable(
    component: MainUi.Component,
    permissions: List<String>,
    onError: (deniedPermissions: List<String>) -> Unit,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val error by rememberUpdatedState(onError)
    val viewModel = viewModel(
        modelClass = MainUiViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state = viewModel.state.observeAsState()
    val identifier = if (BuildConfig.IS_ADVERTISER) {
        UUID.randomUUID().toString().take(10)
    } else {
        null
    }
    MainUiView {
        if (state.value == true) {
            DiscoveryUiComposable(identifier, component, viewModelStoreOwner)
        } else {
            Text(
                text = "Explorer",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
    Permissions(
        permissions = permissions,
        onGranted = { viewModel.initialize() },
        onError = error
    )
}

@Composable
fun MainUiView(content: @Composable () -> Unit) {
    UiTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            content()
        }
    }
}
