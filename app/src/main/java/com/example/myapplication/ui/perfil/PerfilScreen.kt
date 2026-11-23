package com.example.myapplication.ui.perfil

import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.R
import com.example.myapplication.components.CasaDaFaxinaHeader
import com.example.myapplication.components.CircularCartBadge
import com.example.myapplication.data.model.Usuario
import com.example.myapplication.ui.home.HomeBottomBar
import com.example.myapplication.ui.home.HomeTab
import com.example.myapplication.ui.theme.AmareloDestaque
import com.example.myapplication.ui.theme.AzulEscuro
import com.example.myapplication.ui.theme.AzulSafiraProfundo
import com.example.myapplication.ui.theme.AzulOceano
import com.example.myapplication.ui.theme.AzulPrimario
import com.example.myapplication.ui.theme.FundoBranco
import com.example.myapplication.ui.theme.FundoPrincipal
import com.example.myapplication.ui.theme.FundoRosaMascote
import com.example.myapplication.ui.theme.FundoRosaBochechaSuave
import com.example.myapplication.ui.theme.FundoNevoaAzul
import com.example.myapplication.ui.theme.FundoSoproVerde
import com.example.myapplication.ui.theme.OuroChampanhe
import com.example.myapplication.ui.theme.FundoInput
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.TextoEscuro
import com.example.myapplication.ui.theme.TextoSecundario
import com.example.myapplication.ui.theme.VermelhoFaixa

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    modifier: Modifier = Modifier,
    usuario: Usuario? = null,
    cartItemCount: Int = 2,
    onNavigateTab: (HomeTab) -> Unit = {},
    onFalarComAtendente: () -> Unit = {},
    onSairDaConta: () -> Unit = {},
    onSalvarDadosUsuario: (String, String) -> Unit = { _, _ -> },
    onExcluirDadosLgpd: () -> Unit = {},
    onAdminAccess: () -> Unit = {}
) {
    val context = LocalContext.current
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
    var logoTapCount by remember { mutableStateOf(0) }

    var nomeAtual by remember { mutableStateOf(usuario?.nome ?: "Nome da Loja") }
    var telefoneAtual by remember { mutableStateOf(usuario?.telefone ?: "(00) 0000-0000") }

    var mostrarModalTrocaFoto by remember { mutableStateOf(false) }
    var mostrarModalEditarPerfil by remember { mutableStateOf(false) }
    var mostrarModalLgpd by remember { mutableStateOf(false) }
    var mostrarConfirmacaoExclusaoLgpd by remember { mutableStateOf(false) }

    var avatarCustomBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var avatarDrawableRes by remember { mutableStateOf<Int?>(null) }

    val inicial = nomeAtual.firstOrNull()?.uppercaseChar()?.toString() ?: "S"

    // Seletor de Foto da Galeria Nativa com Tratamento Seguro
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                context.contentResolver.openInputStream(uri)?.use { stream ->
                    val bmp = BitmapFactory.decodeStream(stream)
                    if (bmp != null) {
                        avatarCustomBitmap = bmp.asImageBitmap()
                        avatarDrawableRes = null
                        Toast.makeText(context, "Foto de perfil atualizada!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (_: Exception) {
                Toast.makeText(context, "Erro ao carregar imagem selecionada", Toast.LENGTH_SHORT).show()
            }
        }
    }

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
                    Column(
                        modifier = Modifier.clickable {
                            logoTapCount++
                            if (logoTapCount >= 5) {
                                logoTapCount = 0
                                onAdminAccess()
                            }
                        },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "CASA DA FAXINA",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "MEU PERFIL",
                            color = OuroChampanhe,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            letterSpacing = 1.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { onNavigateTab(HomeTab.INICIO) },
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
                    Box(
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(
                            onClick = { onNavigateTab(HomeTab.CARRINHO) },
                            modifier = Modifier.size(40.dp)
                        ) {
                            BadgedBox(
                                badge = {
                                    CircularCartBadge(
                                        count = cartItemCount,
                                        modifier = Modifier.offset(x = 6.dp, y = (-2).dp)
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
                }
            )
        },
        bottomBar = {
            HomeBottomBar(
                selectedTab = HomeTab.PERFIL,
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
            Spacer(modifier = Modifier.height(16.dp))

            // Card Principal de Identidade do Cliente
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(3.dp, RoundedCornerShape(18.dp)),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = FundoBranco)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar Interativo (Tocar para Trocar a Foto)
                    Box(contentAlignment = Alignment.BottomEnd) {
                        Box(
                            modifier = Modifier
                                .size(84.dp)
                                .shadow(4.dp, CircleShape)
                                .clip(CircleShape)
                                .background(AzulPrimario, CircleShape)
                                .clickable { mostrarModalTrocaFoto = true },
                            contentAlignment = Alignment.Center
                        ) {
                            if (avatarCustomBitmap != null) {
                                Image(
                                    bitmap = avatarCustomBitmap!!,
                                    contentDescription = "Foto do cliente",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.Center
                                )
                            } else if (avatarDrawableRes != null) {
                                Image(
                                    painter = painterResource(id = avatarDrawableRes!!),
                                    contentDescription = "Avatar selecionado",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.Center
                                )
                            } else {
                                Text(
                                    text = inicial,
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Botãozinho no canto do avatar para troca rápida
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(AmareloDestaque, CircleShape)
                                .border(2.dp, Color.White, CircleShape)
                                .clip(CircleShape)
                                .clickable { mostrarModalTrocaFoto = true },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "📷",
                                fontSize = 13.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = nomeAtual,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulEscuro
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = telefoneAtual,
                        fontSize = 14.sp,
                        color = TextoSecundario,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Botãozinho sutil "Trocar foto de perfil"
                    Text(
                        text = "Toque na foto para trocar",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AzulPrimario,
                        modifier = Modifier.clickable { mostrarModalTrocaFoto = true }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Badge de Segurança da Conta
                    Row(
                        modifier = Modifier
                            .background(Color(0xFFE8F5E9), RoundedCornerShape(20.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_security),
                            contentDescription = null,
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Conta Segura e Protegida",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Seção de Contato Direto com a Dona
            Text(
                text = "Atendimento e Contato",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AzulEscuro,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Card Oficial WhatsApp Sarinha
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(2.dp, RoundedCornerShape(14.dp))
                    .clickable(onClick = onFalarComAtendente),
                shape = RoundedCornerShape(14.dp),
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
                            contentDescription = "WhatsApp",
                            tint = Color(0xFF25D366),
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Falar com a Sarinha (Dona)",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoEscuro
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "+55 (00) 0000-0000 • Atendimento humanizado",
                            fontSize = 12.sp,
                            color = TextoSecundario
                        )
                    }

                    Text(
                        text = "Conversar >",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulPrimario
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Seção Segurança do Sistema & LGPD
            Text(
                text = "Privacidade e LGPD em Dia",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = AzulEscuro,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .shadow(1.dp, RoundedCornerShape(14.dp)),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = FundoBranco)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_security),
                            contentDescription = "Cadeado de Proteção",
                            tint = AzulPrimario,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Conformidade Total com a LGPD",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextoEscuro
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Em cumprimento à Lei Geral de Proteção de Dados (Lei nº 13.709/2018), suas informações são armazenadas exclusivamente no banco seguro local do seu dispositivo móvel. Sem rastreadores, sem custos e sem compartilhamento com terceiros.",
                        fontSize = 12.sp,
                        color = TextoSecundario,
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Ver termos de privacidade",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AzulPrimario,
                            modifier = Modifier.clickable { mostrarModalLgpd = true }
                        )

                        Text(
                            text = "Excluir meus dados",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = VermelhoFaixa,
                            modifier = Modifier.clickable { mostrarConfirmacaoExclusaoLgpd = true }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Ações: Editar Dados e Sair da Conta
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { mostrarModalEditarPerfil = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulPrimario,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Editar Meus Dados",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onSairDaConta,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFFD32F2F)
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logout),
                            contentDescription = "Sair",
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sair da Conta",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Modal de Troca de Foto de Perfil
    if (mostrarModalTrocaFoto) {
        AlertDialog(
            onDismissRequest = { mostrarModalTrocaFoto = false },
            title = {
                Text(
                    text = "Escolha sua Foto de Perfil",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulEscuro
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Selecione uma foto da sua galeria ou um dos ícones temáticos da Casa da Faxina:",
                        fontSize = 13.sp,
                        color = TextoEscuro
                    )

                    // Opção Galeria
                    Button(
                        onClick = {
                            mostrarModalTrocaFoto = false
                            galleryLauncher.launch("image/*")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
                    ) {
                        Text("📁 Escolher da Galeria do Celular", fontWeight = FontWeight.Bold)
                    }

                    Text(
                        text = "Ou escolha um avatar temático:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextoSecundario
                    )

                    // Grade com 3 opções temáticas
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Mascote
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                avatarDrawableRes = R.drawable.banner_mascote_sala
                                avatarCustomBitmap = null
                                mostrarModalTrocaFoto = false
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(FundoInput),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.banner_mascote_sala),
                                    contentDescription = "Mascote",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Mascote", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }

                        // Limpador Laminado
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                avatarDrawableRes = R.drawable.produto_laminado
                                avatarCustomBitmap = null
                                mostrarModalTrocaFoto = false
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(FundoInput),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.produto_laminado),
                                    contentDescription = "Laminado",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Laminado", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }

                        // Limpador Tapetes
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable {
                                avatarDrawableRes = R.drawable.produto_tapetes
                                avatarCustomBitmap = null
                                mostrarModalTrocaFoto = false
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(54.dp)
                                    .clip(CircleShape)
                                    .background(FundoInput),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.produto_tapetes),
                                    contentDescription = "Tapetes",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Tapetes", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    }

                    // Restaurar Inicial Padrão
                    OutlinedButton(
                        onClick = {
                            avatarDrawableRes = null
                            avatarCustomBitmap = null
                            mostrarModalTrocaFoto = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Restaurar Inicial Padrão", color = TextoEscuro)
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                OutlinedButton(onClick = { mostrarModalTrocaFoto = false }) {
                    Text("Cancelar", color = TextoEscuro)
                }
            },
            containerColor = FundoBranco,
            shape = RoundedCornerShape(18.dp)
        )
    }

    // Modal de Edição de Dados do Usuário
    if (mostrarModalEditarPerfil) {
        var tempNome by remember { mutableStateOf(nomeAtual) }
        var tempTelefone by remember { mutableStateOf(telefoneAtual) }

        AlertDialog(
            onDismissRequest = { mostrarModalEditarPerfil = false },
            title = {
                Text(
                    text = "Editar Dados Cadastrais",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulEscuro
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Mantenha seus dados atualizados para combinar as entregas com a Sarinha:",
                        fontSize = 13.sp,
                        color = TextoSecundario
                    )

                    OutlinedTextField(
                        value = tempNome,
                        onValueChange = { tempNome = it },
                        label = { Text("Seu Nome") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = tempTelefone,
                        onValueChange = { tempTelefone = it },
                        label = { Text("Seu Telefone / WhatsApp") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (tempNome.isNotBlank()) {
                            nomeAtual = tempNome
                            telefoneAtual = tempTelefone
                            onSalvarDadosUsuario(tempNome, tempTelefone)
                            mostrarModalEditarPerfil = false
                            Toast.makeText(context, "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
                ) {
                    Text("Salvar Alterações", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { mostrarModalEditarPerfil = false }) {
                    Text("Cancelar", color = TextoEscuro)
                }
            },
            containerColor = FundoBranco,
            shape = RoundedCornerShape(18.dp)
        )
    }

    // Modal de Informações e Transparência da LGPD (Lei nº 13.709/2018)
    if (mostrarModalLgpd) {
        AlertDialog(
            onDismissRequest = { mostrarModalLgpd = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_security),
                        contentDescription = "Segurança",
                        tint = AzulPrimario,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Privacidade & LGPD",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AzulEscuro
                    )
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = "A Casa da Faxina preza pela total transparência e proteção dos seus dados pessoais:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextoEscuro
                    )

                    Text(
                        text = "• 100% Local: Seus dados (nome, telefone e foto) são armazenados estritamente na memória protegida do seu dispositivo.",
                        fontSize = 12.sp,
                        color = TextoSecundario,
                        lineHeight = 16.sp
                    )

                    Text(
                        text = "• Sem Custos & Sem Venda de Dados: O aplicativo não utiliza rastreadores de publicidade e nunca comercializa ou repassa seus dados para terceiros.",
                        fontSize = 12.sp,
                        color = TextoSecundario,
                        lineHeight = 16.sp
                    )

                    Text(
                        text = "• Seus Direitos (Art. 18 LGPD): Você pode editar seus dados ou solicitar a exclusão definitiva a qualquer momento nesta tela.",
                        fontSize = 12.sp,
                        color = TextoSecundario,
                        lineHeight = 16.sp
                    )

                    Text(
                        text = "• Atendimento Direto: Todo fechamento de pedido é realizado diretamente com a proprietária Sarinha via WhatsApp oficial.",
                        fontSize = 12.sp,
                        color = TextoSecundario,
                        lineHeight = 16.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { mostrarModalLgpd = false },
                    colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario)
                ) {
                    Text("Entendido", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            containerColor = FundoBranco,
            shape = RoundedCornerShape(18.dp)
        )
    }

    // Modal de Confirmação de Exclusão Definitiva de Dados (Art. 18 LGPD)
    if (mostrarConfirmacaoExclusaoLgpd) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacaoExclusaoLgpd = false },
            title = {
                Text(
                    text = "Excluir Meus Dados Locais?",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFD32F2F)
                )
            },
            text = {
                Text(
                    text = "Em conformidade com a LGPD, esta ação apagará permanentemente seu cadastro e sua sessão local deste celular. Para fazer novos pedidos, será necessário se cadastrar novamente.\n\nDeseja prosseguir?",
                    fontSize = 13.sp,
                    color = TextoEscuro,
                    lineHeight = 18.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarConfirmacaoExclusaoLgpd = false
                        onExcluirDadosLgpd()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F))
                ) {
                    Text("Sim, Excluir Meus Dados", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { mostrarConfirmacaoExclusaoLgpd = false }) {
                    Text("Cancelar", color = TextoEscuro)
                }
            },
            containerColor = FundoBranco,
            shape = RoundedCornerShape(18.dp)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PerfilScreenPreview() {
    MyApplicationTheme {
        PerfilScreen()
    }
}
