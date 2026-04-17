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
        } catch (e: Exception) {
            data
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoriaTab(uzytkownikId: Int, viewModel: HistoriaViewModel = viewModel()) {

    var wybraneZamowienie by remember { mutableStateOf<HistoriaZamowienia?>(null) }

    // 2. Jeśli coś wybrano, pokazujemy nowy ekran
    if (wybraneZamowienie != null) {
        SzczegolyZamowieniaScreen(
            zamowienie = wybraneZamowienie!!,
            onNavigateBack = { wybraneZamowienie = null },
            viewModel = viewModel
        )
    }
    // 3. W przeciwnym razie pokazujemy standardową listę
    else {
        LaunchedEffect(Unit) {
            viewModel.fetchHistoria(uzytkownikId)
        }

        val historia by viewModel.historiaList
        val loading by viewModel.isLoading
        val error by viewModel.errorMessage

        // Stany dla filtrów i sortowania
        var searchQuery by remember { mutableStateOf("") }
        var sortDescending by remember { mutableStateOf(true) } // Domyślnie od najnowszych

        // Dynamiczne filtrowanie i sortowanie listy
        val processedList = remember(historia, searchQuery, sortDescending) {
            // 1. Mapujemy DTO na nasz model wizualny
            val mappedList = historia.map { dto ->
                HistoriaZamowienia(
                    id = dto.id,
                    data = dto.data,
                    nazwaDostawcy = dto.nazwaDostawcy,
                    status = dto.status,
                    sumaProduktow = dto.sumaProduktow.toInt()
                )
            }

            // 2. Filtrujemy po wpisanym tekście (dostawca lub data)
            val filteredList = if (searchQuery.isNotBlank()) {
                mappedList.filter {
                    it.nazwaDostawcy.contains(searchQuery, ignoreCase = true) ||
                            it.formatowanaData.contains(searchQuery, ignoreCase = true)
                }
            } else {
                mappedList
            }

            // 3. Sortujemy (jako że data ISO-8601 ładnie sortuje się jako string, możemy użyć jej bezpośrednio)
            if (sortDescending) {
                filteredList.sortedByDescending { it.data }
            } else {
                filteredList.sortedBy { it.data }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Historia Zamówień",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // --- SEKCJA FILTRÓW I SORTOWANIA ---
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Szukaj dostawcy lub daty...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Wyczyść")
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Liczba wyników: ${processedList.size}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )

                    FilterChip(
                        selected = true,
                        onClick = { sortDescending = !sortDescending },
                        label = { Text(if (sortDescending) "Od najnowszych" else "Od najstarszych") },
                        leadingIcon = {
                            Icon(
                                imageVector = if (sortDescending) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                contentDescription = "Sortuj",
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                // --- KONIEC SEKCJI FILTRÓW ---

                if (loading) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (error != null) {
                    Text(text = error!!, color = Color.Red, modifier = Modifier.padding(16.dp))
                } else if (processedList.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            "Brak wyników do wyświetlenia.",
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 100.dp)
                    ) {
                        items(processedList, key = { it.id }) { zamowienie ->
                            ZamowienieCard(
                                zamowienie = zamowienie,
                                onClick = { wybraneZamowienie = zamowienie }
                            )
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ZamowienieCard(zamowienie: HistoriaZamowienia, onClick: () -> Unit) {
    val statusText = if (zamowienie.status == 2) "Zrealizowane" else "W trakcie"
    val statusColor =
        if (zamowienie.status == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Zamówienie #${zamowienie.id}",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.outline
                )

                Surface(
                    color = statusColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = statusText,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = zamowienie.nazwaDostawcy,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Event,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = zamowienie.formatowanaData,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                thickness = 0.5.dp
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.List,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Liczba produktów: ${zamowienie.sumaProduktow}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}