package pt.ips.pointsystem.ui

import android.preference.PreferenceActivity
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF9712FA),
                            Color(0xFF5039F7)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            Header()

            Status()

            GridInformation()

        }
    }
}

//@Preview
@Composable
fun Header(){

    var currentTime by remember { mutableStateOf(Date()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            currentTime = Date()
        }
    }

    var time = SimpleDateFormat("HH:mm", Locale("pt", "PT"))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Column {
            Text(
                text = "Bem-vindo de volta,",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(4.dp)
            )
            Text(
                text = "Tiago Dores",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(4.dp)
            )
            Text(
                text = "Desenvolvedor",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .padding(4.dp)
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "Agora",
                color = Color.White,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .padding( top = 15.dp, end = 8.dp)

            )
            Text(
                text = time.format(currentTime),
                color = Color.White,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp,
                modifier = Modifier
                    .padding(end = 8.dp)
            )
        }
    }

}

//@Preview
@Composable
fun Status(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF7724F9)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Trabalhando",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Desde 08:45",
                    color = Color.White.copy(alpha = 0.7f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Text(
                text = "5.5h hoje",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

//@Preview
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
                imageVector = Icons.Default.ShowChart,
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
        modifier = modifier.height(250.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(
                imageVector = imageVector,
                contentDescription = null,
                tint = iconColor
            )

            Spacer(modifier = Modifier.height(30.dp))
            Text(text = title, style = MaterialTheme.typography.bodyMedium)

            Text(text = "5.5h", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(70.dp))

            val progress = 0.69f // 69%
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(4.dp),
                color = iconColor,
                trackColor = Color.LightGray
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Meta: 8h", style = MaterialTheme.typography.bodySmall)
                Text(text = "69%", style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}
