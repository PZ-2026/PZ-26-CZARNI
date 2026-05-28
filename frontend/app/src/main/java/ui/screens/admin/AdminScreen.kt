package ui.screens.admin

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magazyn.api.ApiConnector
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import com.example.magazyn.api.dtos.*
import com.example.magazyn.ui.theme.DeepBurgundy
import java.math.BigDecimal
import com.example.magazyn.ui.theme.SoftPinkBG
import com.example.magazyn.utils.AppSettings
import com.example.magazyn.utils.RoleConstants
import com.example.magazyn.utils.getRolaName
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard(
    onLogout: () -> Unit,
    darkTheme: Boolean,
    onDarkModeChange: (Boolean) -> Unit
) {
    val selectedItem = remember { mutableIntStateOf(0) }
    val items = listOf(
        AdminBottomNavItem.Uzytkownicy,
        AdminBottomNavItem.Dostawcy,
        AdminBottomNavItem.Magazyn,
        AdminBottomNavItem.Finanse,
        AdminBottomNavItem.Raporty
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
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Panel Admina",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = DeepBurgundy
                        )
                        Text(
                            text = "Zarządzanie systemem",
                            style = MaterialTheme.typography.bodySmall,
                            color = DeepBurgundy.copy(alpha = 0.6f)
                        )
                    }
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.Logout, contentDescription = "Wyloguj", tint = DeepBurgundy)
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
                        alwaysShowLabel = false, // Etykieta pokaże się tylko dla wybranego elementu
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DeepBurgundy,
                            selectedTextColor = DeepBurgundy,
                            indicatorColor = DeepBurgundy.copy(alpha = 0.1f),
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),
                        onClick = {
                            selectedItem.intValue = index
                        }
                    )
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (selectedItem.intValue) {
                0 -> UsersTab()
                1 -> DostawcyTab()
                2 -> MagazynTab()
                3 -> FinanceTab()
                4 -> DashboardTab()
            }
        }
    }
}

@Composable
fun UsersTab() {
    val users = remember { mutableStateOf<List<UzytkownikAdminDTO>?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val error = remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }

    var showUserDialog by remember { mutableStateOf(false) }
    var userToEdit by remember { mutableStateOf<UzytkownikAdminDTO?>(null) }
    var showOrdersDialog by remember { mutableStateOf(false) }
    var ordersForUser by remember { mutableStateOf<UzytkownikAdminDTO?>(null) }

    fun refreshUsers() {
        scope.launch {
            isLoading.value = true
            error.value = null
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

    LaunchedEffect(Unit) {
        refreshUsers()
    }

    if (showUserDialog) {
        UserDialog(
            user = userToEdit,
            onDismiss = {
                showUserDialog = false
                userToEdit = null
            },
            onConfirm = {
                showUserDialog = false
                userToEdit = null
                refreshUsers()
            }
        )
    }

    if (showOrdersDialog && ordersForUser != null) {
        OrdersDialog(
            user = ordersForUser!!,
            onDismiss = { showOrdersDialog = false; ordersForUser = null }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AdminSectionHeader("Użytkownicy", Icons.Default.People)
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = { refreshUsers() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Odśwież", tint = DeepBurgundy)
                }
            }
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Szukaj użytkownika") },
                placeholder = { Text("imię, nazwisko, email lub telefon") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
            )
        }

        val filteredUsers = users.value?.filter { user ->
            val query = searchQuery.trim().lowercase()
            query.isBlank() || listOf(
                user.imie,
                user.nazwisko,
                user.email ?: "",
                user.telefon ?: ""
            ).any { it.lowercase().contains(query) }
        } ?: emptyList()

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
                    Button(onClick = { refreshUsers() }) { Text("Ponów") }
                }
            }
        } else if (users.value != null) {
            if (filteredUsers.isEmpty()) {
                item {
                    AdminCard(title = "Brak wyników") {
                        Text("Nie znaleziono użytkowników pasujących do zapytania.", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            } else {
                items(filteredUsers) { user ->
                    AdminCard(title = "${user.imie} ${user.nazwisko}") {
                        Column {
                            Text(text = "Email: ${user.email}", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                            Text(text = "Telefon: ${user.telefon ?: "Brak"}", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                            Text(text = "Rola: ${getRolaName(user.rola)}", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                            if (user.zablokowany) {
                                Text(text = "⛔ Zablokowany", color = Color.Red, style = MaterialTheme.typography.bodySmall)
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                IconButton(onClick = {
                                    ordersForUser = user
                                    showOrdersDialog = true
                                }) { Icon(Icons.Default.ListAlt, contentDescription = "Zamówienia", tint = DeepBurgundy) }

                                IconButton(onClick = {
                                    userToEdit = user
                                    showUserDialog = true
                                }) { Icon(Icons.Default.Edit, contentDescription = "Edytuj", tint = DeepBurgundy) }

                                IconButton(onClick = {
                                    scope.launch {
                                        val success = if (user.zablokowany) {
                                            ApiConnector.odblokowakUzytkownika(user.id ?: 0)
                                        } else {
                                            ApiConnector.zablokowakUzytkownika(user.id ?: 0)
                                        }
                                        if (success) refreshUsers()
                                        else Toast.makeText(context, "Błąd zmiany statusu", Toast.LENGTH_SHORT).show()
                                    }
                                }) {
                                    Icon(
                                        if (user.zablokowany) Icons.Default.LockOpen else Icons.Default.Block,
                                        contentDescription = if (user.zablokowany) "Odblokuj" else "Zablokuj",
                                        tint = if (user.zablokowany) Color.Green else Color.Red
                                    )
                                }

                                IconButton(onClick = {
                                    scope.launch {
                                        if (ApiConnector.usunUzytkownika(user.id ?: 0)) {
                                            refreshUsers()
                                            Toast.makeText(context, "Użytkownik usunięty", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, "Błąd usuwania", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }) { Icon(Icons.Default.Delete, contentDescription = "Usuń", tint = Color.Red) }
                            }
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    userToEdit = null
                    showUserDialog = true
                },
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
fun UserDialog(
    user: UzytkownikAdminDTO?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current
    var imie by remember { mutableStateOf(user?.imie ?: "") }
    var nazwisko by remember { mutableStateOf(user?.nazwisko ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var telefon by remember { mutableStateOf(user?.telefon ?: "") }
    var firma by remember { mutableStateOf(user?.firma ?: "") }
    var nip by remember { mutableStateOf(user?.nip ?: "") }
    var haslo by remember { mutableStateOf("") }
    var zablokowany by remember { mutableStateOf(user?.zablokowany ?: false) }
    var rola by remember { mutableIntStateOf(user?.rola ?: RoleConstants.KLIENT) }

    var telefonError by remember { mutableStateOf<String?>(null) }
    var nipError by remember { mutableStateOf<String?>(null) }

    val scope = rememberCoroutineScope()
    val isEditing = user != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "Edytuj użytkownika" else "Dodaj użytkownika") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(value = imie, onValueChange = { imie = it }, label = { Text("Imię") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = nazwisko, onValueChange = { nazwisko = it }, label = { Text("Nazwisko") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(
                    value = telefon,
                    onValueChange = {
                        telefon = it
                        if (it.isNotBlank()) {
                            val digits = it.filter { c -> c.isDigit() }
                            telefonError = if (digits.length != 9) "Telefon powinien mieć dokładnie 9 cyfr" else null
                        } else telefonError = null
                    },
                    label = { Text("Telefon") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = telefonError != null
                )
                if (telefonError != null) Text(telefonError ?: "", color = Color.Red, style = MaterialTheme.typography.bodySmall)
                OutlinedTextField(value = firma, onValueChange = { firma = it }, label = { Text("Firma") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(
                    value = nip,
                    onValueChange = {
                        nip = it
                        if (it.isNotBlank()) {
                            val digits = it.filter { c -> c.isDigit() }
                            nipError = if (digits.length != 10) "NIP powinien mieć dokładnie 10 cyfr" else null
                        } else nipError = null
                    },
                    label = { Text("NIP") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nipError != null
                )
                if (nipError != null) Text(nipError ?: "", color = Color.Red, style = MaterialTheme.typography.bodySmall)

                OutlinedTextField(
                    value = haslo,
                    onValueChange = { haslo = it },
                    label = { Text(if (isEditing) "Nowe hasło (puste by nie zmieniać)" else "Hasło (wymagane)") },
                    modifier = Modifier.fillMaxWidth(),
                    visualTransformation = PasswordVisualTransformation()
                )

                if (isEditing) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 8.dp)) {
                        Checkbox(checked = zablokowany, onCheckedChange = { zablokowany = it })
                        Text("Konto zablokowane")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("Rola:", fontWeight = FontWeight.Bold)

                Column {
                    RolaOption("Administrator", rola == RoleConstants.ADMINISTRATOR) { rola = RoleConstants.ADMINISTRATOR }
                    RolaOption("Magazynier", rola == RoleConstants.MAGAZYNIER) { rola = RoleConstants.MAGAZYNIER }
                    RolaOption("Zaopatrzeniowiec", rola == RoleConstants.ZAOPATRZENIOWIEC) { rola = RoleConstants.ZAOPATRZENIOWIEC }
                    RolaOption("Klient", rola == RoleConstants.KLIENT) { rola = RoleConstants.KLIENT }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (!isEditing && haslo.isBlank()) {
                    Toast.makeText(context, "Hasło jest wymagane!", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                if (telefonError != null || nipError != null) {
                    Toast.makeText(context, "Proszę poprawić błędy w formularzu", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                scope.launch {
                    val dto = UzytkownikAdminDTO(
                        id = user?.id,
                        imie = imie.trim(),
                        nazwisko = nazwisko.trim(),
                        email = email.trim(),
                        telefon = telefon.trim().ifBlank { null },
                        firma = firma.trim().ifBlank { null },
                        nip = nip.trim().ifBlank { null },
                        rola = rola,
                        zablokowany = zablokowany,
                        haslo = if (haslo.isNotBlank()) haslo.trim() else null
                    )

                    val result = if (isEditing) {
                        val updates = mutableMapOf<String, Any?>(
                            "imie" to imie.trim(),
                            "nazwisko" to nazwisko.trim(),
                            "email" to email.trim(),
                            "telefon" to telefon.trim().ifBlank { null },
                            "firma" to firma.trim().ifBlank { null },
                            "nip" to nip.trim().ifBlank { null },
                            "rola" to rola,
                            "zablokowany" to zablokowany
                        )
                        if (haslo.isNotBlank()) {
                            updates["haslo"] = haslo.trim()
                        }
                        ApiConnector.edytujUzytkownika(user!!.id ?: 0, updates)
                    } else {
                        ApiConnector.utworzUzytkownika(dto)
                    }

                    if (result.first != null) {
                        Toast.makeText(context, "Zapisano pomyślnie", Toast.LENGTH_SHORT).show()
                        onConfirm()
                    } else {
                        Toast.makeText(context, "Błąd serwera: ${result.second}", Toast.LENGTH_LONG).show()
                    }
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = DeepBurgundy)) {
                Text("Zapisz")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Anuluj") }
        }
    )
}

@Composable
fun DostawcyTab() {
    val dostawcy = remember { mutableStateOf<List<DostawcaDTO>?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var dostawcaToEdit by remember { mutableStateOf<DostawcaDTO?>(null) }

    LaunchedEffect(Unit) {
        scope.launch {
            dostawcy.value = ApiConnector.pobierzWszystkichDostawcow()
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(16.dp)) {
        AdminSectionHeader("Zarządzanie Dostawcami", Icons.Default.LocalShipping)

        dostawcy.value?.let { lista ->
            lista.forEach { dostawca ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), shape = RoundedCornerShape(12.dp)) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(dostawca.nazwa, fontWeight = FontWeight.Bold)
                            Text(dostawca.adres, style = MaterialTheme.typography.bodySmall)
                            Text(dostawca.telefon, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        IconButton(onClick = {
                            dostawcaToEdit = dostawca
                            showDialog = true
                        }) { Icon(Icons.Default.Edit, contentDescription = "Edytuj", tint = DeepBurgundy) }
                        IconButton(onClick = {
                            scope.launch {
                                if (ApiConnector.usunDostawce(dostawca.id ?: 0)) {
                                    dostawcy.value = ApiConnector.pobierzWszystkichDostawcow()
                                    Toast.makeText(context, "Dostawca usunięty", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }) { Icon(Icons.Default.Delete, contentDescription = "Usuń", tint = Color.Red) }
                    }
                }
            }
        }

        Button(onClick = {
            dostawcaToEdit = null
            showDialog = true
        }, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DeepBurgundy)) {
            Text("Dodaj dostawcę")
        }
    }

    if (showDialog) {
        DostawcaDialog(
            dostawca = dostawcaToEdit,
            onDismiss = { showDialog = false },
            onConfirm = {
                scope.launch {
                    dostawcy.value = ApiConnector.pobierzWszystkichDostawcow()
                    showDialog = false
                }
            }
        )
    }
}

@Composable
fun MagazynTab() {
    val stan = remember { mutableStateOf<List<StanMagazynuDTO>?>(null) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var showAddDialog by remember { mutableStateOf(false) }
    var stanToEdit by remember { mutableStateOf<StanMagazynuDTO?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    fun refreshStan() {
        scope.launch {
            stan.value = ApiConnector.pobierzCalyStanMagazynu()
        }
    }

    LaunchedEffect(Unit) {
        refreshStan()
    }

    val filteredStan = stan.value?.filter { produkt ->
        val query = searchQuery.trim().lowercase()
        query.isBlank() || listOf(
            produkt.nazwa ?: "",
            produkt.id?.toString() ?: "",
            produkt.ilosc.toString()
        ).any { it.lowercase().contains(query) }
    } ?: emptyList()

    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(rememberScrollState())
        .padding(16.dp)) {
        AdminSectionHeader("Stan Magazynu", Icons.Default.Warehouse)

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("Szukaj w magazynie") },
            placeholder = { Text("nazwa produktu lub ID") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        )

        Button(
            onClick = { showAddDialog = true },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DeepBurgundy)
        ) {
            Icon(Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Dodaj nowy produkt")
        }

        if (filteredStan.isEmpty()) {
            Text(
                text = if (stan.value == null) "Ładowanie..." else "Brak produktów w magazynie",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        } else {
            filteredStan.forEach { produkt ->
                Card(modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), shape = RoundedCornerShape(12.dp)) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(produkt.nazwa ?: "Produkt ID: ${produkt.id ?: "Nieznany"}", fontWeight = FontWeight.Bold)
                            Text("ID produktu: ${produkt.id ?: "Brak"}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            Text("Ilość: ${produkt.ilosc}", style = MaterialTheme.typography.bodySmall)
                            if (produkt.cena != null) {
                                Text("Cena: ${produkt.cena} PLN", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                            }
                        }
                        IconButton(onClick = {
                            stanToEdit = produkt
                            showDialog = true
                        }) { Icon(Icons.Default.Edit, contentDescription = "Edytuj", tint = DeepBurgundy) }
                        IconButton(onClick = {
                            scope.launch {
                                if (ApiConnector.usunStanMagazynu(produkt.id ?: 0)) {
                                    refreshStan()
                                    Toast.makeText(context, "Produkt usunięty", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }) { Icon(Icons.Default.Delete, contentDescription = "Usuń", tint = Color.Red) }
                    }
                }
            }
        }
    }

    if (showDialog && stanToEdit != null) {
        MagazynDialog(
            stan = stanToEdit,
            onDismiss = { showDialog = false },
            onConfirm = {
                scope.launch {
                    refreshStan()
                    showDialog = false
                }
            }
        )
    }

    if (showAddDialog) {
        MagazynAddDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = {
                scope.launch {
                    refreshStan()
                    showAddDialog = false
                }
            }
        )
    }
}

@Composable
fun DostawcaDialog(
    dostawca: DostawcaDTO?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current
    var nazwa by remember { mutableStateOf(dostawca?.nazwa ?: "") }
    var adres by remember { mutableStateOf(dostawca?.adres ?: "") }
    var telefon by remember { mutableStateOf(dostawca?.telefon ?: "") }
    val scope = rememberCoroutineScope()
    val isEditing = dostawca != null
    var telefonError by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "Edytuj dostawcę" else "Dodaj dostawcę") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(value = nazwa, onValueChange = { nazwa = it }, label = { Text("Nazwa") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = adres, onValueChange = { adres = it }, label = { Text("Adres") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = telefon, onValueChange = { telefon = it }, label = { Text("Telefon") }, modifier = Modifier.fillMaxWidth(), isError = telefonError != null)
                if (telefonError != null) Text(telefonError ?: "", color = Color.Red, style = MaterialTheme.typography.bodySmall)
            }
        },
        confirmButton = {
            Button(onClick = {
                if (telefonError != null) {
                    Toast.makeText(context, "Proszę poprawić błędy w formularzu", Toast.LENGTH_SHORT).show()
                    return@Button
                }
                scope.launch {
                    val dto = DostawcaDTO(id = dostawca?.id ?: 0, nazwa = nazwa, adres = adres, telefon = telefon)
                    val result = if (isEditing) {
                        ApiConnector.edytujDostawce(dostawca?.id ?: 0, dto)
                    } else {
                        ApiConnector.utworzDostawce(dto)
                    }
                    if (result.first != null) {
                        Toast.makeText(context, "Zapisano", Toast.LENGTH_SHORT).show()
                        onConfirm()
                    } else {
                        Toast.makeText(context, "Błąd: ${result.second}", Toast.LENGTH_LONG).show()
                    }
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = DeepBurgundy)) {
                Text("Zapisz")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Anuluj") }
        }
    )
}

@Composable
fun MagazynDialog(
    stan: StanMagazynuDTO?,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current
    var ilosc by remember { mutableStateOf(stan?.ilosc?.toString() ?: "") }
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edytuj ilość - ID: ${stan?.id ?: "Nieznany"}") },
        text = {
            Column {
                OutlinedTextField(value = ilosc, onValueChange = { ilosc = it }, label = { Text("Ilość") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            Button(onClick = {
                scope.launch {
                    val parsedIlosc = ilosc.toIntOrNull() ?: 0
                    val dto = stan?.copy(ilosc = parsedIlosc)

                    if (dto != null) {
                        val result = ApiConnector.edytujStanMagazynu(dto.id ?: 0, dto)
                        if (result.first != null) {
                            Toast.makeText(context, "Zapisano", Toast.LENGTH_SHORT).show()
                            onConfirm()
                        } else {
                            Toast.makeText(context, "Błąd: ${result.second}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = DeepBurgundy)) {
                Text("Zapisz")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Anuluj") }
        }
    )
}

@Composable
fun MagazynAddDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var nazwa by remember { mutableStateOf("") }
    var ilosc by remember { mutableStateOf("") }
    var cena by remember { mutableStateOf("") }
    var kodKreskowy by remember { mutableStateOf("") }
    var jednostka by remember { mutableStateOf("szt.") }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Dodaj nowy produkt do magazynu") },
        text = {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                OutlinedTextField(value = nazwa, onValueChange = { nazwa = it }, label = { Text("Nazwa produktu") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = kodKreskowy, onValueChange = { kodKreskowy = it }, label = { Text("Kod kreskowy") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = ilosc, onValueChange = { ilosc = it }, label = { Text("Ilość") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = cena, onValueChange = { cena = it }, label = { Text("Cena") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = jednostka, onValueChange = { jednostka = it }, label = { Text("Jednostka") }, modifier = Modifier.fillMaxWidth())
                if (error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = error ?: "", color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val parsedIlosc = ilosc.toIntOrNull()
                val parsedCena = cena.toBigDecimalOrNull()
                if (nazwa.isBlank() || parsedIlosc == null || parsedCena == null || kodKreskowy.isBlank()) {
                    error = "Wypełnij wszystkie pola poprawnymi wartościami"
                    return@Button
                }
                scope.launch {
                    val dto = StanMagazynuDTO(
                        id = 0,
                        ilosc = parsedIlosc,
                        nazwa = nazwa.trim(),
                        cena = parsedCena,
                        kodKreskowy = kodKreskowy.trim(),
                        jednostka = jednostka.trim()
                    )
                    val result = ApiConnector.utworzStanMagazynu(dto)
                    if (result.first != null) {
                        Toast.makeText(context, "Produkt dodany do magazynu", Toast.LENGTH_SHORT).show()
                        onConfirm()
                    } else {
                        error = result.second ?: "Błąd serwera"
                    }
                }
            }, colors = ButtonDefaults.buttonColors(containerColor = DeepBurgundy)) {
                Text("Dodaj")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Anuluj") }
        }
    )
}

@Composable
fun RolaOption(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = onClick)
        Text(text = label, modifier = Modifier.padding(start = 8.dp))
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
fun FinanceTab() {
    val report = remember { mutableStateOf<RaportFinansowyDTO?>(null) }
    val history = remember { mutableStateOf<List<DaneFinansoweDTO>?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val przychodyMiesiac = remember { mutableStateOf<Double?>(null) }
    val wydatkiMiesiac = remember { mutableStateOf<Double?>(null) }
    val zyskMiesiac = remember { mutableStateOf<Double?>(null) }
    val startDate = remember { mutableStateOf(LocalDate.now().withDayOfMonth(1).format(DateTimeFormatter.ISO_LOCAL_DATE)) }
    val endDate = remember { mutableStateOf(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)) }
    val dateError = remember { mutableStateOf<String?>(null) }
    val historySearch = remember { mutableStateOf("") }
    val context = LocalContext.current

    fun buildQueryRange(): Pair<String, String>? {
        return try {
            val start = LocalDate.parse(startDate.value, DateTimeFormatter.ISO_LOCAL_DATE)
            val end = LocalDate.parse(endDate.value, DateTimeFormatter.ISO_LOCAL_DATE)
            if (start.isAfter(end)) {
                dateError.value = "Data początkowa nie może być późniejsza niż końcowa"
                null
            } else {
                dateError.value = null
                Pair("${start}T00:00:00", "${end}T23:59:59")
            }
        } catch (e: DateTimeParseException) {
            dateError.value = "Użyj formatu RRRR-MM-DD"
            null
        }
    }

    fun refreshFinancialData() {
        scope.launch {
            isLoading.value = true
            val range = buildQueryRange()
            if (range == null) {
                isLoading.value = false
                return@launch
            }
            report.value = ApiConnector.pobierzRaportFinansowy(range.first, range.second)
            history.value = ApiConnector.pobierzHistorieFinansowa(range.first, range.second)
            przychodyMiesiac.value = ApiConnector.pobierzPrzychodyMiesiac()
            wydatkiMiesiac.value = ApiConnector.pobierzWydatkiMiesiac()
            zyskMiesiac.value = ApiConnector.pobierzZyskMiesiac()
            isLoading.value = false
        }
    }

    LaunchedEffect(Unit) {
        refreshFinancialData()
    }

    val filteredHistory = history.value?.filter {
        val query = historySearch.value.trim().lowercase()
        query.isBlank() || it.typ.lowercase().contains(query) ||
                it.data.lowercase().contains(query) ||
                it.idZamowienia?.toString()?.contains(query) == true
    } ?: emptyList()

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item { AdminSectionHeader("Finanse", Icons.Default.Payments) }

        item {
            AdminCard(title = "Filtruj okres") {
                Column {
                    OutlinedTextField(
                        value = startDate.value,
                        onValueChange = { startDate.value = it },
                        label = { Text("Data początkowa") },
                        placeholder = { Text("RRRR-MM-DD") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = endDate.value,
                        onValueChange = { endDate.value = it },
                        label = { Text("Data końcowa") },
                        placeholder = { Text("RRRR-MM-DD") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    if (dateError.value != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = dateError.value ?: "", color = Color.Red, style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { refreshFinancialData() }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = DeepBurgundy)) {
                            Text("Pobierz raport")
                        }
                        Button(onClick = {
                            val currentReport = report.value
                            if (currentReport != null) {
                                val okres = "${startDate.value} do ${endDate.value}"
                                val path = zapiszRaportFinansowyDoPdf(context, currentReport, history.value ?: emptyList(), okres)
                                Toast.makeText(context, if (path != null) "PDF zapisany: $path" else "Nie udało się wygenerować PDF", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(context, "Brak danych do wygenerowania PDF", Toast.LENGTH_SHORT).show()
                            }
                        }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))) {
                            Text("Generuj PDF")
                        }
                    }
                }
            }
        }

        if (isLoading.value) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = DeepBurgundy)
                }
            }
        } else {
            item {
                AdminCard(title = "Podsumowanie okresu") {
                    FinanceRow("Przychody", "${report.value?.sumaPrzychodow ?: 0} PLN", Color(0xFF4CAF50))
                    FinanceRow("Wydatki", "${report.value?.sumaWydatkow ?: 0} PLN", Color(0xFFF44336))
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    FinanceRow("Zysk netto", "${report.value?.sumaZysku ?: 0} PLN", DeepBurgundy)
                }
            }

            item {
                AdminCard(title = "Bieżący miesiąc") {
                    FinanceRow("Przychody (mies.)", "${przychodyMiesiac.value ?: 0.0} PLN", Color(0xFF4CAF50))
                    FinanceRow("Wydatki (mies.)", "${wydatkiMiesiac.value ?: 0.0} PLN", Color(0xFFF44336))
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    FinanceRow("Zysk (mies.)", "${zyskMiesiac.value ?: 0.0} PLN", DeepBurgundy)
                }
            }

            item {
                AdminCard(title = "Historia finansowa") {
                    OutlinedTextField(
                        value = historySearch.value,
                        onValueChange = { historySearch.value = it },
                        label = { Text("Szukaj w historii") },
                        placeholder = { Text("typ, data lub ID zamówienia") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    if (filteredHistory.isEmpty()) {
                        Text("Brak pozycji dla wybranego okresu lub zapytania.", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                    } else {
                        filteredHistory.take(10).forEach { item ->
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                Text("${item.data} - ${item.typ}", fontWeight = FontWeight.Bold)
                                Text("Przychody: ${item.przychody} PLN, Wydatki: ${item.wydatki} PLN, Zysk: ${item.zysk} PLN", style = MaterialTheme.typography.bodySmall)
                                item.idZamowienia?.let { Text("Zamówienie ID: $it", style = MaterialTheme.typography.bodySmall, color = Color.Gray) }
                                HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))
                            }
                        }
                    }
                }
            }
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

fun zapiszRaportFinansowyDoPdf(context: Context, raport: RaportFinansowyDTO, historia: List<DaneFinansoweDTO>, okres: String): String? {
    return try {
        val fileName = "raport_finansowy_${LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)}.pdf"
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)
        val document = PdfDocument()
        var pageNumber = 1
        fun createPage(): PdfDocument.Page {
            val pageInfo = PdfDocument.PageInfo.Builder(595, 842, pageNumber).create()
            pageNumber += 1
            return document.startPage(pageInfo)
        }

        var page = createPage()
        var canvas = page.canvas
        val paint = Paint().apply {
            textSize = 12f
            color = android.graphics.Color.BLACK
        }

        var y = 40f
        canvas.drawText("Raport finansowy", 40f, y, paint)
        y += 24f
        canvas.drawText("Okres: $okres", 40f, y, paint)
        y += 24f
        canvas.drawText("Przychody: ${raport.sumaPrzychodow} PLN", 40f, y, paint)
        y += 20f
        canvas.drawText("Wydatki: ${raport.sumaWydatkow} PLN", 40f, y, paint)
        y += 20f
        canvas.drawText("Zysk netto: ${raport.sumaZysku} PLN", 40f, y, paint)
        y += 32f
        canvas.drawText("Historia finansowa:", 40f, y, paint)
        y += 20f

        historia.forEach { item ->
            if (y > 780f) {
                document.finishPage(page)
                page = createPage()
                canvas = page.canvas
                y = 40f
            }
            canvas.drawText("${item.data} | ${item.typ} | Przychody: ${item.przychody} PLN | Wydatki: ${item.wydatki} PLN | Zysk: ${item.zysk} PLN", 40f, y, paint)
            y += 18f
        }

        document.finishPage(page)
        FileOutputStream(file).use { document.writeTo(it) }
        document.close()
        file.absolutePath
    } catch (e: Exception) {
        Log.e("AdminScreen", "Błąd generowania PDF", e)
        null
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

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item { AdminSectionHeader("Raporty", Icons.Default.Analytics) }

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
                Triple("⏳ Zamówienia w toku", d.liczbaZamowienWProgress.toString(), Color(0xFFFFC107)),
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
fun OrdersDialog(
    user: UzytkownikAdminDTO,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val orders = remember { mutableStateOf<List<HistoriaZamowieniaDTO>?>(null) }
    val isLoading = remember { mutableStateOf(true) }
    var expandedOrderId by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(user.id) {
        scope.launch {
            isLoading.value = true
            orders.value = ApiConnector.pobierzHistorieZamowien(user.id ?: 0)
            isLoading.value = false
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Zamówienia: ${user.imie} ${user.nazwisko}") },
        text = {
            Column(modifier = Modifier.fillMaxWidth().heightIn(max = 500.dp)) {
                if (isLoading.value) {
                    Box(modifier = Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = DeepBurgundy)
                    }
                } else {
                    val lista = orders.value ?: emptyList()
                    if (lista.isEmpty()) {
                        Text("Brak zamówień")
                    } else {
                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
                            items(lista.size) { idx ->
                                val z = lista[idx]
                                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                    Column(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                                        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                            Column(modifier = Modifier.weight(1f)) {
                                                Text(text = "Zamówienie #${z.id}", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                                                Text(text = "Data: ${z.data}", style = MaterialTheme.typography.labelSmall)
                                                val statusText = if (z.status == 2) "✓ Zrealizowane" else "⧗ W trakcie"
                                                Text(text = "Status: $statusText", style = MaterialTheme.typography.labelSmall)
                                            }
                                            IconButton(onClick = { expandedOrderId = if (expandedOrderId == z.id) null else z.id }) {
                                                Icon(if (expandedOrderId == z.id) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = "Rozwiń")
                                            }
                                        }

                                        if (expandedOrderId == z.id) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            HorizontalDivider()
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(text = "Ilość produktów: ${z.sumaProduktow}", style = MaterialTheme.typography.bodySmall)
                                            Text(text = "Kwota: ${String.format("%.2f", z.kwota)} PLN", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold, color = DeepBurgundy)
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Column(modifier = Modifier.fillMaxWidth()) {
                                                if (z.status != 2) {
                                                    Button(
                                                        onClick = {
                                                            scope.launch {
                                                                val ok = ApiConnector.zmienStatusZamowienia(z.id, 2)
                                                                if (ok) {
                                                                    orders.value = ApiConnector.pobierzHistorieZamowien(user.id ?: 0)
                                                                    expandedOrderId = null
                                                                    Toast.makeText(context, "Status zmieniony", Toast.LENGTH_SHORT).show()
                                                                } else Toast.makeText(context, "Błąd zmiany statusu", Toast.LENGTH_SHORT).show()
                                                            }
                                                        },
                                                        modifier = Modifier.fillMaxWidth(),
                                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                                                    ) { Text("✓ Oznacz jako zrealizowane") }
                                                    Spacer(modifier = Modifier.height(8.dp))
                                                }
                                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                                    Button(
                                                        onClick = {
                                                            Toast.makeText(context, "Edycja niedostępna - brak endpointu", Toast.LENGTH_LONG).show()
                                                        },
                                                        modifier = Modifier.weight(1f),
                                                        colors = ButtonDefaults.buttonColors(containerColor = DeepBurgundy)
                                                    ) { Text("✎ Edytuj") }
                                                    Button(
                                                        onClick = {
                                                            Toast.makeText(context, "Usuwanie niedostępne - brak endpointu", Toast.LENGTH_LONG).show()
                                                        },
                                                        modifier = Modifier.weight(1f),
                                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF44336))
                                                    ) { Text("✕ Usuń") }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) { Text("Zamknij") }
        }
    )
}
