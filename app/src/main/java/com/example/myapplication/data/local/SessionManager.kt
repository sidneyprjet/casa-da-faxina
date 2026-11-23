package com.example.myapplication.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Base64
import com.example.myapplication.data.model.Usuario
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class SessionManager(context: Context) {

    private val appContext = context.applicationContext
    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "casadafaxina_secure_session"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_HAS_CUSTOMER_ACCOUNT = "has_customer_account"
        private const val KEY_ACCOUNT_ID = "customer_id"
        private const val KEY_ACCOUNT_NAME = "customer_name"
        private const val KEY_ACCOUNT_PHONE = "customer_phone"
        private const val KEY_ACCOUNT_CREATED = "customer_created"
        private const val KEY_CUSTOMER_PASSWORD_SALT = "customer_password_salt"
        private const val KEY_CUSTOMER_PASSWORD_HASH = "customer_password_hash"
        private const val PASSWORD_ITERATIONS = 120_000
        private const val PASSWORD_KEY_LENGTH_BITS = 256
        private const val KEY_USER_ID = "user_id"
        private const val KEY_USER_NOME = "user_nome"
        private const val KEY_USER_TELEFONE = "user_telefone"
        private const val KEY_USER_IS_VISITANTE = "user_is_visitante"
        private const val KEY_USER_CRIADO_EM = "user_criado_em"
    }

    fun salvarSessao(usuario: Usuario) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            if (!usuario.isVisitante) {
                putBoolean(KEY_HAS_CUSTOMER_ACCOUNT, true)
                putLong(KEY_ACCOUNT_ID, usuario.id)
                putString(KEY_ACCOUNT_NAME, usuario.nome)
                putString(KEY_ACCOUNT_PHONE, usuario.telefone)
                putLong(KEY_ACCOUNT_CREATED, usuario.criadoEm)
            }
            putLong(KEY_USER_ID, usuario.id)
            putString(KEY_USER_NOME, usuario.nome)
            putString(KEY_USER_TELEFONE, usuario.telefone)
            putBoolean(KEY_USER_IS_VISITANTE, usuario.isVisitante)
            putLong(KEY_USER_CRIADO_EM, usuario.criadoEm)
            apply()
        }
    }

    fun obterUsuario(): Usuario? {
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        if (!isLoggedIn) return null

        val id = prefs.getLong(KEY_USER_ID, 0L)
        val nome = prefs.getString(KEY_USER_NOME, "") ?: "Visitante"
        val telefone = prefs.getString(KEY_USER_TELEFONE, "") ?: ""
        val isVisitante = prefs.getBoolean(KEY_USER_IS_VISITANTE, false)
        val criadoEm = prefs.getLong(KEY_USER_CRIADO_EM, System.currentTimeMillis())

        return Usuario(
            id = id,
            nome = nome,
            telefone = telefone,
            criadoEm = criadoEm,
            isVisitante = isVisitante
        )
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    /** Mantém a conta cadastrada e métricas locais ao encerrar apenas a sessão. */
    fun limparSessao() {
        prefs.edit()
            .remove(KEY_IS_LOGGED_IN)
            .remove(KEY_USER_ID)
            .remove(KEY_USER_NOME)
            .remove(KEY_USER_TELEFONE)
            .remove(KEY_USER_IS_VISITANTE)
            .remove(KEY_USER_CRIADO_EM)
            .apply()
    }

    /** Busca a conta registrada, inclusive em instalações antigas sem metadados nas preferências. */
    fun obterContaCliente(): Usuario? {
        val id = prefs.getLong(KEY_ACCOUNT_ID, 0L)
        val nome = prefs.getString(KEY_ACCOUNT_NAME, null)
        if (id > 0L && nome != null) {
            return Usuario(
                id = id,
                nome = nome,
                telefone = prefs.getString(KEY_ACCOUNT_PHONE, "") ?: "",
                criadoEm = prefs.getLong(KEY_ACCOUNT_CREATED, 0L),
                isVisitante = false
            )
        }

        val contaAntiga = DatabaseHelper(appContext).listarUsuarios()
            .lastOrNull { !it.isVisitante }
        contaAntiga?.let { salvarDadosConta(it) }
        return contaAntiga
    }

    fun existeContaCliente(): Boolean = obterContaCliente() != null

    fun clienteTemSenha(): Boolean =
        prefs.contains(KEY_CUSTOMER_PASSWORD_SALT) && prefs.contains(KEY_CUSTOMER_PASSWORD_HASH)

    fun salvarSenhaCliente(senha: String) {
        require(senha.length >= 6) { "A senha deve ter pelo menos 6 caracteres." }
        val salt = ByteArray(16).also(SecureRandom()::nextBytes)
        val hash = derivarHashDaSenha(senha, salt)
        prefs.edit()
            .putBoolean(KEY_HAS_CUSTOMER_ACCOUNT, true)
            .putString(KEY_CUSTOMER_PASSWORD_SALT, Base64.encodeToString(salt, Base64.NO_WRAP))
            .putString(KEY_CUSTOMER_PASSWORD_HASH, Base64.encodeToString(hash, Base64.NO_WRAP))
            .apply()
    }

    fun validarSenhaCliente(senha: String): Boolean {
        val saltCodificado = prefs.getString(KEY_CUSTOMER_PASSWORD_SALT, null) ?: return false
        val hashCodificado = prefs.getString(KEY_CUSTOMER_PASSWORD_HASH, null) ?: return false
        val salt = Base64.decode(saltCodificado, Base64.NO_WRAP)
        val hashEsperado = Base64.decode(hashCodificado, Base64.NO_WRAP)
        return MessageDigest.isEqual(hashEsperado, derivarHashDaSenha(senha, salt))
    }

    fun limparContaCliente() {
        prefs.edit()
            .remove(KEY_HAS_CUSTOMER_ACCOUNT)
            .remove(KEY_ACCOUNT_ID)
            .remove(KEY_ACCOUNT_NAME)
            .remove(KEY_ACCOUNT_PHONE)
            .remove(KEY_ACCOUNT_CREATED)
            .remove(KEY_CUSTOMER_PASSWORD_SALT)
            .remove(KEY_CUSTOMER_PASSWORD_HASH)
            .apply()
    }

    private fun salvarDadosConta(usuario: Usuario) {
        prefs.edit()
            .putBoolean(KEY_HAS_CUSTOMER_ACCOUNT, true)
            .putLong(KEY_ACCOUNT_ID, usuario.id)
            .putString(KEY_ACCOUNT_NAME, usuario.nome)
            .putString(KEY_ACCOUNT_PHONE, usuario.telefone)
            .putLong(KEY_ACCOUNT_CREATED, usuario.criadoEm)
            .apply()
    }

    private fun derivarHashDaSenha(senha: String, salt: ByteArray): ByteArray {
        val spec = PBEKeySpec(senha.toCharArray(), salt, PASSWORD_ITERATIONS, PASSWORD_KEY_LENGTH_BITS)
        return try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
        }
    }

    /**
     * Registra o envio de um produto para o WhatsApp da dona.
     * Utilizado para alimentar automaticamente a lista de "Mais Vendidos".
     */
    fun registrarVendaWhatsApp(produtoId: String, quantidade: Int = 1) {
        val total = prefs.getInt("vendas_whatsapp_$produtoId", 0)
        prefs.edit().putInt("vendas_whatsapp_$produtoId", total + quantidade).apply()
    }

    /**
     * Retorna a contagem de vezes que o produto foi enviado para o WhatsApp da dona.
     */
    fun obterVendasWhatsApp(produtoId: String): Int {
        return prefs.getInt("vendas_whatsapp_$produtoId", 0)
    }
}
