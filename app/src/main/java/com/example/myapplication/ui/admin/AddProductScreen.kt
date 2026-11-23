package com.example.myapplication.ui.admin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.data.model.Produto
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

private val AzulCadastro = Color(0xFF0047BB)
private val AmareloCadastro = Color(0xFFFFD500)
private val FundoCadastro = Color(0xFFF3F4F6)
private val categoriasDisponiveis = listOf(
    "Limpadores",
    "Desengraxantes",
    "Cerâmicas",
    "Pisos",
    "Tecidos",
    "Acessórios",
    "Utilidades",
    "Aromatizantes",
    "Outros",
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreen(
    onBack: () -> Unit,
    onSaveProduct: (Produto) -> Unit,
    modifier: Modifier = Modifier,
    initialProduct: Produto? = null,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var nome by remember(initialProduct?.id) { mutableStateOf(initialProduct?.nome.orEmpty()) }
    var precoTexto by remember(initialProduct?.id) {
        mutableStateOf(initialProduct?.preco?.toString()?.replace('.', ',').orEmpty())
    }
    var categoria by remember(initialProduct?.id) { mutableStateOf(initialProduct?.categoria.orEmpty()) }
    var fotoSelecionada by remember(initialProduct?.id) {
        mutableStateOf(initialProduct?.imagemUri?.let(Uri::parse))
    }
    var fotoPrevia by remember { mutableStateOf<androidx.compose.ui.graphics.ImageBitmap?>(null) }
    var categoriaExpandida by remember { mutableStateOf(value = false) }
    var erro by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(initialProduct?.id) {
        val imagemExistente = initialProduct?.imagemUri?.let(Uri::parse)
        fotoPrevia = imagemExistente?.let { uri ->
            withContext(Dispatchers.IO) { decodeSampledBitmap(context, uri)?.asImageBitmap() }
        }
    }

    val coresCampos = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color(0xFF1A202C),
        unfocusedTextColor = Color(0xFF1A202C),
        focusedLabelColor = AzulCadastro,
        unfocusedLabelColor = Color(0xFF475569),
        focusedPlaceholderColor = Color(0xFF64748B),
        unfocusedPlaceholderColor = Color(0xFF64748B),
        cursorColor = AzulCadastro,
        focusedBorderColor = AzulCadastro,
        unfocusedBorderColor = Color(0xFF64748B)
    )

    val seletorDeFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        fotoSelecionada = null
        fotoPrevia = null
        if (uri != null) {
            coroutineScope.launch {
                val imagemPermanente = withContext(Dispatchers.IO) {
                    copyUriToInternalStorage(context, uri)
                }
                fotoSelecionada = imagemPermanente
                fotoPrevia = imagemPermanente?.let {
                    withContext(Dispatchers.IO) {
                        decodeSampledBitmap(context, it)?.asImageBitmap()
                    }
                }
                if (imagemPermanente == null) erro = "Não foi possível salvar a foto selecionada."
                else erro = null
            }
        }
    }

    // Este contrato abre o app de câmera do sistema; a permissão de câmera é
    // administrada por esse app, portanto o Casa da Faxina não precisa solicitá-la.
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            fotoPrevia = bitmap.asImageBitmap()
            coroutineScope.launch {
                fotoSelecionada = withContext(Dispatchers.IO) {
                    saveBitmapToInternalStorage(context, bitmap)
                }
                erro = if (fotoSelecionada == null) "Não foi possível salvar a foto." else null
            }
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = FundoCadastro,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (initialProduct == null) "Novo Produto" else "Editar Produto",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulCadastro,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = {
                    nome = it
                    erro = null
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nome do Produto") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = coresCampos
            )

            OutlinedTextField(
                value = precoTexto,
                onValueChange = { novoValor ->
                    val filtrado = novoValor.filter { it.isDigit() || (it == ',') }
                    if (filtrado.count { it == ',' } <= 1) {
                        precoTexto = filtrado
                        erro = null
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Preço") },
                placeholder = { Text("Ex.: 29,99") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = coresCampos,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
            )

            Box {
                OutlinedTextField(
                    value = categoria,
                    onValueChange = { valor ->
                        categoria = valor
                        categoriaExpandida = true
                        erro = null
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Categoria") },
                    placeholder = { Text("Digite ou selecione uma categoria") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = coresCampos,
                    trailingIcon = {
                        IconButton(onClick = { categoriaExpandida = true }) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Mostrar categorias"
                            )
                        }
                    }
                )
                DropdownMenu(
                    expanded = categoriaExpandida,
                    onDismissRequest = { categoriaExpandida = false }
                ) {
                    categoriasDisponiveis.forEach { opcao ->
                        DropdownMenuItem(
                            text = { Text(opcao) },
                            onClick = {
                                categoria = opcao
                                categoriaExpandida = false
                                erro = null
                            }
                        )
                    }
                }
            }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Prévia da foto do produto",
                        color = AzulCadastro,
                        fontWeight = FontWeight.SemiBold
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(190.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFEAF1FC)),
                        contentAlignment = Alignment.Center
                    ) {
                        val imagem = fotoPrevia
                        if (imagem != null) {
                            Image(
                                bitmap = imagem,
                                contentDescription = "Foto selecionada para o produto",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Image(
                                    painter = painterResource(R.drawable.logo_casa_da_faxina),
                                    contentDescription = "Logo Casa da Faxina",
                                    modifier = Modifier
                                        .size(82.dp)
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                                Text(
                                    text = "Imagem do produto",
                                    color = AzulCadastro,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = { seletorDeFoto.launch("image/*") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AzulCadastro,
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 14.dp)
                ) {
                    Text("Escolher da Galeria")
                }

                OutlinedButton(
                    onClick = { cameraLauncher.launch(null) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AzulCadastro),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 14.dp)
                ) {
                    Text("Tirar Foto")
                }
            }

            fotoSelecionada?.let { uri ->
                Text(
                    text = uri.lastPathSegment ?: "Imagem selecionada",
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            erro?.let {
                Text(
                    text = it,
                    color = Color(0xFFB3261E),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Button(
                onClick = {
                    val preco = precoTexto.replace(',', '.').toDoubleOrNull()
                    if (nome.isBlank()) {
                        erro = "Informe o nome do produto."
                    } else if (preco == null || (preco <= 0.0)) {
                        erro = "Informe um preço maior que zero, como 29,99."
                    } else if (categoria.isBlank()) {
                        erro = "Informe ou selecione uma categoria."
                    } else {
                        onSaveProduct(
                            Produto(
                                id = initialProduct?.id ?: UUID.randomUUID().toString(),
                                nome = nome.trim(),
                                preco = preco,
                                categoria = categoria.trim(),
                                imagemRes = initialProduct?.imagemRes ?: 0,
                                descricao = initialProduct?.descricao.orEmpty(),
                                precoSobConsulta = initialProduct?.precoSobConsulta ?: false,
                                fotosGaleria = initialProduct?.fotosGaleria.orEmpty(),
                                emPromocao = initialProduct?.emPromocao ?: false,
                                maisVendido = initialProduct?.maisVendido ?: false,
                                imagemUri = fotoSelecionada?.toString()
                            )
                        )
                        Toast.makeText(
                            context,
                            if (initialProduct == null) "Produto cadastrado com sucesso!" else "Produto atualizado com sucesso!",
                            Toast.LENGTH_SHORT
                        ).show()
                        onBack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AmareloCadastro,
                    contentColor = AzulCadastro
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Text(
                    text = if (initialProduct == null) "Salvar Produto" else "Salvar Alterações",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private fun decodeSampledBitmap(context: Context, uri: Uri): Bitmap? {
    val resolver = context.contentResolver
    val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }

    val sampleSize = calculateInSampleSize(bounds)
    val options = BitmapFactory.Options().apply { inSampleSize = sampleSize }
    return resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, options) }
}

private fun calculateInSampleSize(options: BitmapFactory.Options): Int {
    val requestedWidth = 1024
    val requestedHeight = 1024
    val height = options.outHeight
    val width = options.outWidth
    var sampleSize = 1
    if (height > requestedHeight || width > requestedWidth) {
        val halfHeight = height / 2
        val halfWidth = width / 2
        while (halfHeight / sampleSize >= requestedHeight &&
            halfWidth / sampleSize >= requestedWidth
        ) {
            sampleSize *= 2
        }
    }
    return sampleSize
}

private fun copyUriToInternalStorage(context: Context, sourceUri: Uri): Uri? = runCatching {
    val imageDirectory = File(context.filesDir, "produtos").apply { mkdirs() }
    val imageFile = File(imageDirectory, "produto-${UUID.randomUUID()}.jpg")
    context.contentResolver.openInputStream(sourceUri)?.use { input ->
        imageFile.outputStream().use(input::copyTo)
    } ?: return null
    Uri.fromFile(imageFile)
}.getOrNull()

private fun saveBitmapToInternalStorage(context: Context, bitmap: Bitmap): Uri? = runCatching {
    val imageDirectory = File(context.filesDir, "produtos").apply { mkdirs() }
    val imageFile = File(imageDirectory, "produto-${UUID.randomUUID()}.jpg")
    imageFile.outputStream().use { output ->
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, output)
    }
    Uri.fromFile(imageFile)
}.getOrNull()

@Preview(showBackground = true)
@Composable
private fun AddProductScreenPreview() {
    MyApplicationTheme {
        AddProductScreen(
            onBack = {},
            onSaveProduct = { _ -> }
        )
    }
}
