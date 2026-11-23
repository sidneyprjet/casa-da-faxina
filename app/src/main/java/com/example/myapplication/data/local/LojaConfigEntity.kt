package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Configurações locais da loja. O ID é fixo para manter uma única configuração.
 * A senha deve ser salva como hash, nunca em texto puro.
 */
@Entity(tableName = "loja_config")
data class LojaConfigEntity(
    @PrimaryKey val id: Int = 1,
    val nomeDaLoja: String,
    val whatsappDaLoja: String,
    val senhaMaster: String
)
