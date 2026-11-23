package com.example.myapplication.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Ilustração vetorial 100% Compose por categoria de produto.
 * Sem marca, sem texto, sem imagem externa — apenas formas e cores bonitas.
 */
@Composable
fun ProdutoPlaceholderImage(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 14.dp,
    categoria: String = "Limpeza"
) {
    val cat = categoria.lowercase().trim()

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(cornerRadius)),
        contentAlignment = Alignment.Center
    ) {
        when {
            cat.contains("piso") || cat.contains("laminado") || cat.contains("porcelanato") ->
                IlustraPiso(modifier = Modifier.fillMaxSize())

            cat.contains("tecido") || cat.contains("tapete") || cat.contains("estofado") ->
                IlustraTecido(modifier = Modifier.fillMaxSize())

            cat.contains("acess") || cat.contains("mop") || cat.contains("microfibra") ->
                IlustraAcessorio(modifier = Modifier.fillMaxSize())

            else ->
                IlustraGenerica(modifier = Modifier.fillMaxSize())
        }
    }
}

// ─── PISOS: frasco de limpador azul moderno ───────────────────────────────────
@Composable
private fun IlustraPiso(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(
            Brush.verticalGradient(listOf(Color(0xFFD6EAFF), Color(0xFFEEF6FF)))
        ),
        contentAlignment = Alignment.Center
    ) {
        // Piso estilizado em perspectiva
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Frasco principal
            Box(contentAlignment = Alignment.TopCenter) {
                // Corpo do frasco
                Box(
                    modifier = Modifier
                        .width(36.dp)
                        .height(52.dp)
                        .offset(y = 10.dp)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp, bottomStart = 10.dp, bottomEnd = 10.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF1565C0), Color(0xFF0D47A1))
                            )
                        )
                )
                // Rótulo branco
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(20.dp)
                        .offset(y = 24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.White.copy(alpha = 0.85f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .width(18.dp)
                                .height(2.dp)
                                .background(Color(0xFF1565C0), RoundedCornerShape(1.dp))
                        )
                        Spacer(Modifier.height(3.dp))
                        Box(
                            modifier = Modifier
                                .width(14.dp)
                                .height(2.dp)
                                .background(Color(0xFF90CAF9), RoundedCornerShape(1.dp))
                        )
                    }
                }
                // Tampa / pescoço
                Box(
                    modifier = Modifier
                        .width(20.dp)
                        .height(10.dp)
                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                        .background(Color(0xFF42A5F5))
                )
                // Bico
                Box(
                    modifier = Modifier
                        .width(10.dp)
                        .height(6.dp)
                        .offset(y = (-8).dp)
                        .clip(RoundedCornerShape(topStart = 3.dp, topEnd = 3.dp))
                        .background(Color(0xFF1E88E5))
                )
            }

            Spacer(Modifier.height(10.dp))

            // Piso em perspectiva (3 tiles)
            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .width(16.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFFB3D9FF), Color(0xFF90CAF9))
                                )
                            )
                    )
                }
            }
        }

        // Brilho decorativo
        Text(
            text = "✦",
            fontSize = 10.sp,
            color = Color(0xFFFFD54F),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
        )
    }
}

// ─── TECIDOS: frasco spray + bolhas de espuma ─────────────────────────────────
@Composable
private fun IlustraTecido(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(
            Brush.verticalGradient(listOf(Color(0xFFE8F5E9), Color(0xFFF1F8E9)))
        ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Spray horizontal
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Cabo do spray
                Box(
                    modifier = Modifier
                        .width(12.dp)
                        .height(16.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFF66BB6A))
                )
                Spacer(Modifier.width(2.dp))
                // Corpo do spray
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(44.dp)
                        .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 10.dp, bottomStart = 4.dp, bottomEnd = 10.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF43A047), Color(0xFF2E7D32))
                            )
                        )
                ) {
                    // Rótulo
                    Box(
                        modifier = Modifier
                            .width(18.dp)
                            .height(16.dp)
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(3.dp))
                            .background(Color.White.copy(alpha = 0.8f))
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            // Bolhas de espuma
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                listOf(14.dp, 10.dp, 12.dp).forEach { size ->
                    Box(
                        modifier = Modifier
                            .size(size)
                            .clip(CircleShape)
                            .background(Color(0xFFA5D6A7).copy(alpha = 0.7f))
                            .padding(2.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(4.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.6f))
                                .align(Alignment.TopStart)
                        )
                    }
                }
            }
        }

        // Brilhinhos decorativos
        Text("✦", fontSize = 9.sp, color = Color(0xFFFFCC02),
            modifier = Modifier.align(Alignment.TopStart).padding(8.dp))
        Text("✦", fontSize = 7.sp, color = Color(0xFF81C784),
            modifier = Modifier.align(Alignment.BottomEnd).padding(10.dp))
    }
}

// ─── ACESSÓRIOS: vassoura / mop estilizado ────────────────────────────────────
@Composable
private fun IlustraAcessorio(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(
            Brush.verticalGradient(listOf(Color(0xFFFFF8E1), Color(0xFFFFF3E0)))
        ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Cabo
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(42.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFFFFB300), Color(0xFFF57F17))
                        )
                    )
            )
            // Cabeça do mop (franjas)
            Box(contentAlignment = Alignment.TopCenter) {
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(10.dp)
                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                        .background(Color(0xFFFFCA28))
                )
                Row(
                    modifier = Modifier
                        .width(44.dp)
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(7) {
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(16.dp)
                                .clip(RoundedCornerShape(bottomStart = 3.dp, bottomEnd = 3.dp))
                                .background(
                                    Brush.verticalGradient(
                                        listOf(Color(0xFFFFE082), Color(0xFFFFCA28))
                                    )
                                )
                        )
                    }
                }
            }

            Spacer(Modifier.height(6.dp))

            // Chão brilhando
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFFFFE082).copy(alpha = 0.4f),
                                Color(0xFFFFF9C4),
                                Color(0xFFFFE082).copy(alpha = 0.4f)
                            )
                        )
                    )
            )
        }

        // Estrelinhas de brilho
        Text("✦", fontSize = 11.sp, color = Color(0xFFFFD54F),
            modifier = Modifier.align(Alignment.TopEnd).padding(8.dp))
        Text("✦", fontSize = 7.sp, color = Color(0xFFFFB300),
            modifier = Modifier.align(Alignment.TopStart).padding(12.dp, 14.dp))
    }
}

// ─── GENÉRICA: caixa de produto ───────────────────────────────────────────────
@Composable
private fun IlustraGenerica(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(
            Brush.verticalGradient(listOf(Color(0xFFEDE7F6), Color(0xFFF3E5F5)))
        ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Caixa
            Box(contentAlignment = Alignment.TopCenter) {
                // Corpo
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(36.dp)
                        .offset(y = 8.dp)
                        .clip(RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp, topStart = 2.dp, topEnd = 2.dp))
                        .background(
                            Brush.verticalGradient(
                                listOf(Color(0xFF7E57C2), Color(0xFF512DA8))
                            )
                        )
                )
                // Tampa
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(10.dp)
                        .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                        .background(Color(0xFF9575CD))
                )
                // Fita decorativa
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(36.dp)
                        .offset(y = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(36.dp)
                            .background(Color(0xFFCE93D8).copy(alpha = 0.6f))
                    )
                    Box(
                        modifier = Modifier
                            .width(44.dp)
                            .height(4.dp)
                            .background(Color(0xFFCE93D8).copy(alpha = 0.6f))
                    )
                }
            }

            Spacer(Modifier.height(10.dp))
            Text("✦", fontSize = 10.sp, color = Color(0xFFFFD54F))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProdutoPlaceholderImagePreview() {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(16.dp)) {
        ProdutoPlaceholderImage(categoria = "Pisos", modifier = Modifier.size(90.dp))
        ProdutoPlaceholderImage(categoria = "Tecidos", modifier = Modifier.size(90.dp))
        ProdutoPlaceholderImage(categoria = "Acessórios", modifier = Modifier.size(90.dp))
    }
}
