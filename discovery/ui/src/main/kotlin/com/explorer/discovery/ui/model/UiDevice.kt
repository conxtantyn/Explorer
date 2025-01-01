package com.explorer.discovery.ui.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UiDevice(
    val name: String,
    val address: String
) : Parcelable
