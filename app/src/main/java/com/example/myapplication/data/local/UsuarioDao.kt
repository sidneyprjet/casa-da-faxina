package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(usuario: UsuarioEntity): Long

    @Query("SELECT * FROM usuarios_room WHERE telefone = :telefone LIMIT 1")
    suspend fun getUsuarioByTelefone(telefone: String): UsuarioEntity?
}
