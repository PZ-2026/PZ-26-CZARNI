package com.example.magazyn.api.dtos

import com.google.gson.annotations.SerializedName

/**
 * Model danych odpowiadający tabeli 'stan_magazynu' w bazie
 * Służy do odebrania szczegółów o ilości i jednostce produktu.
 */
data class StanMagazynuDTO(
    val id: Int,
    @SerializedName("ilosc")
    val ilosc: Int,
)