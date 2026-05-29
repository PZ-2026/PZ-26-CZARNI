package ui.screens.zaopatrzeniowiec

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.* // Ważne: importuje wszystko co potrzebne dla 'by'
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.magazyn.api.models.HistoriaViewModel
import com.example.magazyn.api.dtos.PozycjaZamowieniaDTO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PozycjeZamowieniaScreen(
    idZamowienia: Int,
    viewModel: HistoriaViewModel,
    onNavigateBack: () -> Unit
) {
    // 1. Pobieranie danych
    LaunchedEffect(idZamowienia) {
        viewModel.fetchPozycjeZamowienia(idZamowienia)
    }

    // 2. Pobieranie stanów (Upewnij się, że w ViewModelu isLoading ma delegat 'by')
    val pozycje = viewModel.pozycjeList
    val loading = viewModel.isLoading.value // Używamy .value, jeśli w ViewModelu jest mutableStateOf

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Produkty w zamówieniu #$idZamowienia") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Wróć")
                    }
                }
            )
        }
    ) { padding ->
        // Używamy Column, aby padding ze Scaffold był poprawnie aplikowany
        Column(modifier = Modifier.padding(padding)) {
            if (loading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (pozycje.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Brak produktów w tym zamówieniu.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(pozycje) { pozycja: PozycjaZamowieniaDTO ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = pozycja.nazwaProduktu,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "ID Produktu: ${pozycja.idProduktu}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                                Text(
                                    text = "${pozycja.ilosc} szt.",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}