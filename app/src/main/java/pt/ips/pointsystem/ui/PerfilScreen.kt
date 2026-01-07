package pt.ips.pointsystem.ui

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import pt.ips.pointsystem.services.AccountService
import pt.ips.pointsystem.services.AppWriteClient
import pt.ips.pointsystem.ui.theme.BackgroundColor
import pt.ips.pointsystem.ui.theme.CardBackgroundColor
import pt.ips.pointsystem.ui.theme.TextDark
import pt.ips.pointsystem.ui.theme.TextGrey

// --- Definição de Cores específicas do PerfilScreen ---
val PrimaryBlue = Color(0xFF2D4B73) // Azul do avatar
val LightGreyItem = Color(0xFFF1F5F9) // Fundo dos items de texto
val RedLogout = Color(0xFFB00020)


@Composable
fun PerfilScreen(navController: NavController) {
    // Scroll state para permitir rolar a tela se o ecrã for pequeno
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf("Nome") }
    var email by remember { mutableStateOf("Email") }

    LaunchedEffect(Unit) {
        val account = AccountService(AppWriteClient.client).getLoggedIn()

        if (account != null) {
            name = account.name
            email = account.email
        }
    }

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
        ProfileHeaderCard(name)

        Spacer(modifier = Modifier.height(16.dp))

        // --- Cartão de Informações Pessoais ---
        PersonalInfoCard(name, email)

        Spacer(modifier = Modifier.height(24.dp))

        // --- Botão de Logout ---
        LogoutButton(navController)

        // Espaço extra no final
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ProfileHeaderCard(name: String) {
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
                    text = name.take(2),
                    color = Color.White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = name,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = TextDark
            )
        }
    }
}

@Composable
fun PersonalInfoCard(name: String, email: String) {
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

            InfoItemRow(icon = Icons.Outlined.Person, label = "Nome completo", value = name)
            Spacer(modifier = Modifier.height(12.dp))
            InfoItemRow(icon = Icons.Outlined.Email, label = "E-mail", value = email)
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
fun LogoutButton(navController: NavController) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    AppWriteClient.getInstance(context)
    val accountService = AppWriteClient.account

    OutlinedButton(
        onClick = {
            coroutineScope.launch {
                accountService.logout()
                navController.navigate("login") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = RedLogout
        ),
        border = BorderStroke(1.dp, RedLogout),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Outlined.ExitToApp,
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
        PerfilScreen(rememberNavController())
    }
}