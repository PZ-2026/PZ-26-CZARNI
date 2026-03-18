package ui.screens.zaopatrzeniowiec

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// Model danych łączący Twoje tabele zamówień
data class HistoriaZamowienia(
    val id: Int,
    val data: String,
    val dostawca: String,
    val status: Int, // 1: Oczekujące, 2: Zrealizowane
    val sumaProduktow: Int
)

@Composable
fun HistoriaTab() {
    // Przykładowe dane na sztywno, zgodne z Twoją strukturą bazy
    val historia = listOf(
        HistoriaZamowienia(101, "2024-03-15 10:30", "Hurtownia Nabiału 'Mleczko'", 2, 15),
        HistoriaZamowienia(102, "2024-03-17 08:45", "Piekarnia Chrupiąca", 1, 5),
        HistoriaZamowienia(103, "2024-03-18 12:00", "Dystrybucja Jaj 'Kurka'", 1, 10)
    )

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
                text = "Historia Zamówień",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(historia) { zamowienie ->
                    ZamowienieCard(zamowienie)
                }
            }
        }
    }
}

@Composable
fun ZamowienieCard(zamowienie: HistoriaZamowienia) {
    val statusText = if (zamowienie.status == 2) "Zrealizowane" else "W trakcie"
    val statusColor = if (zamowienie.status == 2) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error

    ElevatedCard(
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

                // Badge statusu
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
                text = zamowienie.dostawca,
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
                    text = zamowienie.data,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)

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