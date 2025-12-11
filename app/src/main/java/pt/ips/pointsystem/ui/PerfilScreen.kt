import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Definição de Cores aproximadas ---
val BackgroundColor = Color(0xFFF8F9FA) // Cinza muito claro para o fundo da tela
val CardBackgroundColor = Color.White
val PrimaryBlue = Color(0xFF2D4B73) // Azul do avatar
val LightGreyItem = Color(0xFFF1F5F9) // Fundo dos items de texto
val GreenStats = Color(0xFF00C853) // Verde do ícone de horas
val GreenStatsBg = Color(0xFFE8F5E9) // Fundo verde claro
val RedLogout = Color(0xFFB00020)
val TextDark = Color(0xFF1A1C1E)
val TextGrey = Color(0xFF6C757D)

@Composable
fun PerfilScreen() {
    // Scroll state para permitir rolar a tela se o ecrã for pequeno
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // --- Cabeçalho ---
        Text(
            text = "Perfil",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Text(
            text = "Gerir as suas informações",
            style = MaterialTheme.typography.bodyMedium,
            color = TextGrey,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // --- Cartão do Avatar ---
        ProfileHeaderCard()

        Spacer(modifier = Modifier.height(16.dp))

        // --- Cartão de Informações Pessoais ---
        PersonalInfoCard()

        Spacer(modifier = Modifier.height(16.dp))

        // --- Cartão de Estatísticas ---
        StatisticsCard()

        Spacer(modifier = Modifier.height(16.dp))

        // --- Cartão de Definições ---
        SettingsCard()

        Spacer(modifier = Modifier.height(24.dp))

        // --- Botão de Logout ---
        LogoutButton()

        // Espaço extra no final
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ProfileHeaderCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar Circular
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "FG",
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Fgdsg",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextDark
            )
            Text(
                text = "Colaborador",
                fontSize = 14.sp,
                color = TextGrey
            )
        }
    }
}

@Composable
fun PersonalInfoCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Informações Pessoais",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = TextDark,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            InfoItemRow(icon = Icons.Outlined.Person, label = "Nome completo", value = "Fgdsg")
            Spacer(modifier = Modifier.height(12.dp))
            InfoItemRow(icon = Icons.Outlined.Email, label = "E-mail", value = "fgdsg")
            Spacer(modifier = Modifier.height(12.dp))
            // Usando ícone genérico de hashtag para número
            InfoItemRow(icon = Icons.Default.Info, label = "Número de funcionário", value = "59687")
        }
    }
}

@Composable
fun InfoItemRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(LightGreyItem)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Círculo do ícone
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFFE2E6EA)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = PrimaryBlue,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = TextGrey
            )
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = TextDark
            )
        }
    }
}

@Composable
fun StatisticsCard() {
    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Estatísticas",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = TextDark,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Container Verde
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(GreenStatsBg) // Fundo verde claro
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(GreenStats),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange, // Ícone de relógio/tempo
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Total de horas trabalhadas",
                        fontSize = 13.sp,
                        color = Color(0xFF2E7D32) // Verde escuro para texto
                    )
                    Text(
                        text = "17.8h",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsCard() {
    // Estado do switch (apenas visual, pois pediste estático)
    val isDarkTheme = remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = CardBackgroundColor),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Definições",
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = TextDark,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Settings, // Ou ícone de sol
                        contentDescription = null,
                        tint = TextDark
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Modo escuro",
                        fontWeight = FontWeight.Medium,
                        color = TextDark
                    )
                }

                Switch(
                    checked = isDarkTheme.value,
                    onCheckedChange = { isDarkTheme.value = it }
                )
            }
        }
    }
}

@Composable
fun LogoutButton() {
    OutlinedButton(
        onClick = { /* Ação vazia */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = RedLogout
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, RedLogout),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(
            imageVector = Icons.Outlined.ExitToApp,
            contentDescription = null,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Terminar sessão", fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    MaterialTheme {
        PerfilScreen()
    }
}