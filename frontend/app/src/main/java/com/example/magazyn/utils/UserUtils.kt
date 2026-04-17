package com.example.magazyn.utils

fun getRolaName(rolaId: Int?): String {
    return when (rolaId) {
        0 -> "Klient"
        1 -> "Magazynier"
        2 -> "Zaopatrzeniowiec"
        3 -> "Administrator"
        else -> "Użytkownik"
    }
}