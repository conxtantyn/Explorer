package com.explorer.android.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.explorer.android.ui.main.MainUiViewModel
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

internal class MainUiViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: MainUiViewModel

    @Before
    fun setup() {
        viewModel = MainUiViewModel()
    }

    @Test
    fun `state should initially be false`() {
        val actualState = viewModel.state.value
        assertEquals(false, actualState)
    }

    @Test
    fun `initialize should set state to true`() {
        val observer = mock(Observer::class.java) as Observer<Boolean>
        viewModel.state.observeForever(observer)

        viewModel.initialize()

        assertEquals(true, viewModel.state.value)
        verify(observer).onChanged(true)
    }
}
