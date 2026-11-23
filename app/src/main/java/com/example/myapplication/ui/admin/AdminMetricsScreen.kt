package com.example.myapplication.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import com.example.myapplication.ui.theme.MyApplicationTheme
import java.util.concurrent.TimeUnit

private val AzulMetricas = Color(0xFF0047BB)
private val AmareloMetricas = Color(0xFFFFD500)
private val FundoMetricas = Color(0xFFF3F4F6)

/**
 * Snapshot de audiência isolado da UI para conectar futuramente a Analytics/backend.
 * A tela identifica explicitamente os valores de demonstração.
 */
data class AdminAudienceMetrics(
    val activeUsersNow: Int,
    val newVisitors: Int,
    val returningVisitors: Int,
    val sessions: Int,
    val convertingSessions: Int,
    val cartAdditions: Int,
    val whatsappClicks: Int
) {
    private val safeNewVisitors get() = newVisitors.coerceAtLeast(0)
    private val safeReturningVisitors get() = returningVisitors.coerceAtLeast(0)
    private val totalVisitors get() = safeNewVisitors + safeReturningVisitors

    val newVisitorPercent: Int
        get() = if (totalVisitors == 0) 0
        else (safeNewVisitors * 100.0 / totalVisitors).roundToInt().coerceIn(0, 100)

    val returningVisitorPercent: Int
        get() = if (totalVisitors == 0) 0
        else 100 - newVisitorPercent

    val conversionRatePercent: Int
        get() {
            val sessionCount = sessions.coerceAtLeast(0)
            if (sessionCount == 0) return 0
            return (convertingSessions.coerceAtLeast(0) * 100.0 / sessionCount)
                .roundToInt()
                .coerceIn(0, 100)
        }

    companion object {
        val demo = AdminAudienceMetrics(
            activeUsersNow = 6,
            newVisitors = 34,
            returningVisitors = 66,
            sessions = 100,
            convertingSessions = 23,
            cartAdditions = 18,
            whatsappClicks = 9
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminMetricsScreen(
    pageAccessCounts: Map<String, Int>,
    topAddedProducts: List<Pair<String, Int>>,
    averageUsageMillis: Long,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    audienceMetrics: AdminAudienceMetrics = AdminAudienceMetrics.demo
) {
    val paginas = listOf("Home", "Categorias", "Carrinho", "Perfil")
    val maiorAcesso = (paginas.maxOfOrNull { pageAccessCounts[it] ?: 0 } ?: 0).coerceAtLeast(1)
    val minutos = TimeUnit.MILLISECONDS.toMinutes(averageUsageMillis.coerceAtLeast(0))
    val segundos = TimeUnit.MILLISECONDS.toSeconds(averageUsageMillis.coerceAtLeast(0)) % 60

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = FundoMetricas,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Métricas do App",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar ao painel",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AzulMetricas,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = AzulMetricas),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Usuários ativos agora",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Estimativa demonstrativa",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = audienceMetrics.activeUsersNow.coerceAtLeast(0).toString(),
                                color = AmareloMetricas,
                                fontSize = 38.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text("ONLINE · SIMULADO", color = Color.White, fontSize = 10.sp)
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "Novos vs. recorrentes",
                            color = AzulMetricas,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Distribuição simulada de clientes",
                            color = Color(0xFF64748B),
                            fontSize = 12.sp
                        )
                        AudienceMetricBar(
                            label = "Primeira visita",
                            count = audienceMetrics.newVisitors.coerceAtLeast(0),
                            percent = audienceMetrics.newVisitorPercent,
                            color = AmareloMetricas
                        )
                        AudienceMetricBar(
                            label = "Recorrentes",
                            count = audienceMetrics.returningVisitors.coerceAtLeast(0),
                            percent = audienceMetrics.returningVisitorPercent,
                            color = AzulMetricas
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Taxa de conversão estimada",
                            color = AzulMetricas,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${audienceMetrics.conversionRatePercent}%",
                                modifier = Modifier.weight(0.7f),
                                color = AzulMetricas,
                                fontSize = 36.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Column(
                                modifier = Modifier.weight(1.3f),
                                horizontalAlignment = Alignment.End
                            ) {
                                Text(
                                    text = "${audienceMetrics.cartAdditions.coerceAtLeast(0)} adições ao carrinho",
                                    color = Color(0xFF25232A),
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = "${audienceMetrics.whatsappClicks.coerceAtLeast(0)} cliques no WhatsApp",
                                    color = Color(0xFF25232A),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Text(
                            text = "${audienceMetrics.convertingSessions.coerceAtLeast(0)} de ${audienceMetrics.sessions.coerceAtLeast(0)} sessões tiveram ao menos uma dessas ações. Valores simulados.",
                            color = Color(0xFF64748B),
                            fontSize = 12.sp
                        )
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Text(
                            text = "🏆  Produtos mais adicionados",
                            color = AzulMetricas,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (topAddedProducts.isEmpty()) {
                            Text(
                                text = "Os toques no botão + aparecerão aqui.",
                                color = Color(0xFF475569)
                            )
                        } else {
                            topAddedProducts.take(3).forEachIndexed { index, (name, count) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "${index + 1}. $name",
                                        modifier = Modifier.weight(1f),
                                        color = Color(0xFF25232A),
                                        fontWeight = FontWeight.Medium
                                    )
                                    Text(
                                        text = "$count adições",
                                        color = AzulMetricas,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "📊  Páginas mais acessadas",
                            color = AzulMetricas,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        paginas.forEach { pagina ->
                            val acessos = pageAccessCounts[pagina] ?: 0
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(pagina, color = Color(0xFF25232A))
                                    Text(
                                        text = "$acessos acessos",
                                        color = AzulMetricas,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(10.dp)
                                        .background(
                                            AmareloMetricas.copy(alpha = 0.35f),
                                            RoundedCornerShape(8.dp)
                                        )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(
                                                (acessos.toFloat() / maiorAcesso).coerceIn(0f, 1f)
                                            )
                                            .height(10.dp)
                                            .background(
                                                AzulMetricas,
                                                RoundedCornerShape(8.dp)
                                            )
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = AzulMetricas),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "⏱  Tempo médio de uso",
                            color = AmareloMetricas,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$minutos minutos e $segundos segundos",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AudienceMetricBar(
    label: String,
    count: Int,
    percent: Int,
    color: Color
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = Color(0xFF25232A))
            Text(
                text = "$count · $percent%",
                color = AzulMetricas,
                fontWeight = FontWeight.SemiBold
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .background(AmareloMetricas.copy(alpha = 0.25f), RoundedCornerShape(8.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth((percent / 100f).coerceIn(0f, 1f))
                    .height(10.dp)
                    .background(color, RoundedCornerShape(8.dp))
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AdminMetricsScreenPreview() {
    MyApplicationTheme {
        AdminMetricsScreen(
            pageAccessCounts = mapOf(
                "Home" to 24,
                "Categorias" to 12,
                "Carrinho" to 18,
                "Perfil" to 6
            ),
            topAddedProducts = listOf(
                "Limpador para Laminado" to 8,
                "Limpador Porcelanato" to 5,
                "Pano de Microfibra Limpeza" to 3
            ),
            averageUsageMillis = 270_000L,
            onBack = {}
        )
    }
}
