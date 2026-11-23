package com.example.myapplication.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

private val AzulAdmin = Color(0xFF0047BB)
private val AmareloAdmin = Color(0xFFFFD500)
private val FundoAdmin = Color(0xFFF3F4F6)

@Composable
fun AdminLoginScreen(
    authViewModel: AdminAuthViewModel,
    onLoginSuccess: () -> Unit,
    onBackToStore: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by authViewModel.uiState.collectAsState()

    LaunchedEffect(authViewModel) {
        authViewModel.loginSuccess.collect { onLoginSuccess() }
    }

    AdminLoginContent(
        state = state,
        onLogin = authViewModel::login,
        onCreateInitialConfig = authViewModel::createInitialConfig,
        onClearError = authViewModel::clearError,
        onBackToStore = onBackToStore,
        modifier = modifier
    )
}

@Composable
private fun AdminLoginContent(
    state: AdminAuthUiState,
    onLogin: (String) -> Unit,
    onCreateInitialConfig: (String, String, String) -> Unit,
    onClearError: () -> Unit,
    onBackToStore: () -> Unit,
    modifier: Modifier = Modifier
) {
    var nomeLoja by remember { mutableStateOf("") }
    var whatsappLoja by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    val firstAccess = !state.isLoading && !state.hasMasterPassword

    fun submit() {
        if (state.isLoading || state.isBusy) return
        if (firstAccess) {
            if (senha != confirmarSenha) {
                onClearError()
                return
            }
            onCreateInitialConfig(nomeLoja, whatsappLoja, senha)
        } else {
            onLogin(senha)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(FundoAdmin)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Área administrativa",
            color = AzulAdmin,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.height(8.dp))

        if (state.isLoading) {
            Text(
                text = "Carregando configuração segura…",
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(24.dp))
            CircularProgressIndicator(color = AzulAdmin)
        } else {
            Text(
                text = if (firstAccess) {
                    "Configure o acesso da proprietária"
                } else if (state.storeName.isNotBlank()) {
                    "Entre para gerenciar ${state.storeName}"
                } else {
                    "Entre para gerenciar a loja"
                },
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(24.dp))

            if (firstAccess) {
                OutlinedTextField(
                    value = nomeLoja,
                    onValueChange = {
                        nomeLoja = it
                        onClearError()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nome da Loja") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = whatsappLoja,
                    onValueChange = {
                        whatsappLoja = it.filter { char -> char.isDigit() || char in "+ ()-" }
                            .take(20)
                        onClearError()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("WhatsApp da Loja") },
                    placeholder = { Text("(11) 99999-9999") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    )
                )
                Spacer(Modifier.height(12.dp))
            }

            OutlinedTextField(
                value = senha,
                onValueChange = { valor ->
                    if (valor.length <= 12 && valor.all { it in '0'..'9' }) {
                        senha = valor
                        onClearError()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(if (firstAccess) "Criar senha master" else "Senha master") },
                supportingText = if (firstAccess) {
                    { Text("Use de 8 a 12 dígitos.") }
                } else null,
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = if (firstAccess) ImeAction.Next else ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { submit() }
                )
            )

            if (firstAccess) {
                Spacer(Modifier.height(12.dp))
                OutlinedTextField(
                    value = confirmarSenha,
                    onValueChange = { valor ->
                        if (valor.length <= 12 && valor.all { it in '0'..'9' }) {
                            confirmarSenha = valor
                            onClearError()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Confirmar senha master") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { submit() })
                )
            }

            val validationError = when {
                firstAccess && confirmarSenha.isNotEmpty() && senha != confirmarSenha ->
                    "As senhas não coincidem."
                else -> state.error
            }
            validationError?.let { error ->
                Spacer(Modifier.height(12.dp))
                Text(
                    text = error,
                    color = Color(0xFFB3261E),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = { submit() },
                enabled = !state.isBusy,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmareloAdmin,
                    contentColor = AzulAdmin
                )
            ) {
                Text(
                    text = when {
                        state.isBusy && firstAccess -> "Salvando configuração…"
                        state.isBusy -> "Verificando…"
                        firstAccess -> "Salvar e entrar"
                        else -> "Entrar"
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        }

        TextButton(onClick = onBackToStore) {
            Text("Voltar para a Loja", color = AzulAdmin)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AdminLoginScreenPreview() {
    MyApplicationTheme {
        AdminLoginContent(
            state = AdminAuthUiState(
                isLoading = false,
                hasMasterPassword = true,
                storeName = "Casa da Faxina"
            ),
            onLogin = {},
            onCreateInitialConfig = { _, _, _ -> },
            onClearError = {},
            onBackToStore = {}
        )
    }
}
