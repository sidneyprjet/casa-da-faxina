package com.example.myapplication.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CasaDaFaxinaColorScheme = lightColorScheme(
    primary = AzulCobalto,
    onPrimary = Color.White,
    primaryContainer = FundoNevoaAzul,
    onPrimaryContainer = AzulSafiraProfundo,
    secondary = VerdeBotanico,
    onSecondary = Color.White,
    secondaryContainer = VerdeAguaClaro,
    onSecondaryContainer = VerdeGrafite,
    tertiary = AzulSafiraProfundo,
    background = FundoPrincipal,
    onBackground = VerdeGrafite,
    surface = Color.White,
    onSurface = VerdeGrafite,
    surfaceVariant = VerdeAguaClaro,
    onSurfaceVariant = VerdeGrafite,
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color.White,
    surfaceContainer = Color.White,
    surfaceContainerHigh = Color.White,
    surfaceContainerHighest = Color.White,
    outline = BordaVerdeAgua,
    outlineVariant = BordaVerdeAgua
)

@Composable
fun MyApplicationTheme(
    // Mantidos por compatibilidade; a identidade da loja é sempre clara e fixa.
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = false,
    @Suppress("UNUSED_PARAMETER") dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CasaDaFaxinaColorScheme,
        typography = Typography,
        content = content
    )
}
