package com.example.magazyn.api.dtos

import com.example.magazyn.api.dtos.StanMagazynuDTO

data class MagazynItemDTO(
    val id: Int,
    val nazwaProduktu: String,
    val kodKreskowy: String,
    val cena: Double,
    val jednostka: String,
    val stanMagazynu: StanMagazynuDTO?
)