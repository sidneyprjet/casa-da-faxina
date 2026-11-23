package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LojaConfigDao {
    @Query("SELECT * FROM loja_config WHERE id = 1 LIMIT 1")
    fun getConfig(): Flow<LojaConfigEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOuAtualizar(config: LojaConfigEntity)
}
