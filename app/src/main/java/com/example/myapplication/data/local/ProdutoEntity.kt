package com.example.myapplication.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.myapplication.data.model.Produto

@Entity(tableName = "produtos")
data class ProdutoEntity(
    @PrimaryKey val id: String,
    val nome: String,
    val preco: Double,
    val categoria: String,
    val imagemUri: String?,
    val emPromocao: Boolean,
    val maisVendido: Boolean,
    val imagemRes: Int = 0,
    val descricao: String = "",
    val precoSobConsulta: Boolean = false,
    val fotosGaleria: List<Int> = emptyList()
)

class ProdutoConverters {
    @TypeConverter
    fun fromIntList(value: List<Int>): String = value.joinToString(",")

    @TypeConverter
    fun toIntList(value: String): List<Int> =
        value.split(",").mapNotNull(String::toIntOrNull)
}

fun Produto.toEntity() = ProdutoEntity(
    id = id,
    nome = nome,
    preco = preco,
    categoria = categoria,
    imagemUri = imagemUri,
    emPromocao = emPromocao,
    maisVendido = maisVendido,
    imagemRes = imagemRes,
    descricao = descricao,
    precoSobConsulta = precoSobConsulta,
    fotosGaleria = fotosGaleria
)

fun ProdutoEntity.toProduto() = Produto(
    id = id,
    nome = nome,
    preco = preco,
    categoria = categoria,
    imagemRes = imagemRes,
    descricao = descricao,
    precoSobConsulta = precoSobConsulta,
    fotosGaleria = fotosGaleria,
    emPromocao = emPromocao,
    maisVendido = maisVendido,
    imagemUri = imagemUri
)
