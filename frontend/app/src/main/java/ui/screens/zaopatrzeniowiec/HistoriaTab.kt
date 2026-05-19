package ui.screens.zaopatrzeniowiec

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.magazyn.api.models.HistoriaViewModel

// Model danych pozostaje bez zmian
data class HistoriaZamowienia(
    val id: Int,
    val data: String,
    val nazwaDostawcy: String,
    val status: Int,
    val sumaProduktow: Int
) {
    val formatowanaData: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() = try {
            val parsed = java.time.ZonedDateTime.parse(data)
            parsed.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        } catch (e: Exception) { data }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoriaTab(uzytkownikId: Int, viewModel: HistoriaViewModel = viewModel()) {

    // Zarządzanie nawigacją wewnątrz zakładki
    var currentView by remember { mutableStateOf("LISTA") } // LISTA, SZCZEGOLY, POZYCJE
    var selectedOrder by remember { mutableStateOf<HistoriaZamowienia?>(null) }

    when (currentView) {
        "SZCZEGOLY" -> {
            selectedOrder?.let { zamowienie ->
                SzczegolyZamowieniaScreen(
                    zamowienie = zamowienie,
                    onNavigateBack = { currentView = "LISTA" },
                    onNavigateToItems = { currentView = "POZYCJE" }, // Nawigacja do produktów
                    viewModel = viewModel
                )
            }
        }
        "POZYCJE" -> {
            selectedOrder?.let { zamowienie ->
                PozycjeZamowieniaScreen(
                    idZamowienia = zamowienie.id,
                    viewModel = viewModel,
                    onNavigateBack = { currentView = "SZCZEGOLY" } // Powrót do szczegółów
                )
            }
        }
        else -> {
            // WIDOK LISTY
            LaunchedEffect(Unit) {
                viewModel.fetchHistoria(uzytkownikId)
            }

            val historia = viewModel.historiaList
            val loading by viewModel.isLoading
            val error by viewModel.errorMessage
            var searchQuery by remember { mutableStateOf("") }
            var sortDescending by remember { mutableStateOf(true) }

            val processedList = remember(historia, searchQuery, sortDescending) {
                val mappedList = historia.map { dto ->
                    HistoriaZamowienia(dto.id, dto.data, dto.nazwaDostawcy, dto.status, dto.sumaProduktow.toInt())
                }
                val filteredList = if (searchQuery.isNotBlank()) {
                    mappedList.filter {
                        it.nazwaDostawcy.contains(searchQuery, ignoreCase = true) ||
                                it.formatowanaData.contains(searchQuery, ignoreCase = true)
                    }
                } else mappedList
                if (sortDescending) filteredList.sortedByDescending { it.data } else filteredList.sortedBy { it.data }
            }

            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
                Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(text = "Historia Zamówień", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold))
                    Spacer(modifier = Modifier.height(16.dp))

                    // Filtrowanie
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Szukaj dostawcy lub daty...") },
                        leadingIcon = { Icon(Icons.Default.Search, null) },
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                        Text("Wyniki: ${processedList.size}", style = MaterialTheme.typography.bodySmall)
                        FilterChip(
                            selected = true,
                            onClick = { sortDescending = !sortDescending },
                            label = { Text(if (sortDescending) "Od najnowszych" else "Od najstarszych") },
                            leadingIcon = { Icon(if (sortDescending) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward, null, Modifier.size(16.dp)) }
                        )
                    }

                    if (loading) {
                        Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), contentPadding = PaddingValues(bottom = 100.dp)) {
                            items(processedList, key = { it.id }) { zamowienie ->
                                ZamowienieCard(
                                    zamowienie = zamowienie,
                                    onClick = {
                                        selectedOrder = zamowienie
                                        currentView = "SZCZEGOLY"
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ZamowienieCard pozostaje bez zmian (jak w Twoim kodzie)
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ZamowienieCard(zamowienie: HistoriaZamowienia, onClick: () -> Unit) {
    val statusText = if (zamowienie.status == 2) "Zrealizowane" else "W trakcie"
    val statusColor = if (zamowienie.status == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
                Text("Zamówienie #${zamowienie.id}", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.outline)
                Surface(color = statusColor.copy(alpha = 0.1f), shape = RoundedCornerShape(8.dp)) {
                    Text(statusText, Modifier.padding(horizontal = 8.dp, vertical = 4.dp), style = MaterialTheme.typography.labelSmall, color = statusColor, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(zamowienie.nazwaDostawcy, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Event, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.outline)
                Spacer(modifier = Modifier.width(4.dp))
                Text(zamowienie.formatowanaData, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
            }
            HorizontalDivider(Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.List, null, Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Liczba produktów: ${zamowienie.sumaProduktow}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}