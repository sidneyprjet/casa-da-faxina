package com.example.myapplication.ui.admin

import android.app.Application
import android.os.Build
import android.util.Base64
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.local.AppDatabase
import com.example.myapplication.data.local.LojaConfigEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

data class AdminAuthUiState(
    val isLoading: Boolean = true,
    val hasMasterPassword: Boolean = false,
    val storeName: String = "",
    val isBusy: Boolean = false,
    val isAuthenticated: Boolean = false,
    val error: String? = null,
    val passwordChangeResult: PasswordChangeResult? = null
)

data class PasswordChangeResult(
    val success: Boolean,
    val message: String
)

class AdminAuthViewModel(application: Application) : AndroidViewModel(application) {
    private val lojaConfigDao = AppDatabase.getDatabase(application).lojaConfigDao()
    private val mutableUiState = MutableStateFlow(AdminAuthUiState())
    val uiState = mutableUiState.asStateFlow()

    private val mutableLoginSuccess = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val loginSuccess = mutableLoginSuccess.asSharedFlow()

    @Volatile
    private var currentConfig: LojaConfigEntity? = null

    init {
        viewModelScope.launch {
            lojaConfigDao.getConfig().collect { config ->
                currentConfig = config
                mutableUiState.update {
                    it.copy(
                        isLoading = false,
                        hasMasterPassword = config != null,
                        storeName = config?.nomeDaLoja.orEmpty()
                    )
                }
            }
        }
    }

    fun clearError() {
        mutableUiState.update { it.copy(error = null) }
    }

    fun login(password: String) {
        if (!password.isDigitsOnly() || password.isBlank()) {
            mutableUiState.update { it.copy(error = "Informe sua senha numérica.") }
            return
        }
        val config = currentConfig
        if (config == null) {
            mutableUiState.update {
                it.copy(error = "Configure a senha master para concluir o primeiro acesso.")
            }
            return
        }

        viewModelScope.launch {
            mutableUiState.update { it.copy(isBusy = true, error = null) }
            val valid = withContext(Dispatchers.Default) {
                AdminPasswordHasher.verify(password, config.senhaMaster)
            }
            mutableUiState.update {
                it.copy(
                    isBusy = false,
                    isAuthenticated = valid,
                    error = if (valid) null else "Senha incorreta."
                )
            }
            if (valid) mutableLoginSuccess.emit(Unit)
        }
    }

    fun createInitialConfig(nomeDaLoja: String, whatsappDaLoja: String, password: String) {
        val nome = nomeDaLoja.trim()
        val whatsapp = whatsappDaLoja.filter(Char::isDigit)
        if (nome.isBlank()) {
            mutableUiState.update { it.copy(error = "Informe o nome da loja.") }
            return
        }
        if (whatsapp.length !in 10..13) {
            mutableUiState.update { it.copy(error = "Informe um WhatsApp válido com DDD.") }
            return
        }
        if (!password.isStrongNumericPassword()) {
            mutableUiState.update {
                it.copy(error = "Crie uma senha numérica com 8 a 12 dígitos.")
            }
            return
        }
        if (currentConfig != null) {
            mutableUiState.update {
                it.copy(error = "A configuração inicial já foi concluída. Entre com sua senha.")
            }
            return
        }

        viewModelScope.launch {
            mutableUiState.update { it.copy(isBusy = true, error = null) }
            runCatching {
                val passwordHash = withContext(Dispatchers.Default) {
                    AdminPasswordHasher.hash(password)
                }
                LojaConfigEntity(
                    id = 1,
                    nomeDaLoja = nome,
                    whatsappDaLoja = whatsapp,
                    senhaMaster = passwordHash
                ).also { lojaConfigDao.insertOuAtualizar(it) }
            }.onSuccess { config ->
                currentConfig = config
                mutableUiState.update {
                    it.copy(
                        isBusy = false,
                        hasMasterPassword = true,
                        storeName = config.nomeDaLoja,
                        isAuthenticated = true,
                        error = null
                    )
                }
                mutableLoginSuccess.emit(Unit)
            }.onFailure {
                mutableUiState.update {
                    it.copy(isBusy = false, error = "Não foi possível salvar a configuração. Tente novamente.")
                }
            }
        }
    }

    fun changeMasterPassword(currentPassword: String, newPassword: String) {
        clearPasswordChangeResult()
        val config = currentConfig
        if (config == null) {
            setPasswordChangeResult(false, "A configuração da loja ainda não foi criada.")
            return
        }
        if (!newPassword.isStrongNumericPassword()) {
            setPasswordChangeResult(false, "A nova senha deve ter de 8 a 12 dígitos.")
            return
        }

        viewModelScope.launch {
            mutableUiState.update { it.copy(isBusy = true) }
            val validCurrentPassword = withContext(Dispatchers.Default) {
                AdminPasswordHasher.verify(currentPassword, config.senhaMaster)
            }
            if (!validCurrentPassword) {
                mutableUiState.update {
                    it.copy(
                        isBusy = false,
                        passwordChangeResult = PasswordChangeResult(false, "A senha atual está incorreta.")
                    )
                }
                return@launch
            }

            runCatching {
                val newHash = withContext(Dispatchers.Default) {
                    AdminPasswordHasher.hash(newPassword)
                }
                config.copy(senhaMaster = newHash).also {
                    lojaConfigDao.insertOuAtualizar(it)
                }
            }.onSuccess { updatedConfig ->
                currentConfig = updatedConfig
                mutableUiState.update {
                    it.copy(
                        isBusy = false,
                        passwordChangeResult = PasswordChangeResult(
                            true,
                            "Senha master alterada com sucesso."
                        )
                    )
                }
            }.onFailure {
                mutableUiState.update {
                    it.copy(
                        isBusy = false,
                        passwordChangeResult = PasswordChangeResult(
                            false,
                            "Não foi possível alterar a senha. Tente novamente."
                        )
                    )
                }
            }
        }
    }

    fun clearPasswordChangeResult() {
        mutableUiState.update { it.copy(passwordChangeResult = null) }
    }

    private fun setPasswordChangeResult(success: Boolean, message: String) {
        mutableUiState.update {
            it.copy(passwordChangeResult = PasswordChangeResult(success, message))
        }
    }
}

private fun String.isDigitsOnly(): Boolean = isNotEmpty() && all { it in '0'..'9' }

private fun String.isStrongNumericPassword(): Boolean =
    length in 8..12 && isDigitsOnly()

/**
 * PBKDF2 with a random salt. Android versions before API 26 use the
 * platform-supported HMAC-SHA1 PRF; newer versions use HMAC-SHA256.
 */
private object AdminPasswordHasher {
    private const val ITERATIONS = 120_000
    private const val KEY_LENGTH_BITS = 256
    private const val VERSION = "pbkdf2"

    fun hash(password: String): String {
        val salt = ByteArray(16).also(SecureRandom()::nextBytes)
        val algorithm = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            "PBKDF2WithHmacSHA256"
        } else {
            "PBKDF2WithHmacSHA1"
        }
        val derivedKey = derive(password, salt, algorithm, ITERATIONS)
        val saltEncoded = Base64.encodeToString(salt, Base64.NO_WRAP)
        val keyEncoded = Base64.encodeToString(derivedKey, Base64.NO_WRAP)
        return "$VERSION:$algorithm:$ITERATIONS:$saltEncoded:$keyEncoded"
    }

    fun verify(password: String, encoded: String): Boolean = runCatching {
        val parts = encoded.split(':')
        if (parts.size != 5 || parts[0] != VERSION) return false
        val algorithm = parts[1]
        val iterations = parts[2].toIntOrNull() ?: return false
        if (iterations !in 10_000..1_000_000) return false
        val salt = Base64.decode(parts[3], Base64.NO_WRAP)
        val expected = Base64.decode(parts[4], Base64.NO_WRAP)
        val actual = derive(password, salt, algorithm, iterations)
        MessageDigest.isEqual(expected, actual)
    }.getOrDefault(false)

    private fun derive(
        password: String,
        salt: ByteArray,
        algorithm: String,
        iterations: Int
    ): ByteArray {
        val spec = PBEKeySpec(password.toCharArray(), salt, iterations, KEY_LENGTH_BITS)
        return try {
            SecretKeyFactory.getInstance(algorithm).generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
        }
    }
}
