package com.example.myapplication

import androidx.compose.runtime.Composable
 import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapplication.ui.home.HomeScreen
import com.example.myapplication.ui.theme.MyApplicationTheme

/**
 * Preview oficial da Casa da Faxina redirecionado para a tela principal (Home).
 */
@Preview(
    name = "Casa da Faxina - Apresentação",
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_7
)
@Composable
fun BakingScreenPreview() {
    MyApplicationTheme {
        HomeScreen()
    }
}

