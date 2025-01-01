package com.explorer.android.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

@MainUi.Scope
class MainUiViewModel @Inject constructor() : ViewModel() {
    private val mutableState = MutableLiveData(false)

    var state: LiveData<Boolean> = mutableState

    fun initialize() {
        mutableState.value = true
    }
}
