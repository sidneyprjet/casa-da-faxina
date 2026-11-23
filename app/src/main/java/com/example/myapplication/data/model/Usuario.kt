package com.example.myapplication.data.model

data class Usuario(
    val id: Long = 0L,
    val nome: String,
    val telefone: String,
    val criadoEm: Long = System.currentTimeMillis(),
    val isVisitante: Boolean = false
)
