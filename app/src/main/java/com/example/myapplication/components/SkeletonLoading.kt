package com.example.myapplication.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.FundoBranco

/**
 * Modificador reutilizável para aplicar o efeito Skeleton Loading / Shimmer
 * (brilho suave passando sobre um fundo cinza, padrão de apps como iFood).
 */
@Composable
fun Modifier.shimmerEffect(): Modifier {
    val transition = rememberInfiniteTransition(label = "ShimmerTransition")
    val translateAnim = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerAnim"
    )

    val shimmerColors = listOf(
        Color(0xFFE2E8F0),
        Color(0xFFF8FAFC),
        Color(0xFFE2E8F0)
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    return this.background(brush)
}

/**
 * Bloco retangular de Skeleton Loading para simular imagens ou textos carregando.
 */
@Composable
fun SkeletonBox(
    modifier: Modifier = Modifier,
    width: Dp? = null,
    height: Dp,
    shapeRadius: Dp = 8.dp
) {
    val baseModifier = if (width != null) {
        modifier.width(width)
    } else {
        modifier.fillMaxWidth()
    }

    Box(
        modifier = baseModifier
            .height(height)
            .clip(RoundedCornerShape(shapeRadius))
            .shimmerEffect()
    )
}

/**
 * Skeleton completo simulando um card de produto carregando.
 */
@Composable
fun ProdutoCardSkeleton(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .width(170.dp)
            .clip(RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FundoBranco)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Imagem skeleton
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(135.dp)
                    .shimmerEffect()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                // Linha de texto 1
                SkeletonBox(height = 14.dp, shapeRadius = 4.dp)
                Spacer(modifier = Modifier.height(6.dp))
                // Linha de texto 2
                SkeletonBox(width = 90.dp, height = 12.dp, shapeRadius = 4.dp)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Preço skeleton
                    SkeletonBox(width = 65.dp, height = 18.dp, shapeRadius = 4.dp)
                    Spacer(modifier = Modifier.weight(1f))
                    // Botão adicionar skeleton
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SkeletonLoadingPreview() {
    Row(
        modifier = Modifier.padding(16.dp)
    ) {
        ProdutoCardSkeleton()
        Spacer(modifier = Modifier.width(16.dp))
        ProdutoCardSkeleton()
    }
}
