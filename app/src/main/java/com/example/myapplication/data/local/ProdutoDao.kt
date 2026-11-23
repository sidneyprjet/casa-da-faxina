package com.example.myapplication.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProdutoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(produto: ProdutoEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(produtos: List<ProdutoEntity>)

    @Update
    suspend fun update(produto: ProdutoEntity)

    @Delete
    suspend fun delete(produto: ProdutoEntity)

    @Query("DELETE FROM produtos WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("SELECT * FROM produtos ORDER BY nome COLLATE NOCASE")
    fun getAllProducts(): Flow<List<ProdutoEntity>>

    @Query("SELECT * FROM produtos WHERE categoria = :categoria ORDER BY nome COLLATE NOCASE")
    fun getProductsByCategoria(categoria: String): Flow<List<ProdutoEntity>>

    @Query("SELECT * FROM produtos WHERE categoria = :categoria ORDER BY nome COLLATE NOCASE")
    fun getProductsByCategory(categoria: String): Flow<List<ProdutoEntity>>

    @Query("SELECT COUNT(*) FROM produtos")
    suspend fun getProductCount(): Int
}
