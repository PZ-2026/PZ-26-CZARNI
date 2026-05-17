package com.example.magazyn.api.dtos

data class MagazynItemDTO(
    val id: Int,
    val nazwaProduktu: String,
    val kodKreskowy: String,
    val cena: Double,
    val jednostka: String,
    val stanMagazynu: StanMagazynuDTO?,
    val strefa: String?
)
