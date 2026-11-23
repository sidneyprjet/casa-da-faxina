package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "usuarios_room",
    indices = [Index(value = ["telefone"], unique = true)]
)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val nome: String,
    val telefone: String,
    val isAdmin: Boolean = false,
    val isSuperAdmin: Boolean = false
)
