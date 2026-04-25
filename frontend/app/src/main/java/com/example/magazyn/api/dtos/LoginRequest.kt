package com.example.magazyn.api.dtos

data class LoginRequest(
    val login: String,
    val haslo: String
)

data class LoginResponse(
    val uzytkownik: UzytkownikDTO,
    val token: String
)
