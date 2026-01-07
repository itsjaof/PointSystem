package pt.ips.pointsystem.ui

import android.location.Geocoder
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.appwrite.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.ips.pointsystem.services.AccountService
import pt.ips.pointsystem.services.AppWriteClient
import pt.ips.pointsystem.services.DatabaseService
import pt.ips.pointsystem.ui.theme.BackgroundColor
import pt.ips.pointsystem.ui.theme.TextDark
import pt.ips.pointsystem.ui.theme.TextGrey
import java.time.OffsetDateTime
import java.util.Locale

data class PicagemUi(
    val tipo: TipoPicagem,
    val hora: String,
    val local: String
)

enum class TipoPicagem {
    ENTRADA, SAIDA, PAUSA_INICIO, PAUSA_FIM
}

data class RegistoDia(
    val data: String,
    val picagens: List<PicagemUi>
)

@Preview
@Composable
fun HistoricoRoot() {
    Scaffold(
        bottomBar = { BottomNavBar(selected = 1) }
    ) { paddingValues ->
        HistoricoScreen(Modifier.padding(paddingValues))
    }
}


@Composable
fun HistoricoScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var registos by remember { mutableStateOf<List<RegistoDia>>(emptyList()) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val account = AccountService(AppWriteClient.client).getLoggedIn()
            if (account != null) {
                // 1. Obter dados da base de dados
                val documents = DatabaseService().listDocuments(
                    "692f2e1a002875f2f416",
                    listOf(Query.equal("userId", account.id))
                )

                // 2. Configurar formatadores e fuso horário
                // 'userZoneId' garante que a hora mostrada é a do telemóvel do utilizador
                val userZoneId = java.time.ZoneId.systemDefault()
                val timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
                val dateFormatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.getDefault())

                val geocoder = Geocoder(context, Locale.getDefault())

                // 3. Processar e transformar os dados
                val grouped = documents.mapNotNull { doc ->
                    val data = doc.data

                    // Leitura segura dos dados brutos
                    val timestampStr = data["timestamp"] as? String ?: return@mapNotNull null
                    val typeStr = data["type"] as? String
                    val lat = data["latitude"].toString().toDoubleOrNull() ?: 0.0
                    val lon = data["longitude"].toString().toDoubleOrNull() ?: 0.0

                    // PARSING INTELIGENTE (Substitui a antiga função parseTimestamp)
                    // Instant.parse lê nativamente o formato ISO 8601 (ex: 2023-10-05T14:30:00.000Z)
                    val instant = try {
                        OffsetDateTime.parse(timestampStr).toInstant()
                    } catch (e: Exception) {
                        return@mapNotNull null // Ignora registos com datas inválidas
                    }

                    // Converter o momento UTC para a hora local do utilizador
                    val zonedDateTime = instant.atZone(userZoneId)

                    // Formatar Strings para a UI
                    val dateStr = zonedDateTime.format(dateFormatter)
                    val timeStr = zonedDateTime.format(timeFormatter)

                    // Mapeamento do Enum
                    val tipo = when (typeStr) {
                        "ENTRADA" -> TipoPicagem.ENTRADA
                        "SAIDA" -> TipoPicagem.SAIDA
                        "PAUSA_INICIO" -> TipoPicagem.PAUSA_INICIO
                        "PAUSA_FIM" -> TipoPicagem.PAUSA_FIM
                        else -> TipoPicagem.ENTRADA
                    }

                    // Geocoding (Converter coordenadas em morada)
                    var addressStr = String.format(Locale.getDefault(), "Lat: %.4f, Lon: %.4f", lat, lon)
                    try {
                        if (lat != 0.0 && lon != 0.0) {
                            @Suppress("DEPRECATION")
                            val addresses = geocoder.getFromLocation(lat, lon, 1)
                            if (!addresses.isNullOrEmpty()) {
                                val address = addresses[0]
                                addressStr = address.getAddressLine(0)
                                    ?: "${address.thoroughfare ?: ""}, ${address.locality ?: ""}"
                            }
                        }
                    } catch (_: Exception) { }

                    // Criar objeto visual
                    val picagem = PicagemUi(
                        tipo = tipo,
                        hora = timeStr,
                        local = addressStr
                    )

                    // Retorna par (Data, Picagem) para agrupar
                    dateStr to picagem
                }
                    .groupBy({ it.first }, { it.second }) // Agrupa por Data (String)
                    .map { (dateStr, picagens) ->
                        // Cria o registo do dia com as picagens ordenadas (mais recente primeiro)
                        RegistoDia(dateStr, picagens.sortedByDescending { it.hora })
                    }
                    .sortedByDescending { registoDia ->
                        // Ordena a lista final de dias (hoje primeiro, ontem depois...)
                        try {
                            java.time.LocalDate.parse(registoDia.data, dateFormatter).toEpochDay()
                        } catch (e: Exception) {
                            0L
                        }
                    }

                registos = grouped
            }
        }
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = "Histórico de Picagens",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Consulte os seus registos anteriores",
            color = TextGrey,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(registos.size) { index ->
                val registo = registos[index]

                Text(
                    text = registo.data,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 6.dp)
                )

                registo.picagens.forEach { picagem ->
                    PicagemItem(picagem)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
            
            if (registos.isEmpty()) {
                item {
                    Text(
                        text = "Sem registos encontrados.",
                        modifier = Modifier.padding(top = 20.dp),
                        color = TextGrey
                    )
                }
            }
        }
    }
}

@Composable
fun PicagemItem(picagem: PicagemUi) {

    val (icon, backgroundColor, title) = when (picagem.tipo) {
        TipoPicagem.ENTRADA ->
            Triple(Icons.AutoMirrored.Filled.Login, Color(0xFFDFF5E1), "Entrada")

        TipoPicagem.SAIDA ->
            Triple(Icons.AutoMirrored.Filled.Logout, Color(0xFFE3E8FF), "Saída")

        TipoPicagem.PAUSA_INICIO ->
            Triple(Icons.Default.Pause, Color(0xFFFFF2CC), "Início Pausa")
            
        TipoPicagem.PAUSA_FIM ->
            Triple(Icons.Default.PlayArrow, Color(0xFFE0F7FA), "Fim Pausa")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {

        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .background(backgroundColor, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null
                )
            }

            Spacer(Modifier.width(12.dp))

            Column {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(picagem.hora, style = MaterialTheme.typography.bodyMedium)
                Text(
                    picagem.local,
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}



@Composable
fun BottomNavBar(selected: Int) {

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 10.dp
    ) {
        NavigationBarItem(
            selected = selected == 0,
            onClick = {},
            icon = { Icon(imageVector = Icons.Default.Home, contentDescription = null) },
            label = { Text("Início") }
        )

        NavigationBarItem(
            selected = selected == 1,
            onClick = {},
            icon = { Icon(imageVector = Icons.Default.History, contentDescription = null) },
            label = { Text("Histórico") }
        )

        NavigationBarItem(
            selected = selected == 2,
            onClick = {},
            icon = { Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null) },
            label = { Text("Perfil") }
        )
    }
}
