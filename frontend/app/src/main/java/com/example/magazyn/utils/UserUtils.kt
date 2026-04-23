package com.example.magazyn.utils

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun getRolaName(rolaId: Int?): String {
    return when (rolaId) {
        0 -> "Klient"
        1 -> "Magazynier"
        2 -> "Zaopatrzeniowiec"
        3 -> "Administrator"
        else -> "Użytkownik"
    }
}

@Composable
fun StatusBadge(statusId: Int): Unit {
    lateinit var _text: String
    lateinit var _icon: ImageVector
    var _contentColor: Color
    var _containerColor: Color
    when (statusId) {
        0 -> {
            _text = "w trakcie"
            _icon = Icons.Default.Schedule
            _contentColor = Color(0xFFB45309)
            _containerColor = Color(0xFFFFFBEB)
        }
        1 -> {
            _text = "do odbioru"
            _icon = Icons.Default.CheckCircleOutline
            _contentColor = Color(0xFF047857)
            _containerColor = Color(0xFFECFDF5)
        }
        2 -> {
            _text = "odebrano"
            _icon = Icons.Default.Schedule
            _contentColor = Color(0xFF1D4ED8)
            _containerColor = Color(0xFFEFF6FF)
        }
        else -> return // Jeśli ID jest nieznane, nic nie rysuj
    }

    Surface(
        color = _containerColor,
        shape = RoundedCornerShape(50)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = _icon,
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = _contentColor
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = _text,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = _contentColor
            )
        }
    }
}