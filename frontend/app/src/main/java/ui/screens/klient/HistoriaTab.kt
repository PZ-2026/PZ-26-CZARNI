package ui.screens.klient

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.magazyn.api.dtos.HistoriaZamowieniaDTO
import com.example.magazyn.api.models.HistoriaViewModel
import com.example.magazyn.utils.StatusBadge

@Composable
fun HistoriaTab(id: Int, historiaViewModel: HistoriaViewModel = viewModel<HistoriaViewModel>()) {
    LaunchedEffect(id) {
        historiaViewModel.fetchHistoriaKlient(id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Twoje zamówienia",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 20.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            items(historiaViewModel.historiaList) { zamowienie ->
                HistoryItem(zamowienie)
            }
        }
    }
}

@Composable
fun HistoryItem(zamowienie: HistoriaZamowieniaDTO) {
    val accentColor = when (zamowienie.status) {
        0 -> Color(0xFFB45309)
        1 -> Color(0xFF047857)
        2 -> Color(0xFF1D4ED8)
        else -> Color(0x000000FF)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(modifier = Modifier.height(IntrinsicSize.Min)) {
            // KOLOROWY PASEK PO LEWEJ
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .fillMaxHeight()
                    .background(accentColor)
            )

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // NAGŁÓWEK: Numer i Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Inventory2,
                            null,
                            modifier = Modifier.size(18.dp),
                            tint = accentColor
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Zamówienie #${zamowienie.id}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                    StatusBadge(zamowienie.status)
                }

                Spacer(modifier = Modifier.height(12.dp))

                // DATA
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.CalendarMonth,
                        null,
                        modifier = Modifier.size(16.dp),
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(zamowienie.data, fontSize = 13.sp, color = Color.Gray)
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFF5F5F5))
                Spacer(modifier = Modifier.height(12.dp))

                // PODSUMOWANIE: Liczba produktów i Kwota
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Produkty: ${zamowienie.sumaProduktow} szt.",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Text(
                        "${String.format("%.2f", zamowienie.kwota)} zł",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D4037)
                    )
                }
            }
        }
    }
}