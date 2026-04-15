package com.example.magazyn.api

data class MagazynItemDTO(
    val id: Int,
    val nazwaProduktu: String,
    val kodKreskowy: String,
    val cena: Double,
    val stanMagazynu: StanMagazynuDTO?
)
