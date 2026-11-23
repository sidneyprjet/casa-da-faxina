package com.example.myapplication.ui.categorias

import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
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
import com.example.myapplication.data.model.Produto
import com.example.myapplication.ui.home.HomeTab
import com.example.myapplication.ui.home.ProdutoCard
import com.example.myapplication.ui.theme.AmareloDestaque
import com.example.myapplication.ui.theme.AzulEscuro
import com.example.myapplication.ui.theme.AzulPrimario
import com.example.myapplication.ui.theme.BordaCinza
import com.example.myapplication.ui.theme.FundoBranco
import com.example.myapplication.ui.theme.FundoPrincipal
import com.example.myapplication.ui.theme.FundoRosaMascote
import com.example.myapplication.ui.theme.FundoRosaBochechaSuave
import com.example.myapplication.ui.theme.FundoNevoaAzul
import com.example.myapplication.ui.theme.FundoSoproVerde
import com.example.myapplication.ui.theme.AzulSafiraProfundo
import com.example.myapplication.ui.theme.AzulOceano
import com.example.myapplication.ui.theme.OuroChampanhe
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.TextoEscuro
import com.example.myapplication.ui.theme.TextoSecundario
import com.example.myapplication.ui.theme.VermelhoFaixa
import com.example.myapplication.util.WhatsAppHelper

// ─── Dados ───────────────────────────────────────────────────────────────────

val todosProdutosCategorias = listOf(
    Produto(
        id = "3",
        nome = "Limpador Porcelanato",
        preco = 34.99,
        categoria = "Pisos",
        imagemRes = R.drawable.produto_porcelanato,
        descricao = "Limpeza com máxima proteção para pisos nobres e porcelanatos.",
        fotosGaleria = listOf(R.drawable.produto_porcelanato, R.drawable.banner_mascote_piso, R.drawable.produto_laminado)
    ),
    Produto(
        id = "2",
        nome = "Limpador para Laminado",
        preco = 29.99,
        categoria = "Pisos",
        imagemRes = R.drawable.produto_laminado,
        descricao = "Brilho instantâneo e proteção contra umidade para pisos laminados.",
        fotosGaleria = listOf(R.drawable.produto_laminado, R.drawable.produto_detalhe_hero, R.drawable.banner_mascote_piso)
    ),
    Produto(
        id = "1",
        nome = "Limpador para Tapetes e Estofados",
        preco = 29.99,
        categoria = "Tecidos",
        imagemRes = R.drawable.produto_tapetes,
        descricao = "Limpeza profunda e perfume duradouro para estofados e tapetes.",
        fotosGaleria = listOf(R.drawable.produto_tapetes, R.drawable.banner_mascote_sala, R.drawable.banner_carrinho_revisar)
    ),
    Produto(
        id = "4",
        nome = "Pano de Microfibra Especial",
        preco = 14.99,
        categoria = "Acessórios",
        imagemRes = R.drawable.cat_acessorios,
        descricao = "Remoção de pó e secagem rápida sem soltar fiapos.",
        fotosGaleria = listOf(R.drawable.cat_acessorios, R.drawable.produto_tapetes, R.drawable.banner_mascote_sala)
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

// ─── Filtros da aba Categorias ────────────────────────────────────────────────

private val filtrosCategorias = listOf("Todos", "Pisos", "Tecidos", "Acessórios", "Promoções", "Mais Vendidos")

private fun String.removerAcentosCat(): String {
    val nfd = java.text.Normalizer.normalize(this, java.text.Normalizer.Form.NFD)
    return Regex("\\p{InCombiningDiacriticalMarks}+").replace(nfd, "")
}

// ─── Tela Principal ───────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriasScreen(
    modifier: Modifier = Modifier,
    products: List<Produto> = todosProdutosCategorias,
    cartItemCount: Int = 0,
    onProdutoClick: (Produto) -> Unit = {},
    onAdicionarAoCarrinho: (Produto) -> Unit = {},
    onNavigateTab: (HomeTab) -> Unit = {},
    onSearchClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    var searchQuery by remember { mutableStateOf("") }
    var filtroAtivo by remember { mutableStateOf("Todos") }

    // Limpa busca ao pressionar Voltar
    BackHandler(enabled = searchQuery.isNotEmpty()) {
        searchQuery = ""
        focusManager.clearFocus()
        keyboardController?.hide()
    }

    // Filtragem reativa — busca tem prioridade sobre filtro rápido
    val (produtosExibidos, tituloSecao) = remember(products, filtroAtivo, searchQuery) {
        val query = searchQuery.trim()
        if (query.isNotBlank()) {
            val queryLimpa = query.removerAcentosCat()
            val resultado = products.filter {
                it.nome.removerAcentosCat().contains(queryLimpa, ignoreCase = true) ||
                it.categoria.removerAcentosCat().contains(queryLimpa, ignoreCase = true) ||
                it.descricao.removerAcentosCat().contains(queryLimpa, ignoreCase = true)
            }
            val contagem = if (resultado.size == 1) "1 produto encontrado" else "${resultado.size} produtos encontrados"
            Pair(resultado, "Resultados para \"$query\" ($contagem)")
        } else {
            val lista = when (filtroAtivo) {
                "Pisos"        -> products.filter { it.categoria == "Pisos" }
                "Tecidos"      -> products.filter { it.categoria == "Tecidos" }
                "Acessórios"   -> products.filter { it.categoria == "Acessórios" }
                "Promoções"    -> products.filter { it.preco <= 30.0 }
                "Mais Vendidos"-> products.filter { it.id in listOf("1", "2", "3", "5", "6", "9") }
                else           -> products
            }
            val titulo = when (filtroAtivo) {
                "Todos"        -> "Todos os Produtos"
                "Promoções"    -> "Promoções em Destaque"
                "Mais Vendidos"-> "Mais Vendidos"
                else           -> "Categoria: $filtroAtivo"
            }
            Pair(lista, titulo)
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
            val colors = MaterialTheme.colorScheme
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
                            start = Offset(0f, lineY),
                            end = Offset(size.width, lineY),
                            strokeWidth = lineHeight
                        )
                    },
                scrollBehavior = scrollBehavior,
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "CASA DA FAXINA",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            textAlign = TextAlign.Center,
                            style = TextStyle(
                                shadow = Shadow(
                                    color = Color.Black.copy(alpha = 0.24f),
                                    offset = Offset(0f, 2f),
                                    blurRadius = 4f
                                )
                            )
                        )
                        Text(
                            text = "PRODUTOS DE LIMPEZA",
                            color = OuroChampanhe,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.1.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                },
                navigationIcon = {
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
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (searchQuery.isNotEmpty()) {
                                    R.drawable.ic_arrow_back
                                } else {
                                    R.drawable.ic_whatsapp
                                }
                            ),
                            contentDescription = if (searchQuery.isNotEmpty()) {
                                "Sair da busca"
                            } else {
                                "WhatsApp"
                            },
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    if (searchQuery.isNotEmpty()) {
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
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.size(48.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { onNavigateTab(HomeTab.CARRINHO) },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.15f))
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_cart),
                                contentDescription = "Carrinho",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        CircularCartBadge(
                            count = cartItemCount,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .offset(x = (-1).dp, y = 2.dp)
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
            CategoriasBottomBar(
                selectedTab = HomeTab.CATEGORIAS,
                cartBadgeCount = cartItemCount,
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
            Spacer(modifier = Modifier.height(14.dp))

            // ── 1. Barra de Pesquisa ──────────────────────────────────────────
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                placeholder = {
                    Text(
                        text = "Buscar em Categorias...",
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

            Spacer(modifier = Modifier.height(14.dp))

            // ── 2. Filtros rápidos em pílulas (só aparece quando não está buscando) ──
            if (searchQuery.isBlank()) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp)
                ) {
                    items(filtrosCategorias) { filtro ->
                        val isAtivo = filtro == filtroAtivo
                        Box(
                            modifier = Modifier
                                .shadow(if (isAtivo) 2.dp else 0.dp, RoundedCornerShape(20.dp))
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isAtivo) AzulPrimario else FundoBranco)
                                .border(
                                    width = 1.dp,
                                    color = if (isAtivo) AzulPrimario else Color(0xFFE2E8F0),
                                    shape = RoundedCornerShape(20.dp)
                                )
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = ripple(bounded = true, color = if (isAtivo) Color.White else AzulPrimario),
                                    onClick = { filtroAtivo = filtro }
                                )
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = filtro,
                                fontSize = 13.sp,
                                fontWeight = if (isAtivo) FontWeight.Bold else FontWeight.Medium,
                                color = if (isAtivo) Color.White else TextoEscuro
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            // ── 3. Título da seção + "Ver todos >" ───────────────────────────
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
                    modifier = Modifier.clickable { filtroAtivo = "Todos" }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ── 4. Grade 2 colunas de produtos ───────────────────────────────
            if (produtosExibidos.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = FundoBranco)
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
                            text = "Tente 'laminado', 'tapete', 'porcelanato' ou 'mop'.",
                            fontSize = 12.sp,
                            color = TextoSecundario,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                val rows = produtosExibidos.chunked(2)
                rows.forEach { par ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ProdutoCard(
                            produto = par[0],
                            modifier = Modifier.weight(1f),
                            onClick = { onProdutoClick(par[0]) },
                            onAdicionarClick = { onAdicionarAoCarrinho(par[0]) }
                        )
                        if (par.size > 1) {
                            ProdutoCard(
                                produto = par[1],
                                modifier = Modifier.weight(1f),
                                onClick = { onProdutoClick(par[1]) },
                                onAdicionarClick = { onAdicionarAoCarrinho(par[1]) }
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── 5. Card da Sarinha (atendimento WhatsApp) ─────────────────────
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(3.dp, RoundedCornerShape(16.dp))
                    .clickable { WhatsAppHelper.abrirSuporte(context) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = FundoBranco)
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

// ─── Bottom Bar ───────────────────────────────────────────────────────────────

@Composable
fun CategoriasBottomBar(
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
                    indicatorColor = Color(0xFFF3E8CB)
                )
            )
        }
    }
}

// ─── Preview ──────────────────────────────────────────────────────────────────

@Preview(
    name = "Categorias - Celular Real",
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_7
)
@Composable
fun CategoriasScreenPreview() {
    MyApplicationTheme {
        CategoriasScreen()
    }
}
