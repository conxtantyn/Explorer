package com.explorer.discovery.ui.view.composer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.material.R

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun UiToolbar() {
    var isToggled by remember { mutableStateOf(false) }
    TopAppBar(
        title = { },
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_call_answer_video),
                contentDescription = "Menu Icon",
                modifier = Modifier
                    .clickable { /* Handle menu click */ }
                    .padding(8.dp),
                tint = Color.White
            )
        },
        actions = {
            Icon(
                painter = painterResource(id = if (isToggled) R.drawable.ic_call_answer_video else R.drawable.ic_call_answer),
                contentDescription = "Toggle Icon",
                modifier = Modifier
                    .clickable {
                        isToggled = !isToggled
                    }
                    .padding(8.dp),
                tint = Color.White
            )
        },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF6200EA))
    )
}
