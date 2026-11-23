package com.example.myapplication.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.ui.theme.AzulPrimario
import com.example.myapplication.ui.theme.AzulSafiraProfundo
import com.example.myapplication.ui.theme.AzulOceano
import com.example.myapplication.ui.theme.OuroChampanhe
import com.example.myapplication.ui.theme.AzulEscuro
import com.example.myapplication.ui.theme.AmareloDestaque
import com.example.myapplication.ui.theme.MyApplicationTheme

/**
 * Componente reutilizável de Cabeçalho da Casa da Faxina.
 *
 * Implementa exatamente a estrutura visual do layout XML fornecido:
 * - Fundo Azul (#0047BB / AzulPrimario) com elevação 4.dp
 * - Seta de Voltar (na esquerda quando onBackClick != null)
 * - Logo e Títulos centralizados:
 *     "CASA DA FAXINA" (Branco 16sp bold)
 *     "PRODUTOS DE LIMPEZA" (Amarelo #FFD500 10sp)
 * - Slot de Ações à direita para WhatsApp (24dp) e Carrinho (28dp) com Badge Amarela e texto preto em negrito.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CasaDaFaxinaHeader(
    title: String = "Casa da Faxina",
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
    showBrandTitle: Boolean = true,
    onAdminAccess: () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    val cliquesLogo = remember { mutableIntStateOf(0) }

    val headerShape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
    val headerGradient = Brush.verticalGradient(
        colors = listOf(AzulSafiraProfundo, AzulOceano)
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(10.dp, headerShape)
            .clip(headerShape)
            .background(headerGradient)
            .drawBehind {
                val lineHeight = 1.5.dp.toPx()
                val lineY = size.height - lineHeight / 2f
                drawLine(
                    color = OuroChampanhe,
                    start = androidx.compose.ui.geometry.Offset(0f, lineY),
                    end = androidx.compose.ui.geometry.Offset(size.width, lineY),
                    strokeWidth = lineHeight
                )
            },
        shape = headerShape,
        shadowElevation = 0.dp,
        color = Color.Transparent
    ) {
        TopAppBar(
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (showBrandTitle || title == "Casa da Faxina" || title.isBlank()) {
                        Column(
                            modifier = Modifier.clickable {
                                cliquesLogo.intValue++
                                if (cliquesLogo.intValue >= 5) {
                                    cliquesLogo.intValue = 0
                                    onAdminAccess()
                                }
                            },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "CASA DA FAXINA",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "PRODUTOS DE LIMPEZA",
                                color = AmareloDestaque,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Text(
                            text = title,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            },
            navigationIcon = {
                if (onBackClick != null) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            },
            actions = actions,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White
            )
        )
    }
}

@Composable
fun CircularCartBadge(
    count: Int,
    modifier: Modifier = Modifier
) {
    if (count <= 0) return

    val label = count.toString()
    val diameter = (20 + (label.length - 1) * 5).coerceAtMost(40).dp
    Box(
        modifier = modifier
            .size(diameter)
            .clip(CircleShape)
            .background(AmareloDestaque),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = label,
            color = AzulSafiraProfundo,
            fontSize = if (label.length > 3) 8.sp else 10.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            softWrap = false,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CasaDaFaxinaHeaderPreview() {
    MyApplicationTheme {
        CasaDaFaxinaHeader(
            title = "Casa da Faxina",
            onBackClick = {},
            actions = {
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_whatsapp),
                        contentDescription = "WhatsApp",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                IconButton(onClick = {}) {
                    BadgedBox(
                        badge = {
                            CircularCartBadge(count = 2)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_cart),
                            contentDescription = "Carrinho",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        )
    }
}
