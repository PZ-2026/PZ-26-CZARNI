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
fun MagazynTab() {
    var searchQuery by remember { mutableStateOf("") }
    // Stan sortowania: true = rosnąco, false = malejąco, null = brak sortowania
    var sortAscending by remember { mutableStateOf<Boolean?>(null) }

    // Przykładowe dane spożywcze zgodne z Twoją bazą
    val produktySpozywcze = remember {
        mutableStateListOf(
            MagazynItem(1, "Mleko 3.2% 1L", "5901234123", 3.50, 45.0, "szt."),
            MagazynItem(2, "Chleb Żytni", "5906543654", 4.20, 5.0, "szt."), // Niski stan!
            MagazynItem(3, "Masło Ekstra 200g", "5909988998", 7.99, 120.0, "szt."),
            MagazynItem(4, "Jaja Wolny Wybieg (10szt)", "5907766554", 12.00, 3.0, "opak."), // Niski stan!
            MagazynItem(5, "Mąka Pszenna 1kg", "5904433221", 2.80, 50.0, "kg.")
        )
    }

    // Filtrowanie i sortowanie
    val filteredList = produktySpozywcze
        .filter { it.nazwa.contains(searchQuery, ignoreCase = true) }
        .let { list ->
            when (sortAscending) {
                true -> list.sortedBy { it.ilosc }
                false -> list.sortedByDescending { it.ilosc }
                else -> list
            }
        }

    val gradientBg = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            MaterialTheme.colorScheme.background
        )
    )

    Box(modifier = Modifier.fillMaxSize().background(gradientBg)) {
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

            // --- PANEL FILTRÓW (Sortowanie) ---
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

            // Lista
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(filteredList) { item ->
                    ProduktCard(item)
                }
            }
        }
    }
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