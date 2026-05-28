package com.example.magazyn.api.dtos

import java.math.BigDecimal

data class DaneFinansoweDTO(
    val id: Int? = null,
    val data: String,
    val przychody: BigDecimal = BigDecimal.ZERO,
    val wydatki: BigDecimal = BigDecimal.ZERO,
    val zysk: BigDecimal = BigDecimal.ZERO,
    val typ: String = "PRZYCHODD",
    val idZamowienia: Int? = null
)
