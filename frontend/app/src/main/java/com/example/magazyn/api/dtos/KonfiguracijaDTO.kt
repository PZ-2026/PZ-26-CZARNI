package com.example.magazyn.api.dtos

data class KonfiguracijaDTO(
    val id: Int? = null,
    val nazwaParametru: String,
    val wartoscParametru: String,
    val typParametru: String = "STRING",
    val opis: String = "",
    val aktywna: Boolean = true
)
