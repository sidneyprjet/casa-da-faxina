package com.example.myapplication.data.model

import androidx.annotation.DrawableRes

/**
 * Modelo de dados que representa um produto no app Casa da Faxina.
 *
 * @param id Identificador único do produto.
 * @param nome Nome de exibição do produto.
 * @param preco Preço numérico.
 * @param categoria Categoria do produto (Pisos, Tecidos, Acessórios).
 * @param imagemRes ID do recurso drawable (opcional; padrão 0).
 * @param descricao Texto descritivo com benefícios e modo de uso.
 * @param precoSobConsulta Define se o preço é exibido sob consulta.
 * @param fotosGaleria Lista de IDs de imagens para a galeria do produto.
 */
data class Produto @JvmOverloads constructor(
    val id: String,
    val nome: String,
    val preco: Double,
    val categoria: String,
    @DrawableRes val imagemRes: Int = 0,
    val descricao: String = "",
    val precoSobConsulta: Boolean = false,
    val fotosGaleria: List<Int> = emptyList(),
    val emPromocao: Boolean = false,
    val maisVendido: Boolean = false,
    val imagemUri: String? = null
) {
    /**
     * Propriedade de compatibilidade: permite acessar o ID da imagem tanto
     * por `produto.imagemRes` quanto por `produto.imagemResId`.
     */
    val imagemResId: Int
        get() = imagemRes
}
