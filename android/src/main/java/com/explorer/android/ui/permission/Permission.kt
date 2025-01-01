package com.explorer.android.ui.permission

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun Permissions(
    permissions: List<String>,
    onGranted: () -> Unit,
    onError: (deniedPermissions: List<String>) -> Unit
) {
    if (permissions.isEmpty()) {
        onGranted()
        return
    }
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        val deniedPermissions = result.filterValues { !it }.keys.toList()
        if (deniedPermissions.isEmpty()) {
            onGranted()
        } else {
            onError(deniedPermissions)
        }
    }
    val unGrantedPermissions = context.grantedPermissions(permissions)
    var requirePermission by remember { mutableStateOf(unGrantedPermissions.isNotEmpty()) }
    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                if (unGrantedPermissions.isNotEmpty() && requirePermission) {
                    requirePermission = false
                    permissionLauncher.launch(unGrantedPermissions.toTypedArray())
                } else if (unGrantedPermissions.isEmpty()) {
                    onGranted()
                } else {
                    requirePermission = true
                }
            }
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }
}

private fun Context.grantedPermissions(permissions: List<String>): List<String> {
    return permissions.filter { permission ->
        permission.isNotEmpty() && checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED
    }
}
