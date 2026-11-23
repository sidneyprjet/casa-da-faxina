package com.example.myapplication.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.example.myapplication.data.local.SessionManager
import com.example.myapplication.data.model.Usuario
import com.example.myapplication.ui.carrinho.ItemCarrinho
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object WhatsAppHelper {

    // Número do WhatsApp da loja (configure antes de publicar)
    var numeroSarinhaWhatsApp: String = "5500000000000"

    fun definirNumeroSarinha(numero: String) {
        val apenasDigitos = numero.filter { it.isDigit() }
        numeroSarinhaWhatsApp = if (apenasDigitos.startsWith("55")) {
            apenasDigitos
        } else {
            "55$apenasDigitos"
        }
    }

    fun enviarPedido(
        context: Context,
        itens: List<ItemCarrinho>,
        usuario: Usuario?,
        numeroDestino: String = numeroSarinhaWhatsApp
    ) {
        if (itens.isEmpty()) {
            Toast.makeText(context, "Seu carrinho está vazio!", Toast.LENGTH_SHORT).show()
            return
        }

        val nomeCliente = if (usuario != null && usuario.nome.isNotBlank()) usuario.nome else "Cliente"
        val telefoneCliente = if (usuario != null && usuario.telefone.isNotBlank()) usuario.telefone else "Não informado"
        val subtotal = itens.sumOf { it.produto.preco * it.quantidade }

        // Registra o pedido no histórico para alimentar os "Mais Vendidos" por conversão no WhatsApp
        try {
            val sessionManager = SessionManager(context)
            itens.forEach { item ->
                sessionManager.registrarVendaWhatsApp(item.produto.id, item.quantidade)
            }
        } catch (_: Exception) {}

        val sb = StringBuilder()
        sb.append("✨ *OLÁ, SARINHA! NOVO PEDIDO - CASA DA FAXINA* ✨\n")
        sb.append("🙌 _(Mais uma bênção chegando, Sarinha!)_\n\n")
        sb.append("👤 *Cliente:* $nomeCliente\n")
        sb.append("📱 *Contato:* $telefoneCliente\n\n")
        sb.append("📦 *ITENS ESCOLHIDOS:*\n")

        itens.forEach { item ->
            val precoTotalItem = item.produto.preco * item.quantidade
            val precoFormatado = if (item.produto.precoSobConsulta) "Sob consulta" else "R$ ${"%.2f".format(precoTotalItem).replace('.', ',')}"
            sb.append("• ${item.quantidade}x ${item.produto.nome} ($precoFormatado)\n")
        }

        sb.append("\n💰 *Subtotal Estimado:* R$ ${"%.2f".format(subtotal).replace('.', ',')}\n")
        sb.append("🚚 *Entrega:* A combinar com você\n\n")
        sb.append("🙏 *Muito obrigado pela preferência e pela confiança!*\n")
        sb.append("✨ _Que Deus abençoe grandemente o seu lar!_\n\n")
        sb.append("🏠 _Pedido enviado diretamente pelo App Oficial Casa da Faxina_")

        abrirWhatsApp(context, numeroDestino, sb.toString())
    }

    fun abrirSuporte(
        context: Context,
        numeroDestino: String = numeroSarinhaWhatsApp,
        mensagemInicial: String = "Olá, Sarinha! Tudo bem? Estou no aplicativo da Casa da Faxina e gostaria de tirar uma dúvida sobre os produtos."
    ) {
        abrirWhatsApp(context, numeroDestino, mensagemInicial)
    }

    private fun abrirWhatsApp(context: Context, telefone: String, mensagem: String) {
        try {
            val mensagemCodificada = URLEncoder.encode(mensagem, StandardCharsets.UTF_8.toString())
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$telefone&text=$mensagemCodificada")
            val intent = Intent(Intent.ACTION_VIEW, uri).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(
                context,
                "Não foi possível abrir o WhatsApp diretamente. Verifique se o aplicativo está instalado.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
