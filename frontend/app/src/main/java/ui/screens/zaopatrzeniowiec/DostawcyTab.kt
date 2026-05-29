package ui.screens.zaopatrzeniowiec

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.magazyn.api.models.DostawcyViewModel

// Model danych
data class DostawcaItem(
    val id: Int,
    val nazwa: String,
    val adres: String,
    val telefon: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DostawcyTab(
    viewModel: DostawcyViewModel = viewModel(),
    uzytkownikId: Int = 4
) {
    var wybranyDostawca by remember { mutableStateOf<DostawcaItem?>(null) }
    var searchQuery by remember { mutableStateOf("") } // Stan dla wyszukiwarki

    // Wyzwalanie pobierania danych
    LaunchedEffect(Unit) {
        viewModel.fetchDostawcy()
    }

    if (wybranyDostawca != null) {
        NoweZamowienieScreen(
            idDostawcy = wybranyDostawca!!.id,
            nazwaDostawcy = wybranyDostawca!!.nazwa,
            idUzytkownika = uzytkownikId,
            onNavigateBack = { wybranyDostawca = null }
        )
    } else {
        val dostawcy by viewModel.dostawcyList
        val loading by viewModel.isLoading
        val error by viewModel.errorMessage

        // Logika filtrowania
        val processedDostawcy = remember(dostawcy, searchQuery) {
            val mappedList = dostawcy.map { dto ->
                DostawcaItem(dto.id, dto.nazwa, dto.adres, dto.telefon)
            }
            if (searchQuery.isNotBlank()) {
                mappedList.filter { it.nazwa.contains(searchQuery, ignoreCase = true) }
            } else {
                mappedList
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {

                // --- POLE FILTROWANIA (DODANE) ---
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    placeholder = { Text("Szukaj dostawcy po nazwie...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Wyczyść")
                            }
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                if (loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (error != null) {
                    Text(text = error!!, color = Color.Red, modifier = Modifier.padding(16.dp))
                } else if (processedDostawcy.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Brak dostawców spełniających kryteria.", color = MaterialTheme.colorScheme.outline)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        items(processedDostawcy, key = { it.id }) { dostawca ->
                            DostawcaCard(
                                dostawca = dostawca,
                                onZamowClick = { wybranyDostawca = dostawca }
                            )
                        }
                    }
                }
            }
        }
    }
}

// Funkcja pomocnicza karty i wiersza info pozostają bez zmian (jak w Twoim kodzie)
@Composable
fun DostawcaCard(dostawca: DostawcaItem, onZamowClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.LocalShipping, null, tint = MaterialTheme.colorScheme.primary)
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(dostawca.nazwa, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)
            DostawcaInfoRow(icon = Icons.Default.LocationOn, text = dostawca.adres)
            Spacer(modifier = Modifier.height(8.dp))
            DostawcaInfoRow(icon = Icons.Default.Phone, text = dostawca.telefon)
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(onClick = onZamowClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
                Icon(Icons.Default.ShoppingCart, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Zamów towar")
            }
        }
    }
}

@Composable
fun DostawcaInfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.outline)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}