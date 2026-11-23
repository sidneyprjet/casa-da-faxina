package com.example.myapplication.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.text.KeyboardOptions
import com.example.myapplication.data.model.Produto
import com.example.myapplication.ui.home.produtosExemplo
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.text.NumberFormat
import java.util.Locale

private val AzulPainel = Color(0xFF0047BB)
private val AmareloPainel = Color(0xFFFFD500)
private val FundoPainel = Color(0xFFF3F4F6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelScreen(
    products: List<Produto> = produtosExemplo,
    onBack: () -> Unit,
    onAddProduct: () -> Unit = {},
    onDeleteProduct: (Produto) -> Unit = {},
    onEditProduct: (Produto) -> Unit = {},
    onViewMetrics: () -> Unit = {},
    passwordChangeResult: PasswordChangeResult? = null,
    passwordChangeBusy: Boolean = false,
    onChangeMasterPassword: (String, String) -> Unit = { _, _ -> },
    onClearPasswordChangeResult: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var produtoParaExcluir by remember { mutableStateOf<Produto?>(null) }
    var mostrarConfiguracoesSenha by remember { mutableStateOf(false) }
    val formatoMoeda = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"))

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = FundoPainel,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Painel da Dona",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    TextButton(onClick = onViewMetrics) {
                        Text("Ver Métricas", color = AmareloPainel)
                    }
                    IconButton(
                        onClick = {
                            onClearPasswordChangeResult()
                            mostrarConfiguracoesSenha = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Configurações de segurança",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulPainel,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddProduct,
                containerColor = AmareloPainel,
                contentColor = AzulPainel
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Adicionar Novo Produto"
                )
            }
        }
    ) { innerPadding ->
        if (products.isEmpty()) {
            Text(
                text = "Nenhum produto cadastrado.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .padding(24.dp),
                color = Color.DarkGray,
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products, key = { it.id }) { product ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 18.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = product.nome,
                                modifier = Modifier.weight(1f),
                                color = Color(0xFF25232A),
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                            Text(
                                text = if (product.precoSobConsulta) {
                                    "Sob consulta"
                                } else {
                                    formatoMoeda.format(product.preco)
                                },
                                modifier = Modifier.padding(start = 12.dp),
                                color = AzulPainel,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            IconButton(onClick = { onEditProduct(product) }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Editar ${product.nome}",
                                    tint = AzulPainel
                                )
                            }
                            IconButton(
                                onClick = { produtoParaExcluir = product }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Excluir ${product.nome}",
                                    tint = Color(0xFFB3261E)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarConfiguracoesSenha) {
        AdminPasswordChangeDialog(
            result = passwordChangeResult,
            isBusy = passwordChangeBusy,
            onSubmit = onChangeMasterPassword,
            onDismiss = {
                mostrarConfiguracoesSenha = false
                onClearPasswordChangeResult()
            }
        )
    }

    produtoParaExcluir?.let { produto ->
        AlertDialog(
            onDismissRequest = { produtoParaExcluir = null },
            title = { Text("Excluir produto?") },
            text = { Text("Tem certeza que deseja excluir este produto?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteProduct(produto)
                        produtoParaExcluir = null
                    }
                ) {
                    Text("Excluir", color = Color(0xFFB3261E))
                }
            },
            dismissButton = {
                TextButton(onClick = { produtoParaExcluir = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
private fun AdminPasswordChangeDialog(
    result: PasswordChangeResult?,
    isBusy: Boolean,
    onSubmit: (String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Alterar senha master") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "A nova senha deve conter de 8 a 12 dígitos.",
                    color = Color(0xFF475569),
                    style = MaterialTheme.typography.bodySmall
                )
                OutlinedTextField(
                    value = currentPassword,
                    onValueChange = { value ->
                        if (value.length <= 12 && value.all { it in '0'..'9' }) {
                            currentPassword = value
                            validationError = null
                        }
                    },
                    label = { Text("Senha atual") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { value ->
                        if (value.length <= 12 && value.all { it in '0'..'9' }) {
                            newPassword = value
                            validationError = null
                        }
                    },
                    label = { Text("Nova senha") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { value ->
                        if (value.length <= 12 && value.all { it in '0'..'9' }) {
                            confirmPassword = value
                            validationError = null
                        }
                    },
                    label = { Text("Confirmar nova senha") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                val visibleError = validationError ?: result?.takeIf { !it.success }?.message
                visibleError?.let {
                    Text(it, color = Color(0xFFB3261E), style = MaterialTheme.typography.bodySmall)
                }
                result?.takeIf { it.success }?.let {
                    Text(it.message, color = AzulPainel, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = !isBusy,
                onClick = {
                    when {
                        currentPassword.isBlank() -> validationError = "Informe a senha atual."
                        newPassword.length !in 8..12 -> validationError = "A nova senha deve ter de 8 a 12 dígitos."
                        newPassword != confirmPassword -> validationError = "A confirmação da nova senha não coincide."
                        else -> {
                            validationError = null
                            onSubmit(currentPassword, newPassword)
                        }
                    }
                }
            ) {
                Text(if (isBusy) "Salvando…" else "Salvar", color = AzulPainel)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(if (result?.success == true) "Concluir" else "Cancelar")
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AdminPanelScreenPreview() {
    MyApplicationTheme {
        AdminPanelScreen(onBack = {})
    }
}
