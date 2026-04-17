package com.example.magazyn.api.dtos

data class RegisterRequest(
    val imie: String,
    val nazwisko: String,
    val telefon: String,
    val email: String,
    val haslo: String,
    val firma: String?,
    val nip: String?,
    val rola: Int = 0
)
