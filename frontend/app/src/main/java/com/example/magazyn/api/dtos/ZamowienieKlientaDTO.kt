package com.example.magazyn.api.dtos

data class ZamowienieKlientaDTO(
    val id: Int,
    val data: String,
    val imieKlienta: String,
    val nazwiskoKlienta: String,
    val status: Int,
    val produkty: List<PozycjaZamowieniaDTO>
) {
    data class PozycjaZamowieniaDTO(
        val produktId: Int,
        val nazwaProduktu: String,
        val ilosc: Int,
        val kodKreskowy: String
    )
}
