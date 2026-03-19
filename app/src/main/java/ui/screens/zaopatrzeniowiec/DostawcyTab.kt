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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

data class DostawcaItem(
    val id: Int,
    val nazwa: String,
    val adres: String,
    val telefon: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DostawcyTab() {
    var searchQuery by remember { mutableStateOf("") }

    // Przykładowe dane zgodne ze strukturą Twojej bazy
    val listaDostawcow = remember {
        listOf(
            DostawcaItem(1, "Hurtownia Nabiału ", "ul. Wiejska 5, 00-001 Warszawa", "+48 500 100 200"),
            DostawcaItem(2, "Piekarnia Chrupiąca", "ul. Piekarzy 12, 31-000 Kraków", "+48 12 444 55 66"),
            DostawcaItem(3, "Zakłady Mięsne 'Polski Smak'", "al. Jerozolimskie 100, Warszawa", "+48 600 700 800"),
            DostawcaItem(4, "Dystrybucja Jaj 'Kurka'", "ul. Polna 2, 80-001 Gdańsk", "+48 58 333 22 11")
        )
    }

    val filteredDostawcy = listaDostawcow.filter {
        it.nazwa.contains(searchQuery, ignoreCase = true)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) // Czysty SoftPinkBG
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)){
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Baza Dostawców",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Wyszukiwarka po nazwie dostawcy
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Szukaj dostawcy po nazwie...") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Lista dostawców
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(filteredDostawcy) { dostawca ->
                    DostawcaCard(dostawca)
                }
            }
        }
    }
}

@Composable
fun DostawcaCard(dostawca: DostawcaItem) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Ikona ciężarówki (LocalShipping) dla dostawcy
                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.LocalShipping,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = dostawca.nazwa,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)

            // Dane kontaktowe i adresowe z Twojej bazy
            DostawcaInfoRow(icon = Icons.Default.LocationOn, text = dostawca.adres)
            Spacer(modifier = Modifier.height(8.dp))
            DostawcaInfoRow(icon = Icons.Default.Phone, text = dostawca.telefon)

            Spacer(modifier = Modifier.height(12.dp))

            // Przycisk szybkiego kontaktu (opcjonalny)
            OutlinedButton(
                onClick = { /* TODO: Wywołanie numeru telefonu */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Zamów towar")
            }
        }
    }
}

@Composable
fun DostawcaInfoRow(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}