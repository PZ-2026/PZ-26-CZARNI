package com.example.magazyn.api.dtos

import java.math.BigDecimal
import java.time.LocalDate

data class RaportFinansowyDTO(
    val dataPoczatek: LocalDate,
    val dataKoniec: LocalDate,
    val sumaPrzychodow: BigDecimal = BigDecimal.ZERO,
    val sumaWydatkow: BigDecimal = BigDecimal.ZERO,
    val sumaZysku: BigDecimal = BigDecimal.ZERO,
    val liczbaTransakcji: Int = 0,
    val typ: String = "ALL"
)
