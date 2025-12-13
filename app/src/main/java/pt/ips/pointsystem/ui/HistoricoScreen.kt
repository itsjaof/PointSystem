package pt.ips.pointsystem.ui

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


data class Picagem(
    val tipo: TipoPicagem,
    val hora: String,
    val local: String
)

enum class TipoPicagem {
    ENTRADA, SAIDA, PAUSA
}

data class RegistoDia(
    val data: String,
    val picagens: List<Picagem>
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

    val registos = listOf(
        RegistoDia(
            "04/12/2025",
            listOf(
                Picagem(TipoPicagem.SAIDA, "18:00", "Escritório Principal, Lisboa"),
                Picagem(TipoPicagem.PAUSA, "12:30", "Escritório Principal, Lisboa"),
                Picagem(TipoPicagem.ENTRADA, "09:00", "Escritório Principal, Lisboa")
            )
        ),
        RegistoDia(
            "03/12/2025",
            listOf(
                Picagem(TipoPicagem.SAIDA, "17:30", "Escritório Principal, Lisboa"),
                Picagem(TipoPicagem.PAUSA, "13:00", "Escritório Principal, Lisboa"),
                Picagem(TipoPicagem.ENTRADA, "08:45", "Escritório Principal, Lisboa")
            )
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Text(
            text = "Histórico de Picagens",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 16.dp)
        )

        Text(
            text = "Consulte os seus registos anteriores",
            color = Color.Gray,
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
        }
    }
}



@Composable
fun PicagemItem(picagem: Picagem) {

    val (icon, backgroundColor, title) = when (picagem.tipo) {
        TipoPicagem.ENTRADA ->
            Triple(Icons.AutoMirrored.Filled.Login, Color(0xFFDFF5E1), "Entrada")

        TipoPicagem.SAIDA ->
            Triple(Icons.AutoMirrored.Filled.Logout, Color(0xFFE3E8FF), "Saída")

        TipoPicagem.PAUSA ->
            Triple(Icons.Default.Pause, Color(0xFFFFF2CC), "Pausa")
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




