package com.example.magazyn.data.api.dtos

import com.google.gson.annotations.SerializedName

data class ProduktDTO(
    @SerializedName("id")
    val id: Int,

    @SerializedName("nazwaProduktu")
    val nazwaProduktu: String,

    @SerializedName("opisProduktu")
    val opisProduktu: String?,

    @SerializedName("kodKreskowy")
    val kodKreskowy: String,

    @SerializedName("cena")
    val cena: Double,

    @SerializedName("idDostawcy")
    val idDostawcy: Int,

    val jednostka: String
)