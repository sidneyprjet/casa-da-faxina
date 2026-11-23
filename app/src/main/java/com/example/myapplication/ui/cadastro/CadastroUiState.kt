package com.example.myapplication.ui.cadastro

import com.example.myapplication.data.model.Usuario

private val dddsBrasileiros = setOf(
    11, 12, 13, 14, 15, 16, 17, 18, 19,
    21, 22, 24, 27, 28,
    31, 32, 33, 34, 35, 37, 38,
    41, 42, 43, 44, 45, 46, 47, 48, 49,
    51, 53, 54, 55,
    61, 62, 63, 64, 65, 66, 67, 68, 69,
    71, 73, 74, 75, 77, 79,
    81, 82, 83, 84, 85, 86, 87, 88, 89,
    91, 92, 93, 94, 95, 96, 97, 98, 99
)

internal fun telefoneBrasileiroValido(digitos: String): Boolean {
    if (digitos.length != 11 || digitos[2] != '9') return false
    val ddd = digitos.take(2).toIntOrNull() ?: return false
    return ddd in dddsBrasileiros
}

data class CadastroUiState(
    val nome: String = "",
    val telefone: String = "",
    val senha: String = "",
    val confirmarSenha: String = "",
    val nomeErro: String? = null,
    val telefoneErro: String? = null,
    val senhaErro: String? = null,
    val confirmarSenhaErro: String? = null,
    val isLoading: Boolean = false,
    val usuarioCadastrado: Usuario? = null,
    val mensagemErro: String? = null
) {
    val telefoneValido: Boolean
        get() = telefoneBrasileiroValido(telefone)
}
