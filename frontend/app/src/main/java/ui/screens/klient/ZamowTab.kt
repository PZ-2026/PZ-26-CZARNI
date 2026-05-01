package ui.screens.klient

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.BasicTextField
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.magazyn.api.dtos.MagazynItemDTO
import com.example.magazyn.api.models.HistoriaViewModel
import com.example.magazyn.api.models.KlientViewModel
import com.example.magazyn.data.api.dtos.ProduktDTO

@Composable
fun ZamowTab(id: Int, klientViewModel: KlientViewModel = viewModel<KlientViewModel>()) {
    LaunchedEffect(Unit) {
        klientViewModel.pobierzProdukty()
    }
    var produkty = klientViewModel.produkty

    var searchQuery by remember { mutableStateOf("") }

    // Stan przechowujący wybrane ilości: ID produktu -> Ilość
    val wybraneIlosci = remember { mutableStateMapOf<Int, Int>() }

    // Obliczanie sumy
    val sumaKoszyka = produkty.sumOf { (wybraneIlosci[it.id] ?: 0) * it.cena }
    val liczbaProduktow = wybraneIlosci.values.sumOf { it }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Zamów Towar",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 20.dp)
            )

            // PASEK WYSZUKIWANIA
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                placeholder = { Text("Szukaj produktu...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )

            // LISTA PRODUKTÓW
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 100.dp) // Miejsce na przycisk na dole
            ) {
                items(produkty.filter { it.nazwaProduktu.contains(searchQuery, ignoreCase = true) }) { produkt ->
                    ProduktItem(
                        produkt = produkt,
                        ilosc = wybraneIlosci[produkt.id] ?: 0,
                        onIloscChange = { nowaIlosc ->
                            if (nowaIlosc > 0) wybraneIlosci[produkt.id] = nowaIlosc
                            else wybraneIlosci.remove(produkt.id)
                        }
                    )
                }
            }
        }

        // PRZYCISK ZAMÓWIENIA NA DOLE
        if (liczbaProduktow > 0) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF5D4037), // Twój burgundowy kolor
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Razem: ${String.format("%.2f", sumaKoszyka)} zł", color = Color.White, fontWeight = FontWeight.Bold)
                        Text("$liczbaProduktow poz.", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
                    }

                    Button(
                        onClick = { /* Tu logika wysyłania zamówienia do bazy */ },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Zamów teraz", color = Color(0xFF5D4037), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ProduktItem(produkt: MagazynItemDTO, ilosc: Int, onIloscChange: (Int) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // IKONA (bez zmian)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFF2EAE8), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Inventory2, contentDescription = null, tint = Color(0xFF5D4037))
            }

            Column(modifier = Modifier.padding(start = 12.dp).weight(1f)) {
                Text(produkt.nazwaProduktu, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("Kod: ${produkt.kodKreskowy}", color = Color.Gray, fontSize = 11.sp)
            }

            Column(horizontalAlignment = Alignment.End) {
                Text("${String.format("%.2f", produkt.cena)} zł", fontWeight = FontWeight.Bold, color = Color(0xFF5D4037))
                Text("Max: ${produkt.stanMagazynu!!.ilosc} ${produkt.stanMagazynu.jednostka}.", color = Color.Gray, fontSize = 10.sp)

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFF5F0F0)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        IconButton(
                            onClick = { if (ilosc > 0) onIloscChange(ilosc - 1) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Remove, null, modifier = Modifier.size(16.dp))
                        }

                        BasicTextField(
                            value = ilosc.toString(),
                            onValueChange = { newValue ->
                                val num = newValue.toIntOrNull() ?: 0
                                if (num <= produkt.stanMagazynu.ilosc) onIloscChange(num)
                            },
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.width(35.dp)
                        )

                        IconButton(
                            onClick = { if (ilosc < produkt.stanMagazynu.ilosc) onIloscChange(ilosc + 1) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.Add, null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}