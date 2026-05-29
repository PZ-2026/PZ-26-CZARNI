package com.example.magazyn.api.dtos

import java.math.BigDecimal

data class PanelAdminaDTO(
    val liczbaUzytkownikow: Int = 0,
    val liczbaProduktu: Int = 0,
    val liczbaMagazynow: Int = 0,
    val liczbaDostawow: Int = 0,
    val przychodyMiesiac: BigDecimal = BigDecimal.ZERO,
    val wydatkiMiesiac: BigDecimal = BigDecimal.ZERO,
    val zyskMiesiac: BigDecimal = BigDecimal.ZERO,
    val liczbaZamowienWProgress: Int = 0,
    val liczbaZamowienDoRealizacji: Int = 0,
    val liczbaProductowPonizejProgu: Int = 0
)
