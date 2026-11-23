package com.example.myapplication.navigation

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.data.model.Produto
import com.example.myapplication.data.model.Usuario
import com.example.myapplication.ui.carrinho.CarrinhoScreen
import com.example.myapplication.ui.carrinho.ItemCarrinho
import com.example.myapplication.ui.categorias.CategoriasScreen
import com.example.myapplication.ui.home.HomeScreen
import com.example.myapplication.ui.home.HomeTab
import com.example.myapplication.ui.perfil.PerfilScreen
import com.example.myapplication.ui.produto.ProdutoDetalheScreen

/**
 * Definição centralizada das rotas do aplicativo Casa da Faxina.
 */
object AppRoutes {
    const val HOME = "home"
    const val CATEGORIAS = "categorias"
    const val CARRINHO = "carrinho"
    const val PERFIL = "perfil"
    const val PRODUTO_DETALHE = "detalhes_produto"
}

/**
 * Componente principal de navegação que gerencia o fluxo de telas do app.
 *
 * @param navController Controlador de navegação do Compose.
 * @param modifier Modificador opcional de layout.
 * @param startDestination Rota inicial (padrão: "home").
 * @param carrinhoItens Lista de itens adicionados ao carrinho.
 * @param usuarioAtivo Dados do usuário logado/visitante.
 * @param selectedProduto Produto selecionado para ver na tela de detalhes.
 * @param onSelectProduto Callback disparado quando o usuário clica em um produto.
 * @param onAdicionarAoCarrinho Callback para incluir produto no carrinho.
 * @param onIncrementCarrinho Callback para aumentar a quantidade de um item.
 * @param onDecrementCarrinho Callback para diminuir a quantidade de um item.
 * @param onRemoverItemCarrinho Callback para excluir um item do carrinho.
 * @param onConfirmarPedidoWhatsApp Callback para finalizar pedido no WhatsApp.
 * @param onFalarComAtendente Callback para abrir atendimento com a Sarinha.
 * @param onSairDaConta Callback para deslogar.
 * @param onExcluirDadosLgpd Callback para exclusão dos dados (LGPD).
 * @param onSalvarDadosUsuario Callback para atualizar perfil.
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    startDestination: String = AppRoutes.HOME,
    products: List<Produto>,
    carrinhoItens: List<ItemCarrinho>,
    usuarioAtivo: Usuario?,
    selectedProduto: Produto,
    onSelectProduto: (Produto) -> Unit,
    onAdicionarAoCarrinho: (Produto, Int) -> Unit,
    onIncrementCarrinho: (ItemCarrinho) -> Unit,
    onDecrementCarrinho: (ItemCarrinho) -> Unit,
    onRemoverItemCarrinho: (ItemCarrinho) -> Unit,
    onConfirmarPedidoWhatsApp: (List<ItemCarrinho>) -> Unit,
    onFalarComAtendente: () -> Unit,
    onSairDaConta: () -> Unit,
    onExcluirDadosLgpd: () -> Unit,
    onSalvarDadosUsuario: (String, String) -> Unit,
    onAdminAccess: () -> Unit = {},
    onScreenAccess: (String) -> Unit = {},
    isDona: Boolean = false,
) {
    val totalItensCarrinho = carrinhoItens.sumOf { it.quantidade }
    val context = LocalContext.current
    val analytics = remember(context) { FirebaseAnalytics.getInstance(context) }
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    LaunchedEffect(currentRoute) {
        val screen = when (currentRoute) {
            AppRoutes.HOME -> "Home" to "HomeScreen"
            AppRoutes.CATEGORIAS -> "Categorias" to "CategoriasScreen"
            AppRoutes.CARRINHO -> "Carrinho" to "CarrinhoScreen"
            AppRoutes.PERFIL -> "Perfil" to "PerfilScreen"
            AppRoutes.PRODUTO_DETALHE -> "Detalhes do produto" to "ProdutoDetalheScreen"
            else -> null
        }
        screen?.let { (screenName, screenClass) ->
            val parameters = Bundle().apply {
                putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
                putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
            }
            analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, parameters)
        }
    }

    // Função auxiliar para navegação fluida entre abas da barra inferior
    fun navegarParaAba(tab: HomeTab) {
        val rotaDestino = when (tab) {
            HomeTab.INICIO -> AppRoutes.HOME
            HomeTab.CATEGORIAS -> AppRoutes.CATEGORIAS
            HomeTab.CARRINHO -> AppRoutes.CARRINHO
            HomeTab.PERFIL -> AppRoutes.PERFIL
        }

        navController.navigate(rotaDestino) {
            // Evita empilhar telas repetidas no histórico (back stack)
            popUpTo(AppRoutes.HOME) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        // 1. Tela Inicial (Home)
        composable(AppRoutes.HOME) {
            LaunchedEffect(Unit) { onScreenAccess("Home") }
            HomeScreen(
                products = products,
                cartItemCount = totalItensCarrinho,
                onProdutoClick = { produto ->
                    onSelectProduto(produto)
                    navController.navigate(AppRoutes.PRODUTO_DETALHE)
                },
                onAdicionarAoCarrinho = { produto ->
                    onAdicionarAoCarrinho(produto, 1)
                },
                onNavigateTab = { tab ->
                    navegarParaAba(tab)
                },
                onAdminAccess = onAdminAccess,
                isDona = isDona
            )
        }

        // 2. Tela de Categorias
        composable(AppRoutes.CATEGORIAS) {
            LaunchedEffect(Unit) { onScreenAccess("Categorias") }
            CategoriasScreen(
                products = products,
                cartItemCount = totalItensCarrinho,
                onProdutoClick = { produto ->
                    onSelectProduto(produto)
                    navController.navigate(AppRoutes.PRODUTO_DETALHE)
                },
                onAdicionarAoCarrinho = { produto ->
                    onAdicionarAoCarrinho(produto, 1)
                },
                onNavigateTab = { tab ->
                    navegarParaAba(tab)
                }
            )
        }

        // 3. Tela do Carrinho de Compras
        composable(AppRoutes.CARRINHO) {
            LaunchedEffect(Unit) { onScreenAccess("Carrinho") }
            CarrinhoScreen(
                itens = carrinhoItens,
                onIncrementItem = onIncrementCarrinho,
                onDecrementItem = onDecrementCarrinho,
                onRemoverItem = onRemoverItemCarrinho,
                onVoltar = {
                    navController.popBackStack()
                },
                onConfirmarPedidoWhatsApp = onConfirmarPedidoWhatsApp,
                onNavigateTab = { tab ->
                    navegarParaAba(tab)
                }
            )
        }

        // 4. Tela de Detalhes do Produto
        composable(AppRoutes.PRODUTO_DETALHE) {
            ProdutoDetalheScreen(
                produto = selectedProduto,
                cartItemCount = totalItensCarrinho,
                onVoltar = {
                    navController.popBackStack()
                },
                onCartClick = {
                    navController.navigate(AppRoutes.CARRINHO)
                },
                onAdicionarAoCarrinho = { produto, quantidade ->
                    onAdicionarAoCarrinho(produto, quantidade)
                },
                onNavigateTab = { tab ->
                    navegarParaAba(tab)
                },
                onAdminAccess = onAdminAccess
            )
        }

        // 5. Tela de Perfil do Cliente
        composable(AppRoutes.PERFIL) {
            LaunchedEffect(Unit) { onScreenAccess("Perfil") }
            PerfilScreen(
                usuario = usuarioAtivo,
                cartItemCount = totalItensCarrinho,
                onNavigateTab = { tab ->
                    navegarParaAba(tab)
                },
                onFalarComAtendente = onFalarComAtendente,
                onSairDaConta = onSairDaConta,
                onExcluirDadosLgpd = onExcluirDadosLgpd,
                onSalvarDadosUsuario = onSalvarDadosUsuario,
                onAdminAccess = onAdminAccess
            )
        }
    }
}
