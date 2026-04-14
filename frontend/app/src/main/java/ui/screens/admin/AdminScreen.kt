package ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magazyn.ui.theme.DeepBurgundy
import com.example.magazyn.ui.theme.SoftPinkBG

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(onLogout: () -> Unit) {
    val selectedItem = remember { mutableIntStateOf(0) }
    val items = listOf(
        AdminBottomNavItem.Uzytkownicy,
        AdminBottomNavItem.Finanse,
        AdminBottomNavItem.Raporty,
        AdminBottomNavItem.Ustawienia,
        AdminBottomNavItem.Wyloguj
    )

    Scaffold(
        containerColor = SoftPinkBG,
        topBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondaryContainer,
                tonalElevation = 6.dp,
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(DeepBurgundy.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = null,
                            tint = DeepBurgundy,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Panel Administratora",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = DeepBurgundy
                        )
                        Text(
                            text = "Zarządzanie systemem magazynu",
                            style = MaterialTheme.typography.bodySmall,
                            color = DeepBurgundy.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title, style = MaterialTheme.typography.labelSmall) },
                        selected = selectedItem.intValue == index,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepBurgundy,
                            selectedTextColor = DeepBurgundy,
                            indicatorColor = DeepBurgundy.copy(alpha = 0.1f),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        onClick = {
                            if (item is AdminBottomNavItem.Wyloguj) {
                                onLogout()
                            } else {
                                selectedItem.intValue = index
                            }
                        }
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedItem.intValue) {
                0 -> UsersTab()
                1 -> FinanceTab()
                2 -> ReportsTab()
                3 -> SettingsTab()
            }
        }
    }
}

@Composable
fun AdminSectionHeader(title: String, icon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = DeepBurgundy, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = DeepBurgundy
        )
    }
}

@Composable
fun AdminCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = DeepBurgundy
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun UsersTab() {
    val users = listOf(
        "Sebastian Mikoś" to "Lider / Admin",
        "Wojciech Koba" to "DevOps",
        "Amadeusz Nowak" to "Backend",
        "Michał Kalisiak" to "Backend",
        "Kacper Kłósek" to "Frontend"
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        item { AdminSectionHeader("Użytkownicy", Icons.Default.People) }
        items(users) { (name, role) ->
            AdminCard(title = name) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = role, color = Color.Gray)
                    Row {
                        IconButton(onClick = {}) { Icon(Icons.Default.Edit, contentDescription = "Edytuj", tint = DeepBurgundy) }
                        IconButton(onClick = {}) { Icon(Icons.Default.Block, contentDescription = "Blokuj", tint = Color.Red) }
                    }
                }
            }
        }
        item {
            Button(
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DeepBurgundy),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Dodaj użytkownika")
            }
        }
    }
}

@Composable
fun FinanceTab() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        AdminSectionHeader("Finanse", Icons.Default.Payments)
        
        AdminCard(title = "Podsumowanie okresu") {
            FinanceRow("Przychody", "12 450.00 PLN", Color(0xFF4CAF50))
            FinanceRow("Wydatki", "8 200.00 PLN", Color(0xFFF44336))
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            FinanceRow("Zysk netto", "4 250.00 PLN", DeepBurgundy)
        }

        AdminCard(title = "Ostatnie transakcje") {
            Text("Brak nowych transakcji do wyświetlenia.", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun FinanceRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun ReportsTab() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        AdminSectionHeader("Raporty", Icons.Default.Assignment)
        
        ReportButton("Raport przychodów", "Zestawienie finansowe z wybranego okresu")
        ReportButton("Raport stanu magazynu", "Aktualne ilości i historia zapasów")
        ReportButton("Raport wydatków", "Analiza kosztów operacyjnych")
    }
}

@Composable
fun ReportButton(title: String, description: String) {
    AdminCard(title = title) {
        Text(text = description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = {},
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = DeepBurgundy.copy(alpha = 0.1f)),
            contentPadding = PaddingValues(8.dp)
        ) {
            Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = DeepBurgundy)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Generuj PDF", color = DeepBurgundy)
        }
    }
}

@Composable
fun SettingsTab() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        AdminSectionHeader("Ustawienia", Icons.Default.Settings)
        
        AdminCard(title = "Progi alarmowe") {
            Text("Minimalna ilość produktów przed powiadomieniem", style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(16.dp))
            Slider(value = 0.2f, onValueChange = {}, colors = SliderDefaults.colors(thumbColor = DeepBurgundy, activeTrackColor = DeepBurgundy))
        }

        AdminCard(title = "Wygląd") {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Tryb ciemny")
                Switch(checked = false, onCheckedChange = {}, colors = SwitchDefaults.colors(checkedThumbColor = DeepBurgundy))
            }
        }
    }
}
