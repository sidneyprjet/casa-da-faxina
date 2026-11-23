package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.DatabaseHelper
import com.example.myapplication.data.local.SessionManager
import com.example.myapplication.data.local.toEntity
import com.example.myapplication.data.local.toProduto
import com.example.myapplication.data.model.Produto
import com.example.myapplication.data.model.Usuario
import com.example.myapplication.navigation.AppNavigation
import com.example.myapplication.ui.admin.AddProductScreen
import com.example.myapplication.ui.admin.AdminLoginScreen
import com.example.myapplication.ui.admin.AdminAuthViewModel
import com.example.myapplication.ui.admin.AdminMetricsScreen
import com.example.myapplication.ui.admin.AdminPanelScreen
import com.example.myapplication.ui.carrinho.ItemCarrinho
import com.example.myapplication.ui.home.produtosExemplo
import com.example.myapplication.ui.categorias.todosProdutosCategorias
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.map
import com.example.myapplication.ui.produto.produtoDetalheExemplo
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.util.WhatsAppHelper
import com.google.firebase.analytics.FirebaseAnalytics

enum class AppDestination {
    SPLASH,
    CADASTRO,
    ADMIN_LOGIN,
    ADMIN_PANEL,
    ADMIN_METRICS,
    ADD_PRODUCT,
    APP_MAIN // Abrange a navegação interna com NavHost (Home, Categorias, Carrinho, Detalhes, Perfil)
}

class MainActivity : ComponentActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var appDatabase: AppDatabase
    private var appSessionStartMillis = 0L
    private val averageUsageMillis = mutableLongStateOf(270_000L)

    private companion object {
        const val METRICS_PREFERENCES = "app_usage_metrics"
        const val TOTAL_USAGE_MILLIS = "total_usage_millis"
        const val USAGE_SESSIONS = "usage_sessions"
        const val EXAMPLE_AVERAGE_USAGE_MILLIS = 270_000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        configurarBarrasDoSistema()
        volumeControlStream = android.media.AudioManager.STREAM_MUSIC
        sessionManager = SessionManager(applicationContext)
        appDatabase = AppDatabase.getDatabase(applicationContext)
        lifecycleScope.launch(Dispatchers.IO) {
            val produtoDao = appDatabase.produtoDao()
            if (produtoDao.getProductCount() == 0) {
                val produtosIniciais = (produtosExemplo + todosProdutosCategorias)
                    .distinctBy { it.id }
                    .map { it.toEntity() }
                produtoDao.insertAll(produtosIniciais)
            }
        }

        val metricsPreferences = getSharedPreferences(METRICS_PREFERENCES, MODE_PRIVATE)
        val totalUsageMillis = metricsPreferences.getLong(TOTAL_USAGE_MILLIS, 0L)
        val usageSessions = metricsPreferences.getInt(USAGE_SESSIONS, 0)
        averageUsageMillis.longValue = if (usageSessions > 0) {
            totalUsageMillis / usageSessions
        } else {
            EXAMPLE_AVERAGE_USAGE_MILLIS
        }

        setContent {
            val adminAuthViewModel: AdminAuthViewModel = viewModel()
            MyApplicationTheme {
                val produtosFlow = remember(appDatabase) {
                    appDatabase.produtoDao().getAllProducts().map { entities ->
                        entities.map { it.toProduto() }
                    }
                }
                val produtosAdmin by produtosFlow.collectAsState(initial = emptyList())
                val adminAuthState by adminAuthViewModel.uiState.collectAsState()
                var currentScreen by remember { mutableStateOf(AppDestination.SPLASH) }
                var selectedProduto by remember { mutableStateOf(produtoDetalheExemplo) }
                var produtoEmEdicao by remember { mutableStateOf<Produto?>(null) }
                var usuarioAtivo by remember { mutableStateOf<Usuario?>(sessionManager.obterUsuario()) }
                var clienteCadastrado by remember {
                    mutableStateOf(sessionManager.existeContaCliente())
                }
                var clienteTemSenha by remember {
                    mutableStateOf(sessionManager.clienteTemSenha())
                }
                val nomeContaCliente = remember(clienteCadastrado) {
                    sessionManager.obterContaCliente()?.nome
                }
                val firebaseAnalytics = remember {
                    FirebaseAnalytics.getInstance(this@MainActivity)
                }

                LaunchedEffect(currentScreen) {
                    val screenName = when (currentScreen) {
                        AppDestination.SPLASH -> "Splash"
                        AppDestination.CADASTRO -> "Cadastro"
                        else -> null
                    }
                    screenName?.let { name ->
                        val parameters = Bundle().apply {
                            putString(FirebaseAnalytics.Param.SCREEN_NAME, name)
                            putString(FirebaseAnalytics.Param.SCREEN_CLASS, "MainActivity")
                        }
                        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, parameters)
                    }
                }

                // Carrinho de compras inicializado com o produto do layout
                val carrinhoItens = remember {
                    mutableStateListOf(
                        ItemCarrinho(produtosExemplo[1], 1)
                    )
                }
                val pageAccessCounts = remember {
                    mutableStateMapOf(
                        "Home" to 0,
                        "Categorias" to 0,
                        "Carrinho" to 0,
                        "Perfil" to 0
                    )
                }
                val productAddCounts = remember { mutableStateMapOf<String, Int>() }

                fun adicionarAoCarrinho(produto: Produto, qtd: Int = 1) {
                    productAddCounts[produto.id] = (productAddCounts[produto.id] ?: 0) + 1
                    val index = carrinhoItens.indexOfFirst { it.produto.id == produto.id }
                    if (index >= 0) {
                        carrinhoItens[index] = carrinhoItens[index].copy(
                            quantidade = carrinhoItens[index].quantidade + qtd
                        )
                    } else {
                        carrinhoItens.add(ItemCarrinho(produto, qtd))
                    }
                    Toast.makeText(
                        this,
                        "$qtd x ${produto.nome} adicionado ao carrinho!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                // Controle do botão Voltar nativo do Android
                BackHandler(enabled = currentScreen == AppDestination.SPLASH) {
                    finish()
                }

                BackHandler(enabled = currentScreen == AppDestination.CADASTRO) {
                    currentScreen = AppDestination.SPLASH
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Crossfade(targetState = currentScreen, label = "MainScreenTransition") { screen ->
                        when (screen) {
                            AppDestination.SPLASH -> {
                                SplashScreen(
                                    onNavigateNext = {
                                        // Sempre avança da 1ª página (Splash) diretamente para a 2ª página (Cadastro + Vídeo)
                                        currentScreen = AppDestination.CADASTRO
                                    }
                                )
                            }
                            AppDestination.CADASTRO -> {
                                CadastroScreen(
                                    mostrarEntrarSemLogin = !clienteCadastrado,
                                    contaClienteExiste = clienteCadastrado,
                                    clienteTemSenha = clienteTemSenha,
                                    nomeContaCliente = nomeContaCliente,
                                    onVoltar = {
                                        currentScreen = AppDestination.SPLASH
                                    },
                                    onCadastrarSucesso = { usuario ->
                                        sessionManager.salvarSessao(usuario)
                                        clienteCadastrado = true
                                        clienteTemSenha = sessionManager.clienteTemSenha()
                                        usuarioAtivo = usuario
                                        Toast.makeText(
                                            this,
                                            "Bem-vindo(a), ${usuario.nome}!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        currentScreen = AppDestination.APP_MAIN
                                    },
                                    onEntrarSemLoginSucesso = { visitante ->
                                        sessionManager.salvarSessao(visitante)
                                        usuarioAtivo = visitante
                                        Toast.makeText(
                                            this,
                                            "Entrando como visitante...",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        currentScreen = AppDestination.APP_MAIN
                                    },
                                )
                            }
                            AppDestination.ADMIN_LOGIN -> {
                                AdminLoginScreen(
                                    authViewModel = adminAuthViewModel,
                                    onLoginSuccess = {
                                        Toast.makeText(
                                            this,
                                            "Acesso administrativo confirmado.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        currentScreen = AppDestination.ADMIN_PANEL
                                    },
                                    onBackToStore = {
                                        currentScreen = AppDestination.APP_MAIN
                                    }
                                )
                            }
                            AppDestination.ADMIN_PANEL -> {
                                AdminPanelScreen(
                                    products = produtosAdmin,
                                    onBack = {
                                        currentScreen = AppDestination.APP_MAIN
                                    },
                                    onAddProduct = {
                                        produtoEmEdicao = null
                                        currentScreen = AppDestination.ADD_PRODUCT
                                    },
                                    onEditProduct = { produto ->
                                        produtoEmEdicao = produto
                                        currentScreen = AppDestination.ADD_PRODUCT
                                    },
                                    onDeleteProduct = { produto ->
                                        lifecycleScope.launch(Dispatchers.IO) {
                                            appDatabase.produtoDao().deleteById(produto.id)
                                        }
                                    },
                                    onViewMetrics = {
                                        currentScreen = AppDestination.ADMIN_METRICS
                                    },
                                    passwordChangeResult = adminAuthState.passwordChangeResult,
                                    passwordChangeBusy = adminAuthState.isBusy,
                                    onChangeMasterPassword = adminAuthViewModel::changeMasterPassword,
                                    onClearPasswordChangeResult = adminAuthViewModel::clearPasswordChangeResult
                                )
                            }
                            AppDestination.ADMIN_METRICS -> {
                                val topProducts = productAddCounts.entries
                                    .mapNotNull { (id, count) ->
                                        produtosAdmin.firstOrNull { it.id == id }?.nome?.let { nome ->
                                            nome to count
                                        }
                                    }
                                    .sortedByDescending { it.second }
                                    .take(3)

                                AdminMetricsScreen(
                                    pageAccessCounts = pageAccessCounts,
                                    topAddedProducts = topProducts,
                                    averageUsageMillis = averageUsageMillis.longValue,
                                    onBack = {
                                        currentScreen = AppDestination.ADMIN_PANEL
                                    }
                                )
                            }
                            AppDestination.ADD_PRODUCT -> {
                                AddProductScreen(
                                    initialProduct = produtoEmEdicao,
                                    onBack = {
                                        currentScreen = AppDestination.ADMIN_PANEL
                                    },
                                    onSaveProduct = { produto ->
                                        val editingExistingProduct = produtoEmEdicao != null
                                        lifecycleScope.launch(Dispatchers.IO) {
                                            val dao = appDatabase.produtoDao()
                                            if (editingExistingProduct) {
                                                dao.update(produto.toEntity())
                                            } else {
                                                dao.insert(produto.toEntity())
                                            }
                                        }
                                        produtoEmEdicao = null
                                    }
                                )
                            }
                            AppDestination.APP_MAIN -> {
                                // Navegação oficial e limpa com NavHost e NavController
                                val navController = rememberNavController()

                                AppNavigation(
                                    navController = navController,
                                    products = produtosAdmin,
                                    carrinhoItens = carrinhoItens,
                                    usuarioAtivo = usuarioAtivo,
                                    isDona = adminAuthState.isAuthenticated,
                                    selectedProduto = selectedProduto,
                                    onSelectProduto = { produto ->
                                        selectedProduto = produto
                                    },
                                    onAdicionarAoCarrinho = { produto, quantidade ->
                                        adicionarAoCarrinho(produto, quantidade)
                                    },
                                    onIncrementCarrinho = { item ->
                                        productAddCounts[item.produto.id] =
                                            (productAddCounts[item.produto.id] ?: 0) + 1
                                        val idx = carrinhoItens.indexOfFirst { it.produto.id == item.produto.id }
                                        if (idx >= 0) {
                                            carrinhoItens[idx] = item.copy(quantidade = item.quantidade + 1)
                                        }
                                    },
                                    onDecrementCarrinho = { item ->
                                        val idx = carrinhoItens.indexOfFirst { it.produto.id == item.produto.id }
                                        if (idx >= 0) {
                                            if (item.quantidade > 1) {
                                                carrinhoItens[idx] = item.copy(quantidade = item.quantidade - 1)
                                            } else {
                                                carrinhoItens.removeAt(idx)
                                            }
                                        }
                                    },
                                    onRemoverItemCarrinho = { item ->
                                        carrinhoItens.removeAll { it.produto.id == item.produto.id }
                                        Toast.makeText(this, "${item.produto.nome} removido", Toast.LENGTH_SHORT).show()
                                    },
                                    onConfirmarPedidoWhatsApp = { itensDoPedido ->
                                        WhatsAppHelper.enviarPedido(
                                            context = this@MainActivity,
                                            itens = itensDoPedido,
                                            usuario = usuarioAtivo
                                        )
                                    },
                                    onFalarComAtendente = {
                                        WhatsAppHelper.abrirSuporte(this@MainActivity)
                                    },
                                    onSairDaConta = {
                                        sessionManager.limparSessao()
                                        usuarioAtivo = null
                                        Toast.makeText(this, "Sessão encerrada com segurança", Toast.LENGTH_SHORT).show()
                                        currentScreen = AppDestination.CADASTRO
                                    },
                                    onExcluirDadosLgpd = {
                                        DatabaseHelper(this@MainActivity).limparTodosDados()
                                        sessionManager.limparSessao()
                                        sessionManager.limparContaCliente()
                                        clienteCadastrado = false
                                        clienteTemSenha = false
                                        usuarioAtivo = null
                                        Toast.makeText(
                                            this,
                                            "Todos os seus dados foram excluídos com sucesso (LGPD).",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        currentScreen = AppDestination.CADASTRO
                                    },
                                    onAdminAccess = {
                                        currentScreen = AppDestination.ADMIN_LOGIN
                                    },
                                    onScreenAccess = { screenName ->
                                        pageAccessCounts[screenName] =
                                            (pageAccessCounts[screenName] ?: 0) + 1
                                    },
                                    onSalvarDadosUsuario = { novoNome, novoTelefone ->
                                        val atualizado = usuarioAtivo?.copy(nome = novoNome, telefone = novoTelefone)
                                            ?: Usuario(nome = novoNome, telefone = novoTelefone)
                                        sessionManager.salvarSessao(atualizado)
                                        if (atualizado.id > 0) {
                                            DatabaseHelper(this@MainActivity).atualizarUsuario(
                                                atualizado.id,
                                                novoNome,
                                                novoTelefone
                                            )
                                        }
                                        usuarioAtivo = atualizado
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (appSessionStartMillis == 0L) {
            appSessionStartMillis = System.currentTimeMillis()
        }
        configurarBarrasDoSistema()
    }

    override fun onPause() {
        if (appSessionStartMillis != 0L) {
            val sessionDuration = (System.currentTimeMillis() - appSessionStartMillis).coerceAtLeast(0L)
            val preferences = getSharedPreferences(METRICS_PREFERENCES, MODE_PRIVATE)
            val totalDuration = preferences.getLong(TOTAL_USAGE_MILLIS, 0L) + sessionDuration
            val sessionCount = preferences.getInt(USAGE_SESSIONS, 0) + 1
            preferences.edit()
                .putLong(TOTAL_USAGE_MILLIS, totalDuration)
                .putInt(USAGE_SESSIONS, sessionCount)
                .apply()
            averageUsageMillis.longValue = totalDuration / sessionCount
            appSessionStartMillis = 0L
        }
        super.onPause()
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            configurarBarrasDoSistema()
        }
    }

    private fun configurarBarrasDoSistema() {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = false
            isAppearanceLightNavigationBars = true
        }
    }
}
