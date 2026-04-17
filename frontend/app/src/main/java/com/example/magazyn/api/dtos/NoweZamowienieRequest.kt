package com.example.magazyn.api.dtos

data class NoweZamowienieRequest(
    val idDostawcy: Int,
    val idUzytkownika: Int,
    val pozycje: List<PozycjaZamowienia>
)

data class PozycjaZamowienia(
    val idProduktu: Int,
    val ilosc: Int
)