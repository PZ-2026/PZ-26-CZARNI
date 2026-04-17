package com.example.magazyn.api.dtos

import com.google.gson.annotations.SerializedName

data class DostawcaDTO(
    val id: Int,
    @SerializedName("nazwaDostawcy")
    val nazwa: String,
    val adres: String,
    val telefon: String
)