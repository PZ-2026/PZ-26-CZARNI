package com.example.magazyn

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.magazyn.ui.theme.MagazynTheme
import com.example.magazyn.ui.screens.LoginScreen
import ui.screens.zaopatrzeniowiec.ZaopatrzeniowiecDashboard

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MagazynTheme {
                var currentUserRole by remember { mutableStateOf(UserRole.NONE) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    when (currentUserRole) {
                        UserRole.NONE -> {
                            LoginScreen(onLoginSuccess = { role ->
                                currentUserRole = role
                            })
                        }
                        UserRole.ZAOPATRZENIOWIEC -> {
                            // funkcja wylogownaia
                            ZaopatrzeniowiecDashboard(onLogut = { currentUserRole = UserRole.NONE })
                        }
                        UserRole.ADMIN -> {
                            PlaceholderScreen("Panel Admina") { currentUserRole = UserRole.NONE }
                        }
                        UserRole.MAGAZYNIER -> {
                            PlaceholderScreen("Panel Magazyniera") { currentUserRole = UserRole.NONE }
                        }
                        UserRole.KLIENT -> {
                            PlaceholderScreen("Panel Klienta") { currentUserRole = UserRole.NONE }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PlaceholderScreen(title: String, onLogout: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = title, fontSize = 24.sp)
        Button(onClick = onLogout) {
            Text("Wyloguj")
        }
    }
}