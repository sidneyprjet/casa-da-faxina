package com.example.myapplication.ui.cadastro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.Usuario
import com.example.myapplication.data.repository.UsuarioRepository
import com.example.myapplication.data.repository.UsuarioRepositoryImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CadastroViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: UsuarioRepository = UsuarioRepositoryImpl(application)

    private val _uiState = MutableStateFlow(CadastroUiState())
    val uiState: StateFlow<CadastroUiState> = _uiState.asStateFlow()

    fun onNomeChange(novoNome: String) {
        _uiState.update {
            it.copy(
                nome = novoNome,
                nomeErro = null
            )
        }
    }

    fun onSenhaChange(novaSenha: String) {
        _uiState.update {
            it.copy(
                senha = novaSenha,
                senhaErro = if (novaSenha.isNotEmpty() && novaSenha.length < 6) {
                    "A senha deve ter pelo menos 6 caracteres."
                } else null,
                confirmarSenhaErro = if (it.confirmarSenha.isNotEmpty() && novaSenha != it.confirmarSenha) {
                    "As senhas não coincidem."
                } else null,
                mensagemErro = null
            )
        }
    }

    fun onConfirmarSenhaChange(novaSenha: String) {
        _uiState.update {
            it.copy(
                confirmarSenha = novaSenha,
                confirmarSenhaErro = if (novaSenha.isNotEmpty() && novaSenha != it.senha) {
                    "As senhas não coincidem."
                } else null
            )
        }
    }

    fun onTelefoneChange(novoTelefone: String) {
        val digitos = novoTelefone.filter { it.isDigit() }.take(11)
        val telefoneValido = telefoneBrasileiroValido(digitos)

        _uiState.update {
            it.copy(
                telefone = digitos,
                telefoneErro = when {
                    digitos.isEmpty() || telefoneValido -> null
                    else -> "Digite um número válido com DDD e 9 dígitos."
                }
            )
        }
    }

    fun cadastrar(onSuccess: (Usuario) -> Unit = {}) {
        val estadoAtual = _uiState.value
        val nomeValido = validarNome(estadoAtual.nome)
        val telefoneValido = validarTelefone(estadoAtual.telefone)
        val senhaValida = estadoAtual.senha.length >= 6
        val senhaConfirmada = estadoAtual.senha == estadoAtual.confirmarSenha

        if (!nomeValido || !telefoneValido || !senhaValida || !senhaConfirmada) {
            _uiState.update {
                it.copy(
                    nomeErro = if (!nomeValido) "Informe seu nome completo (mínimo 3 letras)" else null,
                    telefoneErro = if (!telefoneValido) "Digite um número válido com DDD e 9 dígitos." else null,
                    senhaErro = if (!senhaValida) "A senha deve ter pelo menos 6 caracteres." else null,
                    confirmarSenhaErro = if (senhaValida && !senhaConfirmada) "As senhas não coincidem." else null
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, mensagemErro = null) }

        viewModelScope.launch {
            val resultado = repository.cadastrarUsuario(
                estadoAtual.nome,
                formatarTelefone(estadoAtual.telefone),
                estadoAtual.senha
            )
            resultado.onSuccess { usuario ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        usuarioCadastrado = usuario,
                        senha = "",
                        confirmarSenha = "",
                        senhaErro = null,
                        confirmarSenhaErro = null
                    )
                }
                onSuccess(usuario)
            }.onFailure { erro ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        mensagemErro = erro.localizedMessage ?: "Erro ao realizar cadastro no banco de dados."
                    )
                }
            }
        }
    }

    fun entrarCliente(onSuccess: (Usuario) -> Unit = {}) {
        val senha = _uiState.value.senha
        if (senha.isBlank()) {
            _uiState.update { it.copy(senhaErro = "Digite sua senha.") }
            return
        }
        executarLogin(onSuccess) { repository.autenticarCliente(senha) }
    }

    fun definirSenhaCliente(onSuccess: (Usuario) -> Unit = {}) {
        val estado = _uiState.value
        when {
            estado.senha.length < 6 -> {
                _uiState.update { it.copy(senhaErro = "A senha deve ter pelo menos 6 caracteres.") }
                return
            }
            estado.senha != estado.confirmarSenha -> {
                _uiState.update { it.copy(confirmarSenhaErro = "As senhas não coincidem.") }
                return
            }
        }
        executarLogin(onSuccess) { repository.definirSenhaCliente(estado.senha) }
    }

    private fun executarLogin(
        onSuccess: (Usuario) -> Unit,
        autenticar: suspend () -> Result<Usuario>
    ) {
        _uiState.update { it.copy(isLoading = true, mensagemErro = null, senhaErro = null) }
        viewModelScope.launch {
            autenticar().onSuccess { usuario ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        usuarioCadastrado = usuario,
                        senha = "",
                        confirmarSenha = "",
                        senhaErro = null,
                        confirmarSenhaErro = null
                    )
                }
                onSuccess(usuario)
            }.onFailure { erro ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        mensagemErro = erro.localizedMessage ?: "Não foi possível entrar."
                    )
                }
            }
        }
    }

    fun entrarComoVisitante(onSuccess: (Usuario) -> Unit = {}) {
        _uiState.update { it.copy(isLoading = true, mensagemErro = null) }

        viewModelScope.launch {
            val resultado = repository.cadastrarVisitante()
            resultado.onSuccess { visitante ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        usuarioCadastrado = visitante
                    )
                }
                onSuccess(visitante)
            }.onFailure { erro ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        mensagemErro = erro.localizedMessage ?: "Erro ao entrar como visitante."
                    )
                }
            }
        }
    }

    private fun validarNome(nome: String): Boolean {
        val limpo = nome.trim()
        return limpo.length >= 3
    }

    private fun validarTelefone(telefone: String): Boolean {
        val digitos = telefone.filter { it.isDigit() }
        return telefoneBrasileiroValido(digitos)
    }

    private fun formatarTelefone(digitos: String): String {
        return when {
            digitos.length <= 2 -> if (digitos.isNotEmpty()) "($digitos" else ""
            digitos.length <= 6 -> "(${digitos.substring(0, 2)}) ${digitos.substring(2)}"
            digitos.length <= 10 -> "(${digitos.substring(0, 2)}) ${digitos.substring(2, 6)}-${digitos.substring(6)}"
            else -> "(${digitos.substring(0, 2)}) ${digitos.substring(2, 7)}-${digitos.substring(7, 11)}"
        }
    }
}
