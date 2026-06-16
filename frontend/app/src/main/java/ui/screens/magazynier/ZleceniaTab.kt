package ui.screens.magazynier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.magazyn.api.RetrofitInstance
import com.example.magazyn.api.dtos.ZamowienieKlientaDTO
import kotlinx.coroutines.launch
import retrofit2.Response


@Composable
fun ZleceniaTab(magazynierId: Int) {
    var zamowienia by remember { mutableStateOf<List<ZamowienieKlientaDTO>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            zamowienia = RetrofitInstance.magazynierApi.getZamowieniaDoSpakowania(magazynierId)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else if (zamowienia.isEmpty()) {
            Text(
                text = "Brak zleceń do wyświetlenia",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(zamowienia) { zamowienie ->
                    ZlecenieCard(zamowienie) { nowyStatus ->
                        scope.launch {
                            try {
                                val response = RetrofitInstance.magazynierApi.zmienStatusZamowienia(zamowienie.id, nowyStatus)
                                if (response.isSuccessful) {
                                    zamowienia = RetrofitInstance.magazynierApi.getZamowieniaDoSpakowania(magazynierId)
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ZlecenieCard(zamowienie: ZamowienieKlientaDTO, onStatusChange: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Zamówienie #${zamowienie.id}", fontWeight = FontWeight.Bold)
                StatusChip(zamowienie.status)
            }
            Text(text = "Klient: ${zamowienie.imieKlienta} ${zamowienie.nazwiskoKlienta}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Data: ${zamowienie.data}", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            
            Text(text = "Lista produktów:", fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodySmall)
            
            zamowienie.produkty.forEach { prod ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "- ${prod.nazwaProduktu} (${prod.ilosc} szt.)", style = MaterialTheme.typography.bodyMedium)
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(4.dp)
                    ) {
                        Text(
                            text = prod.strefa ?: "Brak strefy",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (zamowienie.status == 0) {
                    Button(
                        onClick = { onStatusChange(1) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Rozpocznij pakowanie")
                    }
                } else if (zamowienie.status == 1) {
                    Button(
                        onClick = { onStatusChange(2) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Zakończ i skompletuj")
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: Int) {
    val (text, color) = when (status) {
        0 -> "Nowe" to Color(0xFFFFA000)
        1 -> "W pakowaniu" to Color(0xFF1976D2)
        else -> "Nieznany" to Color.Gray
    }
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}
