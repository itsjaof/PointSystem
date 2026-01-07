package pt.ips.pointsystem.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay
import pt.ips.pointsystem.model.PicagemType
import pt.ips.pointsystem.services.AccountService
import pt.ips.pointsystem.services.AppWriteClient
import pt.ips.pointsystem.services.DatabaseService
import pt.ips.pointsystem.ui.theme.BackgroundColor
import pt.ips.pointsystem.ui.theme.CardBackgroundColor
import pt.ips.pointsystem.ui.theme.CardBorderColor
import pt.ips.pointsystem.ui.theme.TextDark
import pt.ips.pointsystem.ui.theme.TextGrey
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.OffsetDateTime
import java.util.Date
import java.util.Locale

@Preview
@Composable
fun PontoScreen(
    navController: NavController = rememberNavController(),
    onTypeSelected: (PicagemType) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var currentTime by remember { mutableStateOf(Date()) }
    val context = LocalContext.current
    var lastPicagemType by remember { mutableStateOf<String?>(null) }
    var lastPicagemTime by remember { mutableStateOf("--:--") }


// Carregar o estado atual (última picagem)
    LaunchedEffect(Unit) {
        val account = AccountService(AppWriteClient.client).getLoggedIn()
        if (account != null) {
            val lastDoc = DatabaseService().getLastPicagem("692f2e1a002875f2f416", account.id)
            if (lastDoc != null) {
                lastPicagemType = lastDoc.data["type"] as? String
                val timestamp = lastDoc.data["timestamp"] as? String

                if (timestamp != null) {
                    try {
                        // 1. Tentar ler formato Appwrite (ISO com Offset +00:00)
                        val instant = try {
                            OffsetDateTime.parse(timestamp).toInstant()
                        } catch (e: Exception) {
                            // 2. Fallback para formato antigo com 'Z'
                            Instant.parse(timestamp)
                        }

                        // 3. Converter para o fuso horário do telemóvel do utilizador
                        val zoneId = java.time.ZoneId.systemDefault()
                        val zonedDateTime = instant.atZone(zoneId)

                        // 4. Formatar para mostrar na UI (HH:mm)
                        val formatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm")
                        lastPicagemTime = zonedDateTime.format(formatter)

                    } catch (e: Exception) {
                        e.printStackTrace() // Data inválida, mantém "--:--"
                    }
                }
            }
        }

        while (true) {
            delay(1000)
            currentTime = Date()
        }
    }

    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.forLanguageTag("pt-PT"))
    val dateFormat = SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", Locale.forLanguageTag("pt-PT"))

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Ponto",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Registe as suas picagens",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGrey,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // ---------------- RELÓGIO ----------------
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CardBorderColor, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = dateFormat.format(currentTime),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGrey
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = timeFormat.format(currentTime),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // ---------------- ESTADO ATUAL ----------------
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, CardBorderColor, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE2E6EA)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Alarm,
                        contentDescription = null,
                        tint = TextDark,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Estado Atual",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = TextDark
                    )
                    
                    val estadoTexto = when(lastPicagemType) {
                        "ENTRADA", "PAUSA_FIM" -> "Em Serviço"
                        "PAUSA_INICIO" -> "Em Pausa"
                        "SAIDA" -> "Fora de Serviço"
                        else -> "Sem registo"
                    }
                    
                    Text(
                        text = estadoTexto,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGrey
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Desde",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextGrey
                    )
                    Text(
                        text = lastPicagemTime,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = TextDark
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // ---------------- BOTÕES ----------------
        
        // Lógica de validação:
        // Só pode ENTRADA se o último for SAIDA ou nulo (início)
        // Só pode PAUSA se o último for ENTRADA ou PAUSA_FIM
        // Se estiver em PAUSA, o botão PAUSA transforma-se em PAUSA_FIM
        // Só pode SAIDA se o último for ENTRADA, PAUSA_INICIO ou PAUSA_FIM

        Button(
            onClick = {
                if (lastPicagemType == null || lastPicagemType == "SAIDA") {
                    onTypeSelected(PicagemType.ENTRADA)
                    navController.navigate("nfcScan")
                } else {
                    Toast.makeText(context, "Já se encontra em serviço ou em pausa.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (lastPicagemType == null || lastPicagemType == "SAIDA") Color(0xFF2E7D32) else Color.Gray
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Registar Entrada",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        val isPaused = lastPicagemType == "PAUSA_INICIO"
        val canPause = lastPicagemType == "ENTRADA" || lastPicagemType == "PAUSA_FIM"
        
        Button(
            onClick = {
                if (isPaused) {
                    onTypeSelected(PicagemType.PAUSA_FIM)
                    navController.navigate("nfcScan")
                } else if (canPause) {
                    onTypeSelected(PicagemType.PAUSA_INICIO)
                    navController.navigate("nfcScan")
                } else {
                    Toast.makeText(context, "Só pode pausar se estiver em serviço.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPaused) Color(0xFF0288D1) else if (canPause) Color(0xFFF57C00) else Color.Gray
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                if (isPaused) "Retomar Trabalho" else "Registar Pausa",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (canPause || isPaused) {
                    onTypeSelected(PicagemType.SAIDA)
                    navController.navigate("nfcScan")
                } else {
                    Toast.makeText(context, "Não tem entrada registada.", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (canPause || isPaused) Color(0xFFD32F2F) else Color.Gray
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                "Registar Saída",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}
