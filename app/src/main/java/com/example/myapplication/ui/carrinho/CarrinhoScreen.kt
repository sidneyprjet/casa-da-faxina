package com.example.myapplication.ui.carrinho

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.components.CircularCartBadge
import com.example.myapplication.data.model.Produto
import com.example.myapplication.ui.home.HomeTab
import com.example.myapplication.ui.home.produtosExemplo
import com.example.myapplication.ui.theme.AmareloDestaque
import com.example.myapplication.ui.theme.AzulEscuro
import com.example.myapplication.ui.theme.AzulSafiraProfundo
import com.example.myapplication.ui.theme.AzulOceano
import com.example.myapplication.ui.theme.AzulPrimario
import com.example.myapplication.ui.theme.BordaCinza
import com.example.myapplication.ui.theme.FundoBranco
import com.example.myapplication.ui.theme.FundoPrincipal
import com.example.myapplication.ui.theme.FundoRosaMascote
import com.example.myapplication.ui.theme.FundoRosaBochechaSuave
import com.example.myapplication.ui.theme.FundoNevoaAzul
import com.example.myapplication.ui.theme.FundoSoproVerde
import com.example.myapplication.ui.theme.OuroChampanhe
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.TextoEscuro
import com.example.myapplication.ui.theme.TextoSecundario
import com.example.myapplication.ui.theme.VermelhoFaixa
import com.example.myapplication.util.WhatsAppHelper

data class ItemCarrinho(
    val produto: Produto,
    val quantidade: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarrinhoScreen(
    modifier: Modifier = Modifier,
    itens: List<ItemCarrinho> = listOf(
        ItemCarrinho(produtosExemplo[0], 1),
        ItemCarrinho(produtosExemplo[1], 1)
    ),
    onIncrementItem: (ItemCarrinho) -> Unit = {},
    onDecrementItem: (ItemCarrinho) -> Unit = {},
    onRemoverItem: (ItemCarrinho) -> Unit = {},
    onLimparCarrinho: () -> Unit = {},
    onVoltar: () -> Unit = {},
    onConfirmarPedidoWhatsApp: (List<ItemCarrinho>) -> Unit = {},
    onNavigateTab: (HomeTab) -> Unit = {}
) {
    val context = LocalContext.current
    var mostrarDialogRevisao by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val bodyGradient = Brush.verticalGradient(
        colors = listOf(
            FundoPrincipal,
            FundoRosaMascote,
            FundoRosaBochechaSuave,
            FundoNevoaAzul,
            FundoSoproVerde
        )
    )

    val subtotal = itens.sumOf { it.produto.preco * it.quantidade }
    val totalItens = itens.sumOf { it.quantidade }

    // Pulsar no botão Revisar quando houver itens
    val infiniteTransition = rememberInfiniteTransition(label = "PulseRevisar")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (itens.isNotEmpty()) 1.02f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseScale"
    )

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        containerColor = Color.Transparent,
        topBar = {
            val headerShape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            val headerGradient = Brush.verticalGradient(
                colors = listOf(AzulSafiraProfundo, AzulOceano)
            )

            CenterAlignedTopAppBar(
                modifier = Modifier
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
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "CARRINHO",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Text(
                            text = "CASA DA FAXINA",
                            color = OuroChampanhe,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.1.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onVoltar,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Voltar",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { WhatsAppHelper.abrirSuporte(context) },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_whatsapp),
                            contentDescription = "WhatsApp",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { /* já está no carrinho */ },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_cart),
                                contentDescription = "Carrinho",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        CircularCartBadge(
                            count = totalItens,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 1.dp, y = (-1).dp)
                        )
                    }
                }
            )
        },
        bottomBar = {
            CarrinhoBottomBar(
                selectedTab = HomeTab.CARRINHO,
                cartBadgeCount = totalItens,
                onTabSelected = onNavigateTab
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(bodyGradient)
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // ── BANNER DO MASCOTE com balão de fala saindo por cima ──────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                // Card azul escuro com sombra colorida por baixo
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(24.dp),
                            ambientColor = AzulPrimario.copy(alpha = 0.4f),
                            spotColor = AzulPrimario.copy(alpha = 0.5f)
                        )
                        .border(
                            width = 1.dp,
                            color = AmareloDestaque.copy(alpha = 0.65f),
                            shape = RoundedCornerShape(24.dp)
                        ),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0047BB))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(76.dp)
                    ) {
                        // Gradiente decorativo no fundo do banner
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.radialGradient(
                                        colors = listOf(
                                            Color(0xFF1565C0),
                                            Color(0xFF0038A8)
                                        ),
                                        radius = 600f
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Tudo prontinho",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                lineHeight = 22.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Text(
                                text = "para revisar!",
                                color = AmareloDestaque,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                lineHeight = 22.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── LISTA DE ITENS ────────────────────────────────────────────────
            if (itens.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 60.dp, horizontal = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🛒", fontSize = 56.sp)
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Seu carrinho está vazio",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = AzulEscuro
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Adicione produtos incríveis da Casa da Faxina!",
                            fontSize = 13.sp,
                            color = TextoSecundario
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { onNavigateTab(HomeTab.INICIO) },
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
                        ) {
                            Text(
                                "Explorar produtos",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itens.forEach { item ->
                        ItemCarrinhoCard(
                            item = item,
                            onIncrement = { onIncrementItem(item) },
                            onDecrement = { onDecrementItem(item) },
                            onRemover = { onRemoverItem(item) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ── RESUMO DE VALORES ─────────────────────────────────────────
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .shadow(
                            elevation = 6.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = AzulPrimario.copy(alpha = 0.15f),
                            spotColor = AzulPrimario.copy(alpha = 0.2f)
                        ),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = FundoBranco)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 18.dp)
                    ) {
                        // Título da seção
                        Text(
                            text = "Resumo do pedido",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoSecundario,
                            letterSpacing = 0.3.sp
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Linha por produto
                        itens.forEach { item ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${item.quantidade}× ${item.produto.nome}",
                                    fontSize = 13.sp,
                                    color = TextoEscuro,
                                    modifier = Modifier.weight(1f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "R$ ${"%.2f".format(item.produto.preco * item.quantidade).replace('.', ',')}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextoEscuro
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = Color(0xFFF0E5E9), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Subtotal em destaque
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Subtotal",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoEscuro
                            )
                            Text(
                                text = "R$ ${"%.2f".format(subtotal).replace('.', ',')}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AzulPrimario
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Frete
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFF0F7FF), RoundedCornerShape(12.dp))
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🚚", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Entrega / Frete",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextoEscuro
                                    )
                                    Text(
                                        text = "Combinamos no WhatsApp",
                                        fontSize = 11.sp,
                                        color = TextoSecundario
                                    )
                                }
                            }
                            Text(
                                text = "A combinar",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulPrimario
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // ── BOTÃO REVISAR PEDIDO — surreal, pulsante, amarelo com sombra ──
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Button(
                        onClick = { mostrarDialogRevisao = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(58.dp)
                            .scale(pulseScale)
                            .shadow(
                                elevation = 14.dp,
                                shape = RoundedCornerShape(29.dp),
                                ambientColor = AmareloDestaque.copy(alpha = 0.6f),
                                spotColor = AmareloDestaque.copy(alpha = 0.9f)
                            ),
                        shape = RoundedCornerShape(29.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AmareloDestaque,
                            contentColor = TextoEscuro
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_whatsapp),
                                contentDescription = null,
                                tint = Color(0xFF1A1A1A),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Revisar pedido",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextoEscuro
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = ">",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulEscuro
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }

    // ── DIALOG DE CONFIRMAÇÃO ─────────────────────────────────────────────────
    if (mostrarDialogRevisao) {
        AlertDialog(
            onDismissRequest = { mostrarDialogRevisao = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_whatsapp),
                        contentDescription = "WhatsApp",
                        tint = Color(0xFF25D366),
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Enviar para a Sarinha",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulEscuro
                    )
                }
            },
            text = {
                Column {
                    Text(
                        text = "Seu pedido será enviado diretamente para o WhatsApp oficial da loja para combinar entrega e frete.",
                        fontSize = 13.sp,
                        color = TextoEscuro,
                        lineHeight = 19.sp
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFEEF4FF), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Total (${itens.sumOf { it.quantidade }} itens)",
                                fontSize = 13.sp,
                                color = TextoSecundario
                            )
                            Text(
                                text = "R$ ${"%.2f".format(subtotal).replace('.', ',')}",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AzulPrimario
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogRevisao = false
                        onConfirmarPedidoWhatsApp(itens)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_whatsapp),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "Confirmar no WhatsApp",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { mostrarDialogRevisao = false },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Voltar", color = TextoEscuro)
                }
            },
            containerColor = FundoBranco,
            shape = RoundedCornerShape(20.dp)
        )
    }
}

// ── CARD DE ITEM DO CARRINHO — Premium, igual ao design do anexo ─────────────
@Composable
fun ItemCarrinhoCard(
    item: ItemCarrinho,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemover: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = Color(0x14000000),
                spotColor = AzulPrimario.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = FundoBranco)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Imagem do produto: quadrado grande e nítido ──────────────────
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Imagem real do produto se disponível, senão ilustração vetorial
                if (item.produto.imagemRes != 0) {
                    Image(
                        painter = painterResource(id = item.produto.imagemRes),
                        contentDescription = item.produto.nome,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    com.example.myapplication.components.ProdutoPlaceholderImage(
                        categoria = item.produto.categoria,
                        cornerRadius = 16.dp,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            // ── Detalhes à direita ────────────────────────────────────────────
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Nome + Lixeira
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = item.produto.nome,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextoEscuro,
                        maxLines = 2,
                        lineHeight = 19.sp,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onRemover,
                        modifier = Modifier
                            .size(32.dp)
                            .offset(x = 6.dp, y = (-4).dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            contentDescription = "Remover item",
                            tint = Color(0xFFB0BEC5),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Preço em azul bold
                Text(
                    text = "R$ ${"%.2f".format(item.produto.preco).replace('.', ',')}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AzulPrimario
                )

                Spacer(modifier = Modifier.height(10.dp))

                // ── Stepper "— N +" moderno ───────────────────────────────────
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    // Botão –
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .shadow(3.dp, CircleShape)
                            .clip(CircleShape)
                            .background(Color(0xFF0047BB))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, color = Color.White),
                                onClick = onDecrement
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "−",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }

                    // Quantidade animada
                    AnimatedContent(
                        targetState = item.quantidade,
                        transitionSpec = {
                            (scaleIn(initialScale = 0.7f) + fadeIn())
                                .togetherWith(scaleOut(targetScale = 0.7f) + fadeOut())
                        },
                        label = "Qtd"
                    ) { qtd ->
                        Text(
                            text = qtd.toString(),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = AzulEscuro,
                            modifier = Modifier.padding(horizontal = 14.dp)
                        )
                    }

                    // Botão +
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .shadow(3.dp, CircleShape)
                            .clip(CircleShape)
                            .background(AmareloDestaque)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, color = AzulEscuro),
                                onClick = onIncrement
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = AzulEscuro
                        )
                    }
                }
            }
        }
    }
}

// ── BOTTOM BAR ────────────────────────────────────────────────────────────────
@Composable
fun CarrinhoBottomBar(
    selectedTab: HomeTab,
    cartBadgeCount: Int = 0,
    onTabSelected: (HomeTab) -> Unit
) {
    NavigationBar(
        containerColor = FundoBranco,
        tonalElevation = 8.dp,
        modifier = Modifier.shadow(8.dp)
    ) {
        HomeTab.entries.forEach { tab ->
            val isSelected = selectedTab == tab

            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    if (tab == HomeTab.CARRINHO && cartBadgeCount > 0) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = AmareloDestaque,
                                    contentColor = AzulEscuro
                                ) {
                                    Text(
                                        text = cartBadgeCount.toString(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = tab.iconRes),
                                contentDescription = tab.label,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Icon(
                            painter = painterResource(id = tab.iconRes),
                            contentDescription = tab.label,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                label = {
                    Text(
                        text = tab.label,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AzulPrimario,
                    selectedTextColor = AzulPrimario,
                    indicatorColor = Color(0xFFF3E8CB),
                    unselectedIconColor = TextoSecundario,
                    unselectedTextColor = TextoSecundario
                )
            )
        }
    }
}

// ── PREVIEW ───────────────────────────────────────────────────────────────────
@Preview(
    name = "Carrinho - Celular Real",
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_7
)
@Composable
fun CarrinhoScreenPreview() {
    MyApplicationTheme {
        CarrinhoScreen()
    }
}
