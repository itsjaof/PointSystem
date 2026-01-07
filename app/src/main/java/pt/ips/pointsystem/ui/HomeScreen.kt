@file:Suppress("AssignedValueIsNeverRead", "AssignedValueIsNeverRead", "AssignedValueIsNeverRead")

package pt.ips.pointsystem.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.appwrite.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ips.pointsystem.services.AccountService
import pt.ips.pointsystem.services.AppWriteClient
import pt.ips.pointsystem.services.DatabaseService
import pt.ips.pointsystem.ui.theme.BackgroundColor
import pt.ips.pointsystem.ui.theme.TextDark
import pt.ips.pointsystem.ui.theme.TextGrey
import java.util.Locale

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    var isRefreshing by remember { mutableStateOf(false) }

    // Estados para as estatísticas
    var userName by remember { mutableStateOf("Utilizador") }
    var todayWorkHours by remember { mutableStateOf("00h 00m") }
    var lastActionType by remember { mutableStateOf("Nenhuma") }
    var lastActionTime by remember { mutableStateOf("--:--") }
    var recentActivity by remember { mutableStateOf<List<Pair<String, String>>>(emptyList()) }

    suspend fun loadData() {
        withContext(Dispatchers.IO) {
            val account = AccountService(AppWriteClient.client).getLoggedIn()
            if (account != null) {
                userName = account.name

                val zoneId = java.time.ZoneId.systemDefault()
                val now = java.time.ZonedDateTime.now(zoneId)
                val todayStr = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE.format(now) // "2023-10-05"

                val docs = DatabaseService().listDocuments(
                    "692f2e1a002875f2f416",
                    listOf(
                        Query.equal("userId", account.id),
                        Query.orderDesc("timestamp"),
                        Query.limit(20)
                    )
                )

                // 1. Processar Última Ação
                val lastDoc = docs.firstOrNull()
                if (lastDoc != null) {
                    val type = lastDoc.data["type"] as? String ?: "ENTRADA"
                    val ts = lastDoc.data["timestamp"] as? String

                    lastActionType = when(type) {
                        "ENTRADA" -> "Entrada"
                        "SAIDA" -> "Saída"
                        "PAUSA_INICIO" -> "Em Pausa"
                        "PAUSA_FIM" -> "A Trabalhar"
                        else -> type
                    }

                    if (ts != null) {
                        try {
                            val instant = try {
                                java.time.OffsetDateTime.parse(ts).toInstant()
                            } catch (e: Exception) {
                                java.time.Instant.parse(ts)
                            }

                            val zonedDateTime = instant.atZone(zoneId)
                            lastActionTime = java.time.format.DateTimeFormatter.ofPattern("HH:mm").format(zonedDateTime)
                        } catch (_: Exception) {
                            lastActionTime = "--:--"
                        }
                    }
                }

                // 2. Processar Atividade Recente
                recentActivity = docs.take(3).mapNotNull { doc ->
                    val type = doc.data["type"] as? String ?: return@mapNotNull null
                    val ts = doc.data["timestamp"] as? String ?: return@mapNotNull null

                    var timeStr = "--:--"
                    try {
                        val instant = try {
                            java.time.OffsetDateTime.parse(ts).toInstant()
                        } catch (e: Exception) {
                            java.time.Instant.parse(ts)
                        }
                        timeStr = java.time.format.DateTimeFormatter.ofPattern("HH:mm")
                            .format(instant.atZone(zoneId))
                    } catch (_: Exception) {}

                    val label = when(type) {
                        "ENTRADA" -> "Entrada registada"
                        "SAIDA" -> "Saída registada"
                        "PAUSA_INICIO" -> "Início de pausa"
                        "PAUSA_FIM" -> "Fim de pausa"
                        else -> "Registo efetuado"
                    }

                    label to timeStr
                }

                // 3. Cálculo de Horas Trabalhadas Hoje
                // Filtra registos onde a data local (yyyy-MM-dd) corresponde a hoje
                val todayDocs = docs.filter { doc ->
                    val ts = doc.data["timestamp"] as? String ?: return@filter false
                    try {
                        val instant = try {
                            java.time.OffsetDateTime.parse(ts).toInstant()
                        } catch (e: Exception) {
                            java.time.Instant.parse(ts)
                        }
                        val docDateStr = java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
                            .format(instant.atZone(zoneId))
                        docDateStr == todayStr
                    } catch (e: Exception) { false }
                }.sortedBy { doc ->
                    // Ordena cronologicamente para o cálculo (Antigo -> Recente)
                    doc.data["timestamp"] as? String ?: ""
                }

                var accumulatedMillis = 0L
                var currentWorkStart: Long? = null

                for (doc in todayDocs) {
                    val type = doc.data["type"] as? String
                    val ts = doc.data["timestamp"] as? String ?: continue

                    var eventTime: Long
                    try {
                        val instant = try {
                            java.time.OffsetDateTime.parse(ts).toInstant()
                        } catch (e: Exception) {
                            java.time.Instant.parse(ts)
                        }
                        eventTime = instant.toEpochMilli()
                    } catch (_: Exception) { continue }

                    when (type) {
                        "ENTRADA" -> {
                            if (currentWorkStart == null) currentWorkStart = eventTime
                        }
                        "PAUSA_INICIO" -> {
                            if (currentWorkStart != null) {
                                accumulatedMillis += (eventTime - currentWorkStart)
                                currentWorkStart = null
                            }
                        }
                        "PAUSA_FIM" -> {
                            if (currentWorkStart == null) currentWorkStart = eventTime
                        }
                        "SAIDA" -> {
                            if (currentWorkStart != null) {
                                accumulatedMillis += (eventTime - currentWorkStart)
                                currentWorkStart = null
                            }
                        }
                    }
                }

                // Se ainda estiver a trabalhar, soma até agora
                if (currentWorkStart != null) {
                    accumulatedMillis += (now.toInstant().toEpochMilli() - currentWorkStart)
                }

                if (accumulatedMillis > 0) {
                    val hours = accumulatedMillis / (1000 * 60 * 60)
                    val minutes = (accumulatedMillis / (1000 * 60)) % 60
                    todayWorkHours = String.format(Locale.US, "%02dh %02dm", hours, minutes)
                } else {
                    todayWorkHours = "00h 00m"
                }
            }
        }
    }


    LaunchedEffect(Unit) {
        loadData()
    }

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                loadData()
                isRefreshing = false
            }
        },
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            // Cabeçalho de Boas-vindas
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Olá, $userName",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Text(
                        text = "Tenha um bom dia de trabalho!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGrey
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Cartão de Estado Atual
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2E7D32)), // Verde
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Estado Atual",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = lastActionType,
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Text(
                        text = "Desde as $lastActionTime",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Estatísticas Rápidas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Horas Hoje
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Horas Hoje",
                    value = todayWorkHours,
                    icon = Icons.Default.AccessTime,
                    color = Color(0xFF1976D2) // Azul
                )

                // Atividade Recente (Resumo)
                StatCard(
                    modifier = Modifier.weight(1f),
                    title = "Última Ação",
                    value = lastActionTime,
                    icon = Icons.Default.History,
                    color = Color(0xFFE64A19) // Laranja
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Atividade Recente",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Atividade Recente
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (recentActivity.isEmpty()) {
                    Text("Sem atividade recente.", color = TextGrey)
                } else {
                    recentActivity.forEach { (action, time) ->
                        ActivityItem(action, time)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
    }

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = TextGrey
            )
        }
    }
}

@Composable
fun ActivityItem(title: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (title.contains("Entrada") || title.contains("Fim")) Color(0xFF2E7D32) else Color(0xFFF57C00))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = TextDark
            )
        }
        
        Text(
            text = time,
            style = MaterialTheme.typography.bodySmall,
            color = TextGrey
        )
    }
}