package com.example.myapplication

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.AzulCobalto
import com.example.myapplication.ui.theme.AzulSafiraProfundo
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.OuroChampanhe

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onNavigateNext: () -> Unit = {}
) {
    var entranceStarted by remember { mutableStateOf(false) }
    val entrance by animateFloatAsState(
        targetValue = if (entranceStarted) 1f else 0f,
        animationSpec = tween(durationMillis = 900, easing = FastOutSlowInEasing),
        label = "splashEntrance"
    )
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        entranceStarted = true
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2200, easing = LinearEasing)
        )
        onNavigateNext()
    }

    val capsuleShape = RoundedCornerShape(20.dp)
    val capsuleBorder = Brush.linearGradient(
        colors = listOf(OuroChampanhe, AzulCobalto)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onNavigateNext()
            }
    ) {
        // A arte original já reúne mascote, logo e cenário: ela entra como uma peça única.
        Image(
            painter = painterResource(id = R.drawable.splash_screen_art),
            contentDescription = "Mascote e identidade visual da Casa da Faxina",
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = entrance
                    val scale = 1.025f - 0.025f * entrance
                    scaleX = scale
                    scaleY = scale
                },
            contentScale = ContentScale.Crop,
            alignment = Alignment.Center
        )

        // Suave clareamento na base para encobrir o slogan impresso atrás da barra do sistema.
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(64.dp)
                .background(
                    Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to Color.Transparent,
                            0.38f to Color.White,
                            1f to Color.White
                        )
                    )
                )
        )

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            val density = LocalDensity.current
            val widthPx = with(density) { maxWidth.toPx() }
            val heightPx = with(density) { maxHeight.toPx() }

            // Luz de estúdio branca bem leve, concentrada no mascote e na marca.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.18f),
                                Color.White.copy(alpha = 0.08f),
                                Color.Transparent
                            ),
                            center = Offset(widthPx / 2f, heightPx * 0.48f),
                            radius = widthPx * 0.82f
                        )
                    )
            )

            // Slogan em cápsula, sobre a área já impressa na arte original.
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
                    .padding(top = maxHeight * 0.66f)
                    .shadow(10.dp, capsuleShape)
                    .clip(capsuleShape)
                    .background(Color.White.copy(alpha = 0.97f))
                    .border(1.dp, capsuleBorder, capsuleShape)
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .graphicsLayer {
                        alpha = entrance
                        val scale = 0.96f + 0.04f * entrance
                        scaleX = scale
                        scaleY = scale
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Seu cuidado com a casa,\nna palma da mão",
                    color = AzulSafiraProfundo,
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center
                )

                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(96.dp)
                        .height(3.dp)
                        .clip(CircleShape)
                        .background(AzulCobalto.copy(alpha = 0.16f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress.value)
                            .fillMaxHeight()
                            .background(OuroChampanhe, CircleShape)
                    )
                }
            }

        }
    }
}

@Preview(
    name = "Splash - Celular Real",
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_7
)
@Composable
fun SplashScreenPreview() {
    MyApplicationTheme {
        SplashScreen()
    }
}
