package com.example.magazyn.api.dtos

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

/**
 * Model danych odpowiadający tabeli 'stan_magazynu' w bazie.
 * Rozszerzony o pola produktu dla widoku administratora.
 */
data class StanMagazynuDTO(
    val id: Int? = null,
    @SerializedName("ilosc")
    val ilosc: Int,
    @SerializedName("nazwaProduktu")
    val nazwa: String? = null,
    @SerializedName("cena")
    val cena: BigDecimal? = null,
    @SerializedName("kodKreskowy")
    val kodKreskowy: String? = null,
    @SerializedName("jednostka")
    val jednostka: String? = null,
    val idProduktu: Int? = null
)
