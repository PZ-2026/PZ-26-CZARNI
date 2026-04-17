package com.example.magazyn.api.dtos

import com.google.gson.annotations.SerializedName

data class HistoriaZamowieniaDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("data")
    val data: String, // Przyjdzie jako tekst w formacie ISO-8601

    @SerializedName("nazwaDostawcy")
    val nazwaDostawcy: String,

    @SerializedName("status")
    val status: Int,

    @SerializedName("sumaProduktow")
    val sumaProduktow: Long
)