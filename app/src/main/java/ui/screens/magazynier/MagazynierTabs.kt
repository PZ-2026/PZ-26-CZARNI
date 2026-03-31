package ui.screens.magazynier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- MODELE DANYCH ---
data class Zlecenie(
    val id: String,
    val typ: String, // "PRZYJĘCIE" lub "WYDANIE"
    val status: String,
    val data: String,
    val klient: String
)

data class MagazynierItem(
    val nazwa: String,
    val lokalizacja: String,
    val ilosc: Int,
    val jednostka: String
)

// --- ZAKŁADKA ZLECENIA ---
@Composable
fun ZleceniaTab() {
    val zlecenia = remember {
        listOf(
            Zlecenie("#WZ/2024/001", "WYDANIE", "W TRAKCIE", "2024-05-20", "Sklep Spożywczy ABC"),
            Zlecenie("#PZ/2024/042", "PRZYJĘCIE", "NOWE", "2024-05-21", "Hurtownia Nabiału"),
            Zlecenie("#WZ/2024/005", "WYDANIE", "GOTOWE", "2024-05-21", "Restauracja Smak"),
            Zlecenie("#PZ/2024/045", "PRZYJĘCIE", "W TRAKCIE", "2024-05-22", "Dostawca Pieczywa")
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Aktywne Zlecenia", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(zlecenia) { zlecenie ->
                ZlecenieCard(zlecenie)
            }
        }
    }
}

@Composable
fun ZlecenieCard(zlecenie: Zlecenie) {
    val statusColor = when (zlecenie.status) {
        "NOWE" -> Color(0xFF2196F3)
        "W TRAKCIE" -> Color(0xFFFF9800)
        "GOTOWE" -> Color(0xFF4CAF50)
        else -> Color.Gray
    }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(zlecenie.id, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = statusColor.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = zlecenie.status,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            color = statusColor,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Text(zlecenie.klient, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                Text("Data: ${zlecenie.data}", style = MaterialTheme.typography.labelSmall)
            }
            
            IconButton(onClick = { /* Szczegóły */ }) {
                Icon(
                    imageVector = if (zlecenie.typ == "WYDANIE") Icons.Default.Outbox else Icons.Default.MoveToInbox,
                    contentDescription = null,
                    tint = if (zlecenie.typ == "WYDANIE") Color(0xFFE91E63) else Color(0xFF009688)
                )
            }
        }
    }
}

// --- ZAKŁADKA STAN MAGAZYNOWY ---
@Composable
fun StanMagazynowyTab() {
    val stany = remember {
        listOf(
            MagazynierItem("Mleko 3.2% 1L", "A-12-04", 45, "szt."),
            MagazynierItem("Chleb Żytni", "B-02-01", 5, "szt."),
            MagazynierItem("Masło Ekstra 200g", "C-05-10", 120, "szt."),
            MagazynierItem("Mąka Pszenna 1kg", "A-01-05", 50, "kg.")
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Lokalizacje Towarów", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(stany) { item ->
                ListItem(
                    headlineContent = { Text(item.nazwa, fontWeight = FontWeight.SemiBold) },
                    supportingContent = { Text("Lokalizacja: ${item.lokalizacja}") },
                    trailingContent = { 
                        Text(
                            "${item.ilosc} ${item.jednostka}", 
                            fontWeight = FontWeight.Bold,
                            color = if (item.ilosc < 10) Color.Red else Color.Unspecified
                        ) 
                    },
                    leadingContent = {
                        Icon(Icons.Default.Inventory2, contentDescription = null)
                    },
                    modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
                )
            }
        }
    }
}

// --- ZAKŁADKA PROFIL ---
@Composable
fun ProfilMagazynieraTab() {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(120.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(64.dp))
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text("Jan Kowalski", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Text("Magazynier (Zmiana A)", color = Color.Gray)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileInfoRow(Icons.Default.Badge, "ID Pracownika", "MAG-007")
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                ProfileInfoRow(Icons.Default.Email, "Email", "j.kowalski@magazyn.pl")
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                ProfileInfoRow(Icons.Default.Event, "Ostatnie logowanie", "Dziś, 07:45")
            }
        }
    }
}

@Composable
fun ProfileInfoRow(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.Gray)
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
    }
}
