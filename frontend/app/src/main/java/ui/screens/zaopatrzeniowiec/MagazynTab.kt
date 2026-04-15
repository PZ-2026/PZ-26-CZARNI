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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.magazyn.api.MagazynItemDTO

data class MagazynItem(
    val id: Int,
    val nazwa: String,
    val kodKreskowy: String,
    val cena: Double,
    val ilosc: Double,
    val jednostka: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MagazynTab(viewModel: MagazynViewModel = viewModel()) {
    // 1. STANY UI
    var searchQuery by remember { mutableStateOf("") }
    var sortAscending by remember { mutableStateOf<Boolean?>(null) }

    // 2. POBIERANIE DANYCH Z VIEWMODELU
    val produktyZBazy by viewModel.produkty.collectAsState()

    // 3. LOGIKA FILTROWANIA I SORTOWANIA (na danych z bazy)
    val filteredList = produktyZBazy
        .filter { it.nazwaProduktu.contains(searchQuery, ignoreCase = true) }
        .let { list ->
            when (sortAscending) {
                true -> list.sortedBy { it.stanMagazynu?.ilosc ?: 0.0 }
                false -> list.sortedByDescending { it.stanMagazynu?.ilosc ?: 0.0 }
                else -> list
            }
        }

    // 4. STRUKTURA WIDOKU
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Stan Magazynowy",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Wyszukiwarka
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Szukaj produktu...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Panel sortowania
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Text("Sortuj po ilości: ", style = MaterialTheme.typography.labelMedium)
                IconButton(onClick = { sortAscending = true }) {
                    Icon(
                        Icons.Default.ArrowUpward,
                        contentDescription = "Rosnąco",
                        tint = if (sortAscending == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    )
                }
                IconButton(onClick = { sortAscending = false }) {
                    Icon(
                        Icons.Default.ArrowDownward,
                        contentDescription = "Malejąco",
                        tint = if (sortAscending == false) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    )
                }
                IconButton(onClick = { sortAscending = null }) {
                    Icon(Icons.Default.Clear, contentDescription = "Reset", tint = MaterialTheme.colorScheme.error)
                }
            }

            // Lista produktów (wyświetla przefiltrowaną listę z bazy)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(filteredList) { produkt ->
                    ProduktCardFromDB(produkt)
                }
            }
        }
    }
}

// Pomocnicza funkcja dla karty (żeby kod był czystszy)
@Composable
fun ProduktCardFromDB(produkt: MagazynItemDTO) {
    ProduktCard(
        item = MagazynItem(
            id = produkt.id,
            nazwa = produkt.nazwaProduktu,
            kodKreskowy = produkt.kodKreskowy,
            cena = produkt.cena,
            ilosc = produkt.stanMagazynu?.ilosc ?: 0.0,
            jednostka = produkt.stanMagazynu?.jednostka ?: "szt."
        )
    )
}
@Composable
fun ProduktCard(item: MagazynItem) {
    val isLowStock = item.ilosc < 10

    ElevatedCard(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = Color.White // Wymuszenie białego koloru karty
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Ikona (zmienia się na ostrzeżenie, jeśli mało towaru)
            Surface(
                modifier = Modifier.size(50.dp),
                shape = RoundedCornerShape(12.dp),
                color = if (isLowStock) MaterialTheme.colorScheme.errorContainer
                else MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = if (isLowStock) Icons.Default.Warning
                        else Icons.Default.Inventory,
                        contentDescription = null,
                        tint = if (isLowStock) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Dane produktu z Twojej bazy (produkty)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.nazwa,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Kod: ${item.kodKreskowy}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
                Text(
                    text = "Cena: ${String.format("%.2f", item.cena)} zł",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            // Stan magazynowy z Twojej bazy (stan_magazynu)
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${item.ilosc}",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isLowStock) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary
                )
                Text(
                    text = item.jednostka,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}