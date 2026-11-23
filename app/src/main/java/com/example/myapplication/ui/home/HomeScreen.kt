package com.example.myapplication.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.components.CircularCartBadge
import com.example.myapplication.VideoPlayer
import com.example.myapplication.data.model.Produto
import com.example.myapplication.ui.theme.AmareloDestaque
import com.example.myapplication.ui.theme.AzulEscuro
import com.example.myapplication.ui.theme.AzulPrimario
import com.example.myapplication.ui.theme.BordaCinza
import com.example.myapplication.ui.theme.BordaVerdeAgua
import com.example.myapplication.ui.theme.AzulSafiraProfundo
import com.example.myapplication.ui.theme.AzulOceano
import com.example.myapplication.ui.theme.OuroChampanhe
import com.example.myapplication.ui.theme.FundoNevoaAzul
import com.example.myapplication.ui.theme.FundoSoproVerde
import com.example.myapplication.ui.theme.FundoBranco
import com.example.myapplication.ui.theme.FundoPrincipal
import com.example.myapplication.ui.theme.FundoRosaMascote
import com.example.myapplication.ui.theme.FundoRosaBochechaSuave
import com.example.myapplication.ui.theme.FundoInput
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.TextoEscuro
import com.example.myapplication.ui.theme.TextoSecundario
import com.example.myapplication.ui.theme.VerdeAguaClaro
import com.example.myapplication.ui.theme.VerdeBotanico
import com.example.myapplication.ui.theme.VermelhoFaixa
import com.example.myapplication.util.WhatsAppHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class HomeTab(val label: String, val iconRes: Int) {
    INICIO("Início", R.drawable.ic_home),
    CATEGORIAS("Categorias", R.drawable.ic_categories),
    CARRINHO("Carrinho", R.drawable.ic_cart),
    PERFIL("Perfil", R.drawable.ic_person)
}

val produtosExemplo = listOf(
    Produto(
        id = "1",
        nome = "Limpador para Tapetes e Estofados",
        preco = 29.99,
        categoria = "Tecidos",
        imagemRes = R.drawable.produto_tapetes,
        descricao = "Limpeza profunda e perfume duradouro para estofados e tapetes.",
        fotosGaleria = listOf(
            R.drawable.produto_tapetes,
            R.drawable.banner_mascote_sala,
            R.drawable.banner_carrinho_revisar
        )
    ),
    Produto(
        id = "2",
        nome = "Limpador para Laminado",
        preco = 29.99,
        categoria = "Pisos",
        imagemRes = R.drawable.produto_laminado,
        descricao = "Brilho instantâneo e proteção contra umidade para pisos laminados.",
        fotosGaleria = listOf(
            R.drawable.produto_laminado,
            R.drawable.produto_detalhe_hero,
            R.drawable.banner_mascote_piso
        )
    ),
    Produto(
        id = "3",
        nome = "Limpador Porcelanato",
        preco = 34.99,
        categoria = "Pisos",
        imagemRes = R.drawable.produto_porcelanato,
        descricao = "Fórmula especializada que remove sujidades sem agredir o esmalte do porcelanato.",
        fotosGaleria = listOf(
            R.drawable.produto_porcelanato,
            R.drawable.banner_mascote_piso,
            R.drawable.produto_laminado
        )
    ),
    Produto(
        id = "4",
        nome = "Pano de Microfibra Limpeza",
        preco = 14.99,
        categoria = "Acessórios",
        imagemRes = R.drawable.cat_acessorios,
        descricao = "Tecido eletrostático de alta retenção de poeira e secagem rápida.",
        fotosGaleria = listOf(
            R.drawable.cat_acessorios,
            R.drawable.produto_tapetes,
            R.drawable.banner_mascote_sala
        )
    ),
    Produto(
        id = "5",
        nome = "Desengraxante Concentrado 1L",
        preco = 32.90,
        categoria = "Pisos",
        descricao = "Remove óleos, graxas e sujeiras pesadas sem agredir o piso."
    ),
    Produto(
        id = "6",
        nome = "Cera Auto Brilho Acrílica 1L",
        preco = 38.50,
        categoria = "Pisos",
        descricao = "Brilho espelhado e camada protetora antiderrapante de alta durabilidade."
    ),
    Produto(
        id = "7",
        nome = "Aromatizador de Ambientes 500ml",
        preco = 24.90,
        categoria = "Tecidos",
        descricao = "Fragrância suave e refrescante para salas, quartos e tecidos."
    ),
    Produto(
        id = "8",
        nome = "Removedor de Manchas para Tecidos",
        preco = 27.90,
        categoria = "Tecidos",
        descricao = "Ação rápida contra manchas difíceis em sofás, colchões e tapetes."
    ),
    Produto(
        id = "9",
        nome = "Mop Giratório Pro com Balde",
        preco = 79.90,
        categoria = "Acessórios",
        descricao = "Sistema de centrifugação 360° para limpeza rápida sem molhar as mãos."
    ),
    Produto(
        id = "10",
        nome = "Rodo Mágico com Esponja PVA",
        preco = 39.90,
        categoria = "Acessórios",
        descricao = "Alta absorção de líquidos e poeira com alavanca espremedora."
    )
)

enum class HomeFiltro {
    TODOS,
    PROMOCOES,
    MAIS_VENDIDOS
}

private fun String.removerAcentos(): String {
    val nfd = java.text.Normalizer.normalize(this, java.text.Normalizer.Form.NFD)
    return Regex("\\p{InCombiningDiacriticalMarks}+").replace(nfd, "")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    products: List<Produto> = produtosExemplo,
    cartItemCount: Int = 2,
    onProdutoClick: (Produto) -> Unit = {},
    onAdicionarAoCarrinho: (Produto) -> Unit = {},
    onNavigateTab: (HomeTab) -> Unit = {},
    onVoltar: (() -> Unit)? = null,
    showVideo: Boolean = true,
    onAdminAccess: () -> Unit = {},
    isDona: Boolean = false
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var selectedTab by remember { mutableStateOf(HomeTab.INICIO) }
    var searchQuery by remember { mutableStateOf("") }
    var filtroSelecionado by remember { mutableStateOf(HomeFiltro.TODOS) }
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    // Ao pressionar voltar no celular enquanto pesquisa, limpa a busca e volta para a vitrine
    BackHandler(enabled = searchQuery.isNotEmpty()) {
        searchQuery = ""
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    // 1. Filtragem reativa para a Home com busca inteligente sem diferenciação de acentos
    val (produtosExibidos, tituloSecao) = remember(products, filtroSelecionado, searchQuery) {
        val query = searchQuery.trim()
        if (query.isNotBlank()) {
            val queryLimpa = query.removerAcentos()
            val resultado = products.filter {
                it.nome.removerAcentos().contains(queryLimpa, ignoreCase = true) ||
                it.categoria.removerAcentos().contains(queryLimpa, ignoreCase = true) ||
                it.descricao.removerAcentos().contains(queryLimpa, ignoreCase = true)
            }
            val contagem = if (resultado.size == 1) "1 produto encontrado" else "${resultado.size} produtos encontrados"
            Pair(resultado, "Resultados para \"$query\" ($contagem)")
        } else {
            val baseFiltrada = when (filtroSelecionado) {
                HomeFiltro.TODOS -> products
                HomeFiltro.PROMOCOES -> products.filter { it.preco <= 30.0 || it.id in listOf("1", "4", "7", "8") }
                HomeFiltro.MAIS_VENDIDOS -> products.filter { it.id in listOf("1", "2", "3", "5", "6", "9") }
            }

            // Se a lista filtrada for menor que 6 (e sem busca ativa), completa com os outros produtos para preencher a tela
            val listaFinal = if (baseFiltrada.size < 6) {
                val outrosProdutos = products.filter { it !in baseFiltrada }
                (baseFiltrada + outrosProdutos).take(maxOf(6, products.size))
            } else {
                baseFiltrada
            }

            val titulo = when (filtroSelecionado) {
                HomeFiltro.PROMOCOES -> "Promoções em Destaque"
                HomeFiltro.MAIS_VENDIDOS -> "Mais Vendidos"
                else -> "Mais Procurados"
            }

            Pair(listaFinal, titulo)
        }
    }

    val bodyGradient = Brush.verticalGradient(
        colors = listOf(
            FundoPrincipal,
            FundoRosaMascote,
            FundoRosaBochechaSuave,
            FundoNevoaAzul,
            FundoSoproVerde
        )
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
                    .statusBarsPadding()
                    .shadow(12.dp, headerShape)
                    .clip(headerShape)
                    .background(headerGradient)
                    .drawBehind {
                        val fileteHeight = 1.5.dp.toPx()
                        val y = size.height - fileteHeight / 2f
                        drawLine(
                            color = OuroChampanhe,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = fileteHeight
                        )
                    },
                scrollBehavior = scrollBehavior,
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isDona) "Painel da Faxina" else "CASA DA FAXINA",
                            color = Color.White,
                            fontSize = if (isDona) 18.sp else 20.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = if (isDona) 0.2.sp else 1.2.sp,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.35f),
                                    offset = Offset(0f, 2f),
                                    blurRadius = 6f
                                )
                            )
                        )
                        Text(
                            text = if (isDona) "Gestão exclusiva • Casa da Faxina" else "PRODUTOS DE LIMPEZA",
                            color = if (isDona) OuroChampanhe.copy(alpha = 0.9f) else OuroChampanhe,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = if (isDona) 0.4.sp else 1.4.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = {
                                if (searchQuery.isNotEmpty()) {
                                    searchQuery = ""
                                    focusManager.clearFocus()
                                    keyboardController?.hide()
                                } else {
                                    WhatsAppHelper.abrirSuporte(context)
                                }
                            },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(
                                    id = if (searchQuery.isNotEmpty()) R.drawable.ic_arrow_back else R.drawable.ic_whatsapp
                                ),
                                contentDescription = if (searchQuery.isNotEmpty()) "Sair da busca" else "WhatsApp",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                actions = {
                    if (searchQuery.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = { WhatsAppHelper.abrirSuporte(context) },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_whatsapp),
                                    contentDescription = "WhatsApp",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            IconButton(
                                onClick = { onNavigateTab(HomeTab.CARRINHO) },
                                modifier = Modifier.size(40.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_cart),
                                    contentDescription = "Carrinho",
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                        }
                        CircularCartBadge(
                            count = cartItemCount,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = 0.dp, y = 2.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            HomeBottomBar(
                selectedTab = selectedTab,
                cartBadgeCount = cartItemCount,
                onTabSelected = {
                    selectedTab = it
                    onNavigateTab(it)
                }
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
            Spacer(modifier = Modifier.height(14.dp))

            // 1. Barra de Pesquisa Interativa (OutlinedTextField com estado reativo e botão de busca/limpar)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = {
                    Text(
                        text = "O que você precisa para sua casa?",
                        color = TextoSecundario,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    IconButton(onClick = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search),
                            contentDescription = "Buscar",
                            tint = AzulPrimario,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = {
                            searchQuery = ""
                            focusManager.clearFocus()
                            keyboardController?.hide()
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_close),
                                contentDescription = "Limpar busca",
                                tint = TextoSecundario,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(26.dp),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                        keyboardController?.hide()
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = FundoBranco,
                    unfocusedContainerColor = FundoBranco,
                    focusedBorderColor = AzulPrimario,
                    unfocusedBorderColor = BordaCinza,
                    focusedTextColor = TextoEscuro,
                    unfocusedTextColor = TextoEscuro
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onAdminAccess) {
                    Text(
                        text = "Painel da Dona (TESTE)",
                        color = AzulPrimario,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            val isBuscando = searchQuery.trim().isNotBlank()

            if (isBuscando) {
                Spacer(modifier = Modifier.height(14.dp))

                if (produtosExibidos.isEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = FundoBranco),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BordaVerdeAgua),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "🔍", fontSize = 40.sp)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Nenhum produto encontrado para \"$searchQuery\"",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoEscuro,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Tente pesquisar por 'laminado', 'tapete', 'porcelanato', 'microfibra' ou 'pisos'.",
                                fontSize = 12.sp,
                                color = TextoSecundario,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    val rows = produtosExibidos.chunked(2)
                    rows.forEach { pair ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ProdutoCard(
                                produto = pair[0],
                                modifier = Modifier.weight(1f),
                                onClick = { onProdutoClick(pair[0]) },
                                onAdicionarClick = { onAdicionarAoCarrinho(pair[0]) }
                            )
                            if (pair.size > 1) {
                                ProdutoCard(
                                    produto = pair[1],
                                    modifier = Modifier.weight(1f),
                                    onClick = { onProdutoClick(pair[1]) },
                                    onAdicionarClick = { onAdicionarAoCarrinho(pair[1]) }
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(14.dp))

                // 2. Vídeo de Apresentação com Alinhamento Perfeito do Título e Indicador
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(AmareloDestaque, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 7.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "VÍDEO",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = AzulEscuro
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Conheça a Casa da Faxina",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoEscuro
                            )
                        }

                        Text(
                            text = "Toque p/ pausar",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextoSecundario
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (showVideo) {
                        VideoPlayer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f),
                            videoResId = R.raw.video_home_apresentacao,
                            isLooping = true,
                            isMuted = false
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f)
                                .clip(RoundedCornerShape(16.dp))
                                .background(AzulPrimario),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Conheça a Casa da Faxina",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3. Atalhos Rápidos Lado a Lado: "Promoções" (amarelo) e "Mais Vendidos" (cinza/ativo)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val isPromoSelected = filtroSelecionado == HomeFiltro.PROMOCOES
                    val isMaisVendidosSelected = filtroSelecionado == HomeFiltro.MAIS_VENDIDOS

                    // Bloco 1 (Esquerda): Promoções (Amarelo Chamativo)
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(76.dp)
                            .shadow(
                                elevation = if (isPromoSelected) 6.dp else 3.dp,
                                shape = RoundedCornerShape(16.dp),
                                ambientColor = Color(0x18000000),
                                spotColor = AmareloDestaque.copy(alpha = 0.5f)
                            )
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, color = AzulEscuro),
                                onClick = {
                                    filtroSelecionado = if (isPromoSelected) HomeFiltro.TODOS else HomeFiltro.PROMOCOES
                                }
                            ),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = AmareloDestaque),
                        border = if (isPromoSelected) androidx.compose.foundation.BorderStroke(2.dp, AzulEscuro) else null
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Promoções",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextoEscuro
                                    )
                                    if (isPromoSelected) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "✓", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AzulEscuro)
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (isPromoSelected) "Filtro ativo" else "Ofertas imperdíveis",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextoEscuro.copy(alpha = 0.85f)
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(Color.White.copy(alpha = 0.6f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🏷️", fontSize = 17.sp)
                            }
                        }
                    }

                    // Bloco 2 (Direita): Mais Vendidos
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(76.dp)
                            .shadow(
                                elevation = if (isMaisVendidosSelected) 6.dp else 3.dp,
                                shape = RoundedCornerShape(16.dp),
                                ambientColor = Color(0x18000000),
                                spotColor = AzulPrimario.copy(alpha = 0.2f)
                            )
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, color = AzulPrimario),
                                onClick = {
                                    filtroSelecionado = if (isMaisVendidosSelected) HomeFiltro.TODOS else HomeFiltro.MAIS_VENDIDOS
                                }
                            ),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isMaisVendidosSelected) VerdeAguaClaro else FundoBranco
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            if (isMaisVendidosSelected) 2.dp else 1.dp,
                            if (isMaisVendidosSelected) AzulPrimario else BordaVerdeAgua
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Mais Vendidos",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = AzulEscuro
                                    )
                                    if (isMaisVendidosSelected) {
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(text = "✓", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = AzulPrimario)
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (isMaisVendidosSelected) "Filtro ativo" else "Os favoritos da loja",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = TextoSecundario
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(AzulPrimario.copy(alpha = 0.12f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "🔥", fontSize = 17.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // 4. Seção: Título + "Ver todos >" (Navega para a aba Categorias)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = tituloSecao,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextoEscuro
                    )
                    Text(
                        text = "Ver todos >",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AzulPrimario,
                        modifier = Modifier.clickable { onNavigateTab(HomeTab.CATEGORIAS) }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 5. Grade de Produtos (Exibe 6 a 8+ produtos em 2 colunas para preencher a tela)
                val rows = produtosExibidos.chunked(2)
                rows.forEach { pair ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ProdutoCard(
                            produto = pair[0],
                            modifier = Modifier.weight(1f),
                            onClick = { onProdutoClick(pair[0]) },
                            onAdicionarClick = { onAdicionarAoCarrinho(pair[0]) }
                        )
                        if (pair.size > 1) {
                            ProdutoCard(
                                produto = pair[1],
                                modifier = Modifier.weight(1f),
                                onClick = { onProdutoClick(pair[1]) },
                                onAdicionarClick = { onAdicionarAoCarrinho(pair[1]) }
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Card Destaque: Atendimento Direto com a Dona Sarinha (No final da rolagem)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(3.dp, RoundedCornerShape(16.dp))
                    .clickable {
                        WhatsAppHelper.abrirSuporte(context)
                    },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FundoBranco),
                border = androidx.compose.foundation.BorderStroke(1.dp, BordaVerdeAgua),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(Color(0xFF25D366).copy(alpha = 0.15f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_whatsapp),
                            contentDescription = "WhatsApp Sarinha",
                            tint = Color(0xFF25D366),
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Dúvidas? Fale com a Sarinha!",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoEscuro
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Ajuda personalizada para escolher o produto ideal para o seu piso ou tecido.",
                            fontSize = 12.sp,
                            color = TextoSecundario,
                            lineHeight = 16.sp
                        )
                    }

                    Text(
                        text = "Chamar >",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulPrimario
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

/**
 * Card de Produto adaptável a layouts em grade (2 colunas).
 */
@Composable
fun ProdutoArtwork(produto: Produto, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val uriBitmap by produceState<ImageBitmap?>(initialValue = null, key1 = produto.imagemUri) {
        value = produto.imagemUri?.let { uriString ->
            withContext(Dispatchers.IO) {
                runCatching {
                    loadProductBitmap(context, Uri.parse(uriString))?.asImageBitmap()
                }.getOrNull()
            }
        }
    }

    when {
        uriBitmap != null -> Image(
            bitmap = uriBitmap!!,
            contentDescription = produto.nome,
            modifier = modifier,
            contentScale = ContentScale.Fit
        )
        produto.imagemRes != 0 -> Image(
            painter = painterResource(id = produto.imagemRes),
            contentDescription = produto.nome,
            modifier = modifier,
            contentScale = ContentScale.Fit
        )
        else -> com.example.myapplication.components.ProdutoPlaceholderImage(
            categoria = produto.categoria,
            modifier = modifier
        )
    }
}

private fun loadProductBitmap(context: Context, uri: Uri): Bitmap? {
    val resolver = context.contentResolver
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
    if (bounds.outWidth <= 0 || bounds.outHeight <= 0) return null

    val sampleSize = maxOf(1, minOf(bounds.outWidth / 1024, bounds.outHeight / 1024))
        .let { Integer.highestOneBit(it) }
    val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
    return resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, options) }
}

@Composable
fun ProdutoCard(
    produto: Produto,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onAdicionarClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val productBorder = remember(colors.primary, colors.tertiary) {
        Brush.linearGradient(
            listOf(
                colors.tertiary.copy(alpha = 0.34f),
                colors.secondary.copy(alpha = 0.34f),
                Color.White.copy(alpha = 0.88f),
                colors.primary.copy(alpha = 0.38f),
                OuroChampanhe.copy(alpha = 0.52f)
            )
        )
    }

    Card(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = FundoBranco),
        border = androidx.compose.foundation.BorderStroke(1.dp, productBorder),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                ProdutoArtwork(
                    produto = produto,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(
                    text = "💬 Frete a combinar",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = AzulPrimario
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = produto.nome,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextoEscuro,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 17.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "R$ ${"%.2f".format(produto.preco).replace('.', ',')}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = VerdeBotanico
                    )

                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .shadow(2.dp, CircleShape)
                            .clip(CircleShape)
                            .background(AzulPrimario)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(bounded = true, color = AzulEscuro),
                                onClick = onAdicionarClick
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Adicionar ao carrinho",
                            tint = Color.White,
                            modifier = Modifier.size(19.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HomeBottomBar(
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
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = AzulPrimario,
                    selectedTextColor = AzulPrimario,
                    unselectedIconColor = TextoSecundario,
                    unselectedTextColor = TextoSecundario,
                    indicatorColor = VerdeAguaClaro
                )
            )
        }
    }
}

@Preview(
    name = "Home - Celular Real",
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_7
)
@Composable
fun HomeScreenPreview() {
    MyApplicationTheme {
        HomeScreen(showVideo = false)
    }
}
