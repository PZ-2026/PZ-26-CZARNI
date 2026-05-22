package com.example.magazyn.api.dtos

data class UzytkownikAdminDTO(
    val id: Int? = null,
    val imie: String,
    val nazwisko: String,
    val email: String,
    val telefon: String?,
    val firma: String?,
    val nip: String?,
    val rola: Int,
    val zablokowany: Boolean = false,
    val haslo: String? = null
)
