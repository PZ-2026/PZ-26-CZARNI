package ui.screens.magazynier

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
            Text(text = "Zamówienie #${zamowienie.id}", fontWeight = FontWeight.Bold)
            Text(text = "Klient: ${zamowienie.imieKlienta} ${zamowienie.nazwiskoKlienta}")
            Text(text = "Data: ${zamowienie.data}")
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            Text(text = "Produkty:", fontWeight = FontWeight.SemiBold)
            zamowienie.produkty.forEach { prod ->
                Text(text = "- ${prod.nazwaProduktu} (${prod.ilosc} szt.)")
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (zamowienie.status == 0) {
                    Button(onClick = { onStatusChange(1) }) {
                        Text("Rozpocznij pakowanie")
                    }
                } else if (zamowienie.status == 1) {
                    Button(
                        onClick = { onStatusChange(2) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                    ) {
                        Text("Skompletowano")
                    }
                }
            }
        }
    }
}
