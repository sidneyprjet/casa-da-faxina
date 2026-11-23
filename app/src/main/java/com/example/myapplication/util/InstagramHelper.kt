package com.example.myapplication.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

/**
 * Utilitário para abertura direta e segura do Instagram da loja da Sarinha (Casa da Faxina).
 * Segue as melhores práticas das versões modernas do Android (API 30 a 37):
 * - Tenta abrir diretamente no aplicativo oficial do Instagram caso instalado.
 * - Caso não esteja instalado, realiza fallback gracioso para o navegador da Web padrão.
 * - Custo zero de APIs e sem rastreamento de dados de terceiros (LGPD).
 */
object InstagramHelper {

    // Perfil oficial a ser informado e confirmado pelo usuário
    var perfilInstagramOficial: String = ""

    /**
     * Atualiza o perfil oficial após confirmação expressa do usuário.
     */
    fun definirPerfilInstagram(perfil: String) {
        perfilInstagramOficial = perfil
            .removePrefix("@")
            .removePrefix("https://instagram.com/")
            .removePrefix("https://www.instagram.com/")
            .trim()
    }

    /**
     * Abre o perfil no aplicativo do Instagram ou no navegador web.
     */
    fun abrirInstagram(
        context: Context,
        usuarioInstagram: String = perfilInstagramOficial
    ) {
        val usernameLimpo = usuarioInstagram
            .removePrefix("@")
            .removePrefix("https://instagram.com/")
            .removePrefix("https://www.instagram.com/")
            .trim()

        if (usernameLimpo.isBlank()) {
            Toast.makeText(context, "Instagram da loja ainda não configurado.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            // 1. Tenta abrir no aplicativo nativo do Instagram
            val appUri = Uri.parse("http://instagram.com/_u/$usernameLimpo")
            val appIntent = Intent(Intent.ACTION_VIEW, appUri).apply {
                setPackage("com.instagram.android")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(appIntent)
        } catch (_: Exception) {
            // 2. Fallback: abre no navegador do celular caso o app nativo não esteja disponível
            try {
                val webUri = Uri.parse("https://instagram.com/$usernameLimpo")
                val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(webIntent)
            } catch (_: Exception) {
                Toast.makeText(
                    context,
                    "Não foi possível abrir o Instagram. Verifique sua conexão.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
