package pt.ips.pointsystem.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import pt.ips.pointsystem.ui.theme.BackgroundColor
import pt.ips.pointsystem.ui.theme.CardBackgroundColor
import pt.ips.pointsystem.ui.theme.CardBorderColor
import pt.ips.pointsystem.ui.theme.TextDark
import pt.ips.pointsystem.ui.theme.TextGrey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(horizontal = 16.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "Ecrã Inicial",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.padding(top = 16.dp)
        )
        Text(
            text = "Visão geral do seu trabalho",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGrey,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Status()

        Spacer(modifier = Modifier.height(16.dp))

        GridInformation()
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}


@Composable
fun Status(){
    var currentTime by remember { mutableStateOf(Date()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = Date()
        }
    }

    val time = SimpleDateFormat("HH:mm", Locale.forLanguageTag("pt-PT"))
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackgroundColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Estado Atual",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = TextDark
                )
                Text(
                    text = "Trabalhando desde 08:45",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGrey
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = time.format(currentTime),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Text(
                    text = "5.5h hoje",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGrey
                )
            }
        }
    }
}

@Composable
fun GridInformation(){
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            StatisticBlock(
                title = "Hoje",
                iconColor = Color(0xFF1E88E5),
                imageVector = Icons.Default.AccessTime,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            StatisticBlock(
                title = "Esta Semana",
                iconColor = Color(0xFF43A047),
                imageVector = Icons.Default.CalendarMonth,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatisticBlock(
                title = "Este Mês",
                iconColor = Color(0xFF9C27B0),
                imageVector = Icons.AutoMirrored.Filled.ShowChart,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(16.dp))
            StatisticBlock(
                title = "Banco de Horas",
                iconColor = Color(0xFF00ACC1),
                imageVector = Icons.Default.Info,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
@Composable
fun StatisticBlock(
    title: String,
    iconColor: Color,
    imageVector: ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(200.dp)
            .border(1.dp, CardBorderColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = TextGrey,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "5.5h",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(20.dp))

            val progress = 0.69f // 69%
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(6.dp),
                color = iconColor,
                trackColor = Color(0xFFE0E0E0)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Meta: 8h",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextGrey
                )
                Text(
                    text = "69%",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = TextDark
                )
            }
        }
    }
}