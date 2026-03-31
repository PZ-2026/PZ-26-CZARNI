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
import ui.screens.klient.KlientDashboard
import ui.screens.login.LoginScreen
import ui.screens.zaopatrzeniowiec.ZaopatrzeniowiecDashboard
import ui.screens.login.RegisterScreen
import ui.screens.magazynier.MagazynierDashboard

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MagazynTheme {
                var currentUserRole by remember { mutableStateOf(UserRole.NONE) }
                // Dodajemy stan pomocniczy dla ekranu rejestracji
                var isRegistering by remember { mutableStateOf(false) }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Logika wyboru ekranu
                    if (isRegistering) {
                        RegisterScreen(
                            onRegisterSuccess = { isRegistering = false },
                            onBackToLogin = { isRegistering = false }
                        )
                    } else {
                        when (currentUserRole) {
                            UserRole.NONE -> {
                                LoginScreen(
                                    onLoginSuccess = { role ->
                                        currentUserRole = role
                                    },
                                    onNavigateToRegister = {
                                        // TO ROZWIĄZUJE TWÓJ BŁĄD
                                        isRegistering = true
                                    }
                                )
                            }
                            UserRole.ZAOPATRZENIOWIEC -> {
                                ZaopatrzeniowiecDashboard(onLogut = { currentUserRole = UserRole.NONE })
                            }
                            UserRole.MAGAZYNIER -> {
                                MagazynierDashboard(onLogout = { currentUserRole = UserRole.NONE })
                            }
                            UserRole.KLIENT -> {
                                // Tutaj wstaw swój MainDashboard() dla klienta
                                KlientDashboard(onLogout = {
                                    currentUserRole = UserRole.NONE
                                })
                            }
                            // Pozostałe role...
                            else -> PlaceholderScreen("Panel") { currentUserRole = UserRole.NONE }
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

@Composable
fun AppNavigation() {
    // Prosty stan przechowujący nazwę aktualnego ekranu
    var currentScreen by remember { mutableStateOf("login") }

    when (currentScreen) {
        "login" -> LoginScreen(
            onLoginSuccess = { role ->
                // Logika po zalogowaniu, np. przejście do Dashboardu
            },
            onNavigateToRegister = {
                currentScreen = "register" // Przełączamy na rejestrację
            }
        )
        "register" -> RegisterScreen(
            onRegisterSuccess = {
                currentScreen = "login" // Po rejestracji wracamy do logowania
            },
            onBackToLogin = {
                currentScreen = "login" // Przycisk powrotu
            }
        )
    }
}