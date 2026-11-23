package com.example.myapplication

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.model.Usuario
import com.example.myapplication.ui.cadastro.CadastroUiState
import com.example.myapplication.ui.cadastro.CadastroViewModel
import com.example.myapplication.ui.theme.AzulEscuro
import com.example.myapplication.ui.theme.AzulPrimario
import com.example.myapplication.ui.theme.BordaCinza
import com.example.myapplication.ui.theme.FundoBranco
import com.example.myapplication.ui.theme.FundoInput
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.TextoEscuro
import com.example.myapplication.ui.theme.TextoSecundario
import com.example.myapplication.ui.theme.VermelhoFaixa

@Composable
fun CadastroScreen(
    modifier: Modifier = Modifier,
    viewModel: CadastroViewModel = viewModel(),
    onVoltar: () -> Unit = {},
    onCadastrarSucesso: (Usuario) -> Unit = {},
    onEntrarSemLoginSucesso: (Usuario) -> Unit = {},
    mostrarEntrarSemLogin: Boolean = true,
    contaClienteExiste: Boolean = false,
    clienteTemSenha: Boolean = false,
    nomeContaCliente: String? = null
) {
    val uiState by viewModel.uiState.collectAsState()

    CadastroContent(
        modifier = modifier,
        uiState = uiState,
        onVoltar = onVoltar,
        onNomeChange = viewModel::onNomeChange,
        onTelefoneChange = viewModel::onTelefoneChange,
        onSenhaChange = viewModel::onSenhaChange,
        onConfirmarSenhaChange = viewModel::onConfirmarSenhaChange,
        onCadastrar = { viewModel.cadastrar(onSuccess = onCadastrarSucesso) },
        onLoginCliente = { viewModel.entrarCliente(onSuccess = onCadastrarSucesso) },
        onDefinirSenhaCliente = { viewModel.definirSenhaCliente(onSuccess = onCadastrarSucesso) },
        onEntrarSemLogin = { viewModel.entrarComoVisitante(onSuccess = onEntrarSemLoginSucesso) },
        mostrarEntrarSemLogin = mostrarEntrarSemLogin,
        contaClienteExiste = contaClienteExiste,
        clienteTemSenha = clienteTemSenha,
        nomeContaCliente = nomeContaCliente
    )
}

@Composable
fun CadastroContent(
    modifier: Modifier = Modifier,
    uiState: CadastroUiState,
    onVoltar: () -> Unit = {},
    onNomeChange: (String) -> Unit = {},
    onTelefoneChange: (String) -> Unit = {},
    onCadastrar: () -> Unit = {},
    onEntrarSemLogin: () -> Unit = {},
    mostrarEntrarSemLogin: Boolean = true,
    contaClienteExiste: Boolean = false,
    clienteTemSenha: Boolean = false,
    nomeContaCliente: String? = null,
    onSenhaChange: (String) -> Unit = {},
    onConfirmarSenhaChange: (String) -> Unit = {},
    onLoginCliente: () -> Unit = {},
    onDefinirSenhaCliente: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val telefoneVisualTransformation = remember { BrazilianMobileVisualTransformation() }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        containerColor = FundoBranco,
        topBar = {
            // Cabeçalho Padrão Oficial Azul da 2ª Página (Login + Vídeo)
            CadastroTopHeader(onVoltar = onVoltar)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Vídeo de apresentação em proporção nativa 16:9 estilizado e com cantos arredondados
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
            ) {
                VideoPlayer(
                    videoResId = R.raw.gemini_generated_video_c375a206,
                    modifier = Modifier.fillMaxSize(),
                    isLooping = true,
                    isMuted = false
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Cabeçalho / Títulos
            Text(
                text = if (contaClienteExiste) "Bem-vindo(a) de volta" else "Crie sua conta",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = AzulEscuro,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = if (contaClienteExiste) nomeContaCliente ?: "Entre com sua senha para continuar" else "Vamos cuidar da sua casa?",
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal,
                color = TextoSecundario,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (contaClienteExiste) {
                if (!clienteTemSenha) {
                    Text(
                        text = "Primeiro acesso: crie uma senha para proteger sua conta.",
                        color = TextoSecundario,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
                CampoSenhaCliente(
                    value = uiState.senha,
                    onValueChange = onSenhaChange,
                    label = if (clienteTemSenha) "Senha" else "Crie uma senha",
                    error = uiState.senhaErro,
                    imeAction = if (clienteTemSenha) ImeAction.Done else ImeAction.Next
                )
                if (!clienteTemSenha) {
                    Spacer(modifier = Modifier.height(10.dp))
                    CampoSenhaCliente(
                        value = uiState.confirmarSenha,
                        onValueChange = onConfirmarSenhaChange,
                        label = "Confirme a senha",
                        error = uiState.confirmarSenhaErro,
                        imeAction = ImeAction.Done
                    )
                }
                uiState.mensagemErro?.let { erro ->
                    Text(erro, color = VermelhoFaixa, fontSize = 13.sp, modifier = Modifier.padding(top = 8.dp))
                }
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = if (clienteTemSenha) onLoginCliente else onDefinirSenhaCliente,
                    enabled = !uiState.isLoading && uiState.senha.length >= 6 &&
                        (clienteTemSenha || uiState.senha == uiState.confirmarSenha),
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulPrimario,
                        disabledContainerColor = AzulPrimario.copy(alpha = 0.6f)
                    )
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(22.dp), strokeWidth = 2.dp)
                    } else {
                        Text(
                            text = if (clienteTemSenha) "Entrar" else "Salvar senha e entrar",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            } else {
            // Campo: Nome Completo
            OutlinedTextField(
                value = uiState.nome,
                onValueChange = onNomeChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Nome completo", color = Color.DarkGray, fontSize = 15.sp)
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_person),
                        contentDescription = "Ícone Usuário",
                        tint = if (uiState.nomeErro != null) VermelhoFaixa else TextoSecundario,
                        modifier = Modifier.size(22.dp)
                    )
                },
                isError = uiState.nomeErro != null,
                supportingText = {
                    uiState.nomeErro?.let { erro ->
                        Text(
                            text = erro,
                            color = VermelhoFaixa,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = FundoInput,
                    unfocusedContainerColor = FundoInput,
                    disabledContainerColor = FundoInput,
                    errorContainerColor = FundoInput,
                    focusedBorderColor = AzulPrimario,
                    unfocusedBorderColor = Color.Transparent,
                    errorBorderColor = VermelhoFaixa,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                    errorTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Campo: Telefone / WhatsApp (com máscara automática)
            OutlinedTextField(
                value = uiState.telefone,
                onValueChange = onTelefoneChange,
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = telefoneVisualTransformation,
                placeholder = {
                    Text(text = "Telefone / WhatsApp", color = Color.DarkGray, fontSize = 15.sp)
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_phone),
                        contentDescription = "Ícone Telefone WhatsApp",
                        tint = if (uiState.telefoneErro != null) VermelhoFaixa else TextoSecundario,
                        modifier = Modifier.size(20.dp)
                    )
                },
                isError = uiState.telefoneErro != null,
                supportingText = {
                    uiState.telefoneErro?.let { erro ->
                        Text(
                            text = erro,
                            color = VermelhoFaixa,
                            fontSize = 12.sp,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        onCadastrar()
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = FundoInput,
                    unfocusedContainerColor = FundoInput,
                    disabledContainerColor = FundoInput,
                    errorContainerColor = FundoInput,
                    focusedBorderColor = AzulPrimario,
                    unfocusedBorderColor = Color.Transparent,
                    errorBorderColor = VermelhoFaixa,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                    errorTextColor = Color.Black
                )
            )

            Spacer(modifier = Modifier.height(10.dp))
            CampoSenhaCliente(
                value = uiState.senha,
                onValueChange = onSenhaChange,
                label = "Crie uma senha (mínimo 6 caracteres)",
                error = uiState.senhaErro,
                imeAction = ImeAction.Next
            )
            Spacer(modifier = Modifier.height(10.dp))
            CampoSenhaCliente(
                value = uiState.confirmarSenha,
                onValueChange = onConfirmarSenhaChange,
                label = "Confirme sua senha",
                error = uiState.confirmarSenhaErro,
                imeAction = ImeAction.Done
            )
            Spacer(modifier = Modifier.height(24.dp))

            // Botão Principal: Cadastrar (com indicador de carregamento)
            Button(
                onClick = onCadastrar,
                enabled = !uiState.isLoading && uiState.telefoneValido &&
                    uiState.senha.length >= 6 && uiState.senha == uiState.confirmarSenha,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AzulPrimario,
                    disabledContainerColor = AzulPrimario.copy(alpha = 0.6f)
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(22.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Cadastrar",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (mostrarEntrarSemLogin && !contaClienteExiste) {
                // Acesso de visitante somente antes de existir uma conta cadastrada.
                OutlinedButton(
                    onClick = onEntrarSemLogin,
                    enabled = !uiState.isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, BordaCinza),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Transparent
                    )
                ) {
                    Text(
                        text = "Entrar sem login",
                        color = TextoEscuro,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            }

            // Selo de Privacidade e LGPD
            Row(
                modifier = Modifier
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_security),
                    contentDescription = "Proteção LGPD",
                    tint = AzulPrimario,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Dados salvos com segurança no seu celular (LGPD)",
                    fontSize = 11.sp,
                    color = TextoSecundario,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun CadastroTopHeader(
    onVoltar: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(AzulPrimario)
            .statusBarsPadding()
            .padding(horizontal = 12.dp, vertical = 10.dp)
    ) {
        IconButton(
            onClick = onVoltar,
            modifier = Modifier
                .size(44.dp)
                .align(Alignment.CenterStart)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = "Voltar",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun CampoSenhaCliente(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    error: String?,
    imeAction: ImeAction
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label, color = Color.DarkGray) },
        singleLine = true,
        visualTransformation = PasswordVisualTransformation(),
        isError = error != null,
        supportingText = {
            error?.let { Text(it, color = VermelhoFaixa, fontSize = 12.sp) }
        },
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = FundoInput,
            unfocusedContainerColor = FundoInput,
            focusedBorderColor = AzulPrimario,
            unfocusedBorderColor = Color.Transparent,
            errorBorderColor = VermelhoFaixa,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            errorTextColor = Color.Black,
            focusedLabelColor = AzulPrimario,
            unfocusedLabelColor = Color.DarkGray
        )
    )
}

private class BrazilianMobileVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digits = text.text.filter(Char::isDigit).take(11)
        val formatted = when {
            digits.isEmpty() -> ""
            digits.length == 1 -> "(${digits}"
            digits.length == 2 -> "(${digits})"
            digits.length <= 7 -> "(${digits.take(2)}) ${digits.drop(2)}"
            else -> "(${digits.take(2)}) ${digits.substring(2, 7)}-${digits.drop(7)}"
        }

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val safeOffset = offset.coerceIn(0, digits.length)
                val mapped = when {
                    safeOffset == 0 -> 0
                    safeOffset == 1 -> 2
                    safeOffset == 2 -> if (digits.length >= 3) 5 else formatted.length
                    safeOffset <= 7 -> safeOffset + 3
                    else -> safeOffset + 4
                }
                return mapped.coerceIn(0, formatted.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                val safeOffset = offset.coerceIn(0, formatted.length)
                val mapped = when {
                    safeOffset <= 1 -> 0
                    safeOffset <= 2 -> 1
                    safeOffset <= 5 -> 2
                    safeOffset <= 10 -> safeOffset - 3
                    safeOffset <= 11 -> 7
                    else -> safeOffset - 4
                }
                return mapped.coerceIn(0, digits.length)
            }
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CadastroScreenPreview() {
    MyApplicationTheme {
        CadastroContent(
            uiState = CadastroUiState(
                nome = "Nome da Loja",
                telefone = "00000000000"
            )
        )
    }
}
