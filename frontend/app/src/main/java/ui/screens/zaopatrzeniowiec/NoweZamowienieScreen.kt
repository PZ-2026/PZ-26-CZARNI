package ui.screens.zaopatrzeniowiec

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.magazyn.api.models.NoweZamowienieViewModel
import com.example.magazyn.data.api.dtos.ProduktDTO
import androidx.compose.foundation.text.BasicTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoweZamowienieScreen(
    idDostawcy: Int,
    nazwaDostawcy: String,
    idUzytkownika: Int,
    onNavigateBack: () -> Unit,
    viewModel: NoweZamowienieViewModel = viewModel()
) {
    LaunchedEffect(idDostawcy) {
        viewModel.pobierzProdukty(idDostawcy)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.wyczyscStan()
        }
    }

    val produkty by viewModel.produkty
    val koszyk = viewModel.koszyk
    val isLoading by viewModel.isLoading
    val isSubmitting by viewModel.isSubmitting
    val successMessage by viewModel.successMessage

    // --- STAN DLA WYSZUKIWARKI ---
    var searchQuery by remember { mutableStateOf("") }

    // Przetwarzanie listy w czasie rzeczywistym (tylko wyszukiwanie)
    val processedProdukty = remember(produkty, searchQuery) {
        if (searchQuery.isNotBlank()) {
            produkty.filter { it.nazwaProduktu.contains(searchQuery, ignoreCase = true) }
        } else {
            produkty
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Zamów: $nazwaDostawcy", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.wyczyscStan()
                        onNavigateBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Wróć")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            // Pokazuj dolny pasek tylko wtedy, gdy nie ma komunikatu o sukcesie
            if (successMessage == null) {
                Surface(
                    tonalElevation = 8.dp,
                    shadowElevation = 8.dp
                ) {
                    val sumaProduktow = koszyk.values.sum()
                    Button(
                        onClick = { viewModel.zlozZamowienie(idDostawcy, idUzytkownika) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .height(56.dp),
                        enabled = sumaProduktow > 0 && !isSubmitting,
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        if (isSubmitting) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                "Złóż zamówienie ($sumaProduktow szt.)",
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {

            if (successMessage != null) {
                // Ekran Sukcesu
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Sukces",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(72.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        successMessage!!,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = {
                            viewModel.wyczyscStan()
                            onNavigateBack()
                        }
                    ) {
                        Text("Wróć do dostawców")
                    }
                }
            } else if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {

                    // --- SEKCJA WYSZUKIWARKI ---
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Szukaj produktu...") },
                            leadingIcon = { Icon(Icons.Default.Search, null) },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { searchQuery = "" }) {
                                        Icon(Icons.Default.Clear, "Wyczyść")
                                    }
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surface,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    // --- KONIEC SEKCJI WYSZUKIWARKI ---

                    LazyColumn(
                        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(processedProdukty, key = { it.id }) { produkt ->
                            ProduktZamowienieKarta(
                                produkt = produkt,
                                ilosc = koszyk[produkt.id] ?: 0,
                                onIloscZmieniona = { nowaIlosc -> viewModel.zmienIlosc(produkt.id, nowaIlosc) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProduktZamowienieKarta(
    produkt: ProduktDTO,
    ilosc: Int,
    onIloscZmieniona: (Int) -> Unit
) {
    var textValue by remember(ilosc) { mutableStateOf(if (ilosc == 0) "" else ilosc.toString()) }

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                Text(produkt.nazwaProduktu, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Kod: ${produkt.kodKreskowy}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { onIloscZmieniona(ilosc - 1) },
                    enabled = ilosc > 0,
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Mniej", modifier = Modifier.size(16.dp))
                }

                Spacer(modifier = Modifier.width(8.dp))

                BasicTextField(
                    value = textValue,
                    onValueChange = { newValue ->
                        val filteredValue = newValue.filter { it.isDigit() }
                        textValue = filteredValue
                        val newInt = filteredValue.toIntOrNull() ?: 0
                        onIloscZmieniona(newInt)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    textStyle = LocalTextStyle.current.copy(
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize,
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    modifier = Modifier
                        .width(40.dp)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 4.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(
                    onClick = { onIloscZmieniona(ilosc + 1) },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Więcej", tint = MaterialTheme.colorScheme.onPrimaryContainer, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}