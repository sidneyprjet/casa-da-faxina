package com.example.myapplication.ui.produto

import android.content.Intent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.components.CasaDaFaxinaHeader
import com.example.myapplication.components.CircularCartBadge
import com.example.myapplication.data.model.Produto
import com.example.myapplication.ui.home.HomeBottomBar
import com.example.myapplication.ui.home.HomeTab
import com.example.myapplication.ui.theme.AmareloDestaque
import com.example.myapplication.ui.theme.AzulEscuro
import com.example.myapplication.ui.theme.AzulPrimario
import com.example.myapplication.ui.theme.BordaCinza
import com.example.myapplication.ui.theme.FundoBranco
import com.example.myapplication.ui.theme.FundoInput
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.TextoEscuro
import com.example.myapplication.ui.theme.TextoSecundario
import com.example.myapplication.ui.theme.VermelhoFaixa
import com.example.myapplication.util.WhatsAppHelper

val produtoDetalheExemplo = Produto(
    id = "2",
    nome = "Limpador para Laminado",
    preco = 29.99,
    categoria = "Pisos",
    imagemRes = R.drawable.produto_laminado,
    descricao = "Fórmula exclusiva desenvolvida para pisos laminados e vinílicos. Remove poeira, restaura o brilho natural e cria uma camada protetora contra umidade e arranhões, deixando um aroma suave e duradouro.",
    fotosGaleria = listOf(
        R.drawable.produto_laminado,
        R.drawable.produto_detalhe_hero,
        R.drawable.banner_mascote_piso
    )
)

@Composable
fun ProdutoDetalheScreen(
    modifier: Modifier = Modifier,
    produto: Produto = produtoDetalheExemplo,
    cartItemCount: Int = 2,
    onVoltar: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onAdicionarAoCarrinho: (Produto, Int) -> Unit = { _, _ -> },
    onNavigateTab: (HomeTab) -> Unit = {},
    onAdminAccess: () -> Unit = {}
) {
    val context = LocalContext.current
    var quantidade by remember { mutableIntStateOf(1) }
    var isFavorito by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    // Lista de fotos disponíveis para a cliente alternar
    val galeriaFotos = remember(produto.id, produto.fotosGaleria, produto.imagemRes) {
        (produto.fotosGaleria + produto.imagemRes)
            .filter { it != 0 }
            .distinct()
            .ifEmpty {
                listOf(
                    R.drawable.produto_detalhe_hero,
                    R.drawable.banner_mascote_piso
                )
            }
    }
    var fotoSelecionadaIndex by remember { mutableIntStateOf(0) }
    val fotoAtual = galeriaFotos.getOrElse(fotoSelecionadaIndex) { produto.imagemRes }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color(0xFFF8FAFC),
        // Cabeçalho Oficial: Logo da Loja, WhatsApp e Carrinho com Badge
        topBar = {
            CasaDaFaxinaHeader(
                title = "Casa da Faxina",
                onBackClick = null,
                onAdminAccess = onAdminAccess,
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
                            contentDescription = "Atendimento WhatsApp",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(
                        onClick = onCartClick,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        BadgedBox(
                            badge = {
                                CircularCartBadge(
                                    count = cartItemCount,
                                    modifier = Modifier.offset(x = 4.dp, y = (-2).dp)
                                )
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_cart),
                                contentDescription = "Carrinho",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            HomeBottomBar(
                selectedTab = HomeTab.INICIO,
                cartBadgeCount = cartItemCount,
                onTabSelected = onNavigateTab
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // 1. Imagem Principal Grande, Perfeitamente Centralizada em Card com Elevação Suave
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(290.dp)
                    .shadow(
                        elevation = 4.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = Color(0x18000000),
                        spotColor = AzulPrimario.copy(alpha = 0.15f)
                    ),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = FundoBranco)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    com.example.myapplication.components.ProdutoPlaceholderImage(
                        categoria = produto.categoria,
                        cornerRadius = 16.dp,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Botões Flutuantes: Favorito e Compartilhar com Ripple e Sombra
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Botão Favorito
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .shadow(3.dp, CircleShape)
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = true, color = VermelhoFaixa),
                                    onClick = { isFavorito = !isFavorito }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_favorite_border),
                                contentDescription = "Favoritar",
                                tint = if (isFavorito) VermelhoFaixa else TextoSecundario,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Botão Compartilhar
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .shadow(3.dp, CircleShape)
                                .clip(CircleShape)
                                .background(Color.White)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = true, color = AzulPrimario),
                                    onClick = {
                                        val sendIntent = Intent().apply {
                                            action = Intent.ACTION_SEND
                                            putExtra(
                                                Intent.EXTRA_TEXT,
                                                "Confira ${produto.nome} na Casa da Faxina por apenas R$ ${"%.2f".format(produto.preco).replace('.', ',')}!"
                                            )
                                            type = "text/plain"
                                        }
                                        context.startActivity(Intent.createChooser(sendIntent, "Compartilhar produto"))
                                    }
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = "Compartilhar",
                                tint = TextoSecundario,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Carrossel de Miniaturas com seleção nítida
            if (galeriaFotos.size > 1) {
                Spacer(modifier = Modifier.height(14.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    galeriaFotos.forEachIndexed { index, fotoResId ->
                        val isSelected = index == fotoSelecionadaIndex
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 6.dp)
                                .size(58.dp)
                                .shadow(if (isSelected) 3.dp else 1.dp, RoundedCornerShape(12.dp))
                                .clip(RoundedCornerShape(12.dp))
                                .background(if (isSelected) Color(0xFFE8F0FE) else FundoBranco)
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) AzulPrimario else BordaCinza.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { fotoSelecionadaIndex = index },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = fotoResId),
                                contentDescription = "Foto ${index + 1}",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(4.dp),
                                contentScale = ContentScale.Fit,
                                alignment = Alignment.Center
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Card Principal de Detalhes, Preço e Stepper
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(2.dp, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = FundoBranco)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // Tag de Categoria com visual de pílula azul suave
                    Row(
                        modifier = Modifier
                            .background(Color(0xFFE8F0FE), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = produto.categoria.uppercase(),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = AzulPrimario
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = produto.nome,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulEscuro
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Selo de e-commerce: Entrega a combinar via WhatsApp
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💬 Frete a combinar no WhatsApp", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = AzulPrimario)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = "• Em estoque", fontSize = 12.sp, color = TextoSecundario)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Preço unitário",
                                fontSize = 12.sp,
                                color = TextoSecundario
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "R$ ${"%.2f".format(produto.preco).replace('.', ',')}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AzulPrimario
                            )
                        }

                        // 2. Stepper Pílula com Micro-interações (Ripple e animação no número)
                        Row(
                            modifier = Modifier
                                .background(Color(0xFFF1F5F9), RoundedCornerShape(20.dp))
                                .border(1.dp, BordaCinza.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                                .padding(horizontal = 4.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple(bounded = true, color = AzulPrimario),
                                        onClick = { if (quantidade > 1) quantidade-- }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "—",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (quantidade > 1) AzulEscuro else TextoSecundario
                                )
                            }

                            // Micro-animação suave no número
                            AnimatedContent(
                                targetState = quantidade,
                                transitionSpec = {
                                    (scaleIn(initialScale = 0.75f) + fadeIn()).togetherWith(scaleOut(targetScale = 0.75f) + fadeOut())
                                },
                                label = "QuantidadeDetalheAnimada"
                            ) { qtd ->
                                Text(
                                    text = qtd.toString(),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AzulEscuro,
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = ripple(bounded = true, color = AzulPrimario),
                                        onClick = { quantidade++ }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AzulEscuro
                                )
                            }
                        }
                    }

                    if (produto.descricao.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = "Sobre o Produto",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = AzulEscuro
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = produto.descricao,
                            fontSize = 14.sp,
                            color = TextoSecundario,
                            lineHeight = 20.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // 2. Botão Principal Amarelo com Sombra Dourada e Efeito de Toque
                    Button(
                        onClick = { onAdicionarAoCarrinho(produto, quantidade) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .shadow(
                                elevation = 5.dp,
                                shape = RoundedCornerShape(27.dp),
                                ambientColor = Color(0x33000000),
                                spotColor = AmareloDestaque.copy(alpha = 0.85f)
                            ),
                        shape = RoundedCornerShape(27.dp),
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
                                painter = painterResource(id = R.drawable.ic_cart),
                                contentDescription = null,
                                tint = TextoEscuro,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Adicionar ao carrinho",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoEscuro
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Preview(
    name = "Produto Detalhe - Celular Real",
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_7
)
@Composable
fun ProdutoDetalheScreenPreview() {
    MyApplicationTheme {
        ProdutoDetalheScreen()
    }
}
