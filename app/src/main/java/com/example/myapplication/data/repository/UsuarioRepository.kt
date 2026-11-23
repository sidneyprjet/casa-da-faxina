package com.example.myapplication.data.repository

import android.content.Context
import com.example.myapplication.data.local.DatabaseHelper
import com.example.myapplication.data.local.SessionManager
import com.example.myapplication.data.model.Usuario
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface UsuarioRepository {
    suspend fun cadastrarUsuario(nome: String, telefone: String, senha: String): Result<Usuario>
    suspend fun autenticarCliente(senha: String): Result<Usuario>
    suspend fun definirSenhaCliente(senha: String): Result<Usuario>
    fun clienteTemSenha(): Boolean
    suspend fun cadastrarVisitante(): Result<Usuario>
    suspend fun obterUsuarioLogado(): Usuario?
    suspend fun deslogar()
    suspend fun listarTodos(): List<Usuario>
}

class UsuarioRepositoryImpl(context: Context) : UsuarioRepository {

    private val dbHelper = DatabaseHelper(context.applicationContext)
    private val sessionManager = SessionManager(context.applicationContext)

    override suspend fun cadastrarUsuario(nome: String, telefone: String, senha: String): Result<Usuario> = withContext(Dispatchers.IO) {
        try {
            val limpoNome = nome.trim()
            val limpoTelefone = telefone.trim()

            // Validação de segurança dos dados
            if (limpoNome.length < 3) {
                return@withContext Result.failure(IllegalArgumentException("Nome muito curto"))
            }

            val novoUsuario = Usuario(
                nome = limpoNome,
                telefone = limpoTelefone,
                isVisitante = false
            )
            val id = dbHelper.inserirUsuario(novoUsuario)
            if (id > 0) {
                val salvo = novoUsuario.copy(id = id)
                sessionManager.salvarSenhaCliente(senha)
                sessionManager.salvarSessao(salvo)
                Result.success(salvo)
            } else {
                Result.failure(Exception("Erro ao salvar cadastro no banco de dados."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun autenticarCliente(senha: String): Result<Usuario> = withContext(Dispatchers.IO) {
        val conta = sessionManager.obterContaCliente()
            ?: return@withContext Result.failure(IllegalStateException("Nenhuma conta de cliente foi encontrada."))
        if (!sessionManager.validarSenhaCliente(senha)) {
            return@withContext Result.failure(IllegalArgumentException("Senha incorreta."))
        }
        sessionManager.salvarSessao(conta)
        Result.success(conta)
    }

    override suspend fun definirSenhaCliente(senha: String): Result<Usuario> = withContext(Dispatchers.IO) {
        try {
            val conta = sessionManager.obterContaCliente()
                ?: return@withContext Result.failure(IllegalStateException("Nenhuma conta de cliente foi encontrada."))
            sessionManager.salvarSenhaCliente(senha)
            sessionManager.salvarSessao(conta)
            Result.success(conta)
        } catch (erro: Exception) {
            Result.failure(erro)
        }
    }

    override fun clienteTemSenha(): Boolean = sessionManager.clienteTemSenha()

    override suspend fun cadastrarVisitante(): Result<Usuario> = withContext(Dispatchers.IO) {
        try {
            val visitante = Usuario(
                nome = "Visitante",
                telefone = "",
                isVisitante = true
            )
            val id = dbHelper.inserirUsuario(visitante)
            if (id > 0) {
                val salvo = visitante.copy(id = id)
                sessionManager.salvarSessao(salvo)
                Result.success(salvo)
            } else {
                Result.failure(Exception("Erro ao criar sessão de visitante."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun obterUsuarioLogado(): Usuario? = withContext(Dispatchers.IO) {
        sessionManager.obterUsuario() ?: dbHelper.obterUltimoUsuario()
    }

    override suspend fun deslogar() = withContext(Dispatchers.IO) {
        sessionManager.limparSessao()
    }

    override suspend fun listarTodos(): List<Usuario> = withContext(Dispatchers.IO) {
        dbHelper.listarUsuarios()
    }
}
