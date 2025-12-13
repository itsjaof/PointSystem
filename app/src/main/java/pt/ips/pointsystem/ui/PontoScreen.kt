package pt.ips.pointsystem.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Preview
@Composable
fun PontoScreen() {
    var currentTime by remember { mutableStateOf(Date()) }

    LaunchedEffect(Unit) {
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
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "Olá, (Nome)",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // ---------------- RELÓGIO ----------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF003366), // Roxo
                            Color.Black       // Preto
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = dateFormat.format(currentTime),
                    fontSize = 16.sp,
                    color = Color.White
                )
                Text(
                    text = timeFormat.format(currentTime),
                    fontSize = 52.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Lisboa, Portugal",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ---------------- ESTADO ATUAL (ICON MINIMALISTA) ----------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(55.dp)
                    .background(
                        color = Color(0xFFE5E5E5),
                        shape = RoundedCornerShape(50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                // Ícone Minimalista -> Círculo Vazio
                Icon(
                    imageVector = Icons.Default.Alarm,
                    contentDescription = null
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column {
                Text(
                    text = "Estado Atual",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Em Serviço",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Desde",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "08:45",
                    fontSize = 18.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ---------------- LOCALIZAÇÃO ATUAL (ICON MINIMALISTA) ----------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(95.dp)
                .background(
                    Color(0xFFF0F0F0),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(55.dp)
                        .background(
                            Color(0xFFE5E5E5),
                            shape = RoundedCornerShape(50)
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    // Ícone minimalista estilo GPS
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null
                        )


                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Localização Atual",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Avenida da Liberdade, 11, Lisboa",
                        fontSize = 15.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // ---------------- BOTÕES ----------------
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(60.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF006400)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Registar Entrada", fontSize = 17.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(60.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFFFB800)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Registar Pausa", fontSize = 17.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth().height(60.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFFFF0000)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("Registar Saída", fontSize = 17.sp, color = Color.White)
        }
    }
}
