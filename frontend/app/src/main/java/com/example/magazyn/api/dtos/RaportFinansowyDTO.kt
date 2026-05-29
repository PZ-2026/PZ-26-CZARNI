package com.example.magazyn.api.dtos

import java.math.BigDecimal

data class RaportFinansowyDTO(
    val dataPoczatek: String? = null,
    val dataKoniec: String? = null,
    val sumaPrzychodow: BigDecimal = BigDecimal.ZERO,
    val sumaWydatkow: BigDecimal = BigDecimal.ZERO,
    val sumaZysku: BigDecimal = BigDecimal.ZERO,
    val liczbaTransakcji: Int = 0,
    val typ: String = "ALL"
)
