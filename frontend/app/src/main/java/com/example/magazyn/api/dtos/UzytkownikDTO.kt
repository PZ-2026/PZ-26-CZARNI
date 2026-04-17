package com.example.magazyn.api.dtos

data class UzytkownikDTO(
    val id: Int,
    val imie: String,
    val nazwisko: String,
    val email: String,
    val telefon: String,
    val firma: String?,
    val nip: String?,
    val rola: Int
)