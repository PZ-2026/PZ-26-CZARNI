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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magazyn.api.ApiConnector
import com.example.magazyn.api.dtos.*
import com.example.magazyn.ui.theme.DeepBurgundy
import com.example.magazyn.ui.theme.SoftPinkBG
import kotlinx.coroutines.launch

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
                2 -> DashboardTab()
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
    val users = remember { mutableStateOf<List<UzytkownikAdminDTO>?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val error = remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val result = ApiConnector.pobierzWszystkowUzytkownikow()
                users.value = result
                isLoading.value = false
            } catch (e: Exception) {
                error.value = e.message
                isLoading.value = false
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item { AdminSectionHeader("Użytkownicy", Icons.Default.People) }

        if (isLoading.value) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = DeepBurgundy)
                }
            }
        } else if (error.value != null) {
            item {
                AdminCard(title = "Błąd") {
                    Text(error.value ?: "Nieznany błąd", color = Color.Red)
                }
            }
        } else if (users.value != null) {
            items(users.value!!) { user ->
                AdminCard(title = "${user.imie} ${user.nazwisko}") {
                    Column {
                        Text(text = "Email: ${user.email}", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                        Text(text = "Rola: ${getRoleName(user.rola)}", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                        if (user.zablokowany) {
                            Text(text = "⛔ Zablokowany", color = Color.Red, style = MaterialTheme.typography.bodySmall)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            IconButton(onClick = {}) { Icon(Icons.Default.Edit, contentDescription = "Edytuj", tint = DeepBurgundy) }
                            IconButton(onClick = {
                                scope.launch {
                                    if (user.zablokowany) {
                                        ApiConnector.odblokowakUzytkownika(user.id ?: 0)
                                    } else {
                                        ApiConnector.zablokowakUzytkownika(user.id ?: 0)
                                    }
                                    val result = ApiConnector.pobierzWszystkowUzytkownikow()
                                    users.value = result
                                }
                            }) { 
                                Icon(
                                    if (user.zablokowany) Icons.Default.Lock else Icons.Default.Block, 
                                    contentDescription = if (user.zablokowany) "Odblokuj" else "Blokuj", 
                                    tint = Color.Red
                                ) 
                            }
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
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
    val report = remember { mutableStateOf<RaportFinansowyDTO?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val result = ApiConnector.pobierzRaportFinansowy(null, null)
                report.value = result
                isLoading.value = false
            } catch (e: Exception) {
                isLoading.value = false
            }
        }
    }

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        item { AdminSectionHeader("Finanse", Icons.Default.Payments) }

        if (isLoading.value) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = DeepBurgundy)
                }
            }
        } else if (report.value != null) {
            item {
                AdminCard(title = "Podsumowanie okresu") {
                    FinanceRow("Przychody", "${report.value!!.sumaPrzychodow} PLN", Color(0xFF4CAF50))
                    FinanceRow("Wydatki", "${report.value!!.sumaWydatkow} PLN", Color(0xFFF44336))
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    FinanceRow("Zysk netto", "${report.value!!.sumaZysku} PLN", DeepBurgundy)
                }
            }
        }

        item {
            AdminCard(title = "Ostatnie transakcje") {
                Text("Pobieranie danych...", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun FinanceRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Text(text = value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = color)
    }
}

@Composable
fun DashboardTab() {
    val dashboard = remember { mutableStateOf<PanelAdminaDTO?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val result = ApiConnector.pobierzDanePanelu()
                dashboard.value = result
                isLoading.value = false
            } catch (e: Exception) {
                isLoading.value = false
            }
        }
    }

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        item { AdminSectionHeader("Panel Administratora", Icons.Default.Dashboard) }

        if (isLoading.value) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = DeepBurgundy)
                }
            }
        } else if (dashboard.value != null) {
            val d = dashboard.value!!
            items(listOf(
                Triple("👥 Użytkownicy", d.liczbaUzytkownikow.toString(), DeepBurgundy),
                Triple("📦 Produkty", d.liczbaProduktu.toString(), Color(0xFF2196F3)),
                Triple("🏢 Magazyny", d.liczbaMagazynow.toString(), Color(0xFF4CAF50)),
                Triple("🚚 Dostawcy", d.liczbaDostawow.toString(), Color(0xFFFFC107)),
                Triple("💰 Przychody (m-c)", "${d.przychodyMiesiac} PLN", Color(0xFF4CAF50)),
                Triple("💸 Wydatki (m-c)", "${d.wydatkiMiesiac} PLN", Color(0xFFF44336)),
                Triple("💵 Zysk (m-c)", "${d.zyskMiesiac} PLN", DeepBurgundy),
                Triple("⏳ Zamówienia w toku", d.liczbaZamowienWProgress.toString(), Color(0xFFFFC107)),
                Triple("✅ Zamówienia do realizacji", d.liczbaZamowienDoRealizacji.toString(), Color(0xFF2196F3)),
                Triple("⚠️ Produkty poniżej progu", d.liczbaProductowPonizejProgu.toString(), Color(0xFFF44336))
            )) { (label, value, color) ->
                AdminCard(title = label) {
                    Text(text = value, style = MaterialTheme.typography.headlineSmall, color = color, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun SettingsTab() {
    val configs = remember { mutableStateOf<List<KonfiguracijaDTO>?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val result = ApiConnector.pobierzWszystkoKonfiguracje()
                configs.value = result
                isLoading.value = false
            } catch (e: Exception) {
                isLoading.value = false
            }
        }
    }

    LazyColumn(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        item { AdminSectionHeader("Ustawienia", Icons.Default.Settings) }

        if (isLoading.value) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = DeepBurgundy)
                }
            }
        } else if (configs.value != null) {
            items(configs.value!!) { config ->
                AdminCard(title = config.nazwaParametru) {
                    Column {
                        Text(text = config.opis, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        Text(text = "Wartość: ${config.wartoscParametru}", style = MaterialTheme.typography.bodyMedium)
                        Text(text = "Typ: ${config.typParametru}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        if (config.aktywna) {
                            Text(text = "✓ Aktywna", color = Color(0xFF4CAF50), style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        } else {
            item {
                Text("Brak konfiguracji", modifier = Modifier.padding(16.dp))
            }
        }
    }
}

fun getRoleName(roleId: Int): String {
    return when (roleId) {
        1 -> "Administrator"
        2 -> "Magazynier"
        3 -> "Zaopatrzeniowiec"
        4 -> "Klient"
        else -> "Nieznana rola"
    }
}

