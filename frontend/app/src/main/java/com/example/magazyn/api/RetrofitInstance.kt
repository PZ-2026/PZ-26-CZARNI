package com.example.magazyn.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://192.168.0.247:8080/" // lokalny adres ip komputera, powinno działać
                                                             // dla emulatora adres to: http://10.0.2.2:8080/

    // jezeli adres lokalny nie dziala to:
    // Wpisz komendę: adb reverse tcp:8080 tcp:8080
    // Jeśli terminal nie widzi 'adb', użyj pełnej ścieżki:
    // ~/AppData/Local/Android/Sdk/platform-tools/adb reverse tcp:8080 tcp:8080
    //  Ustaw BASE_URL na "http://localhost:8080/"
    val api: MagazynApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MagazynApi::class.java)
    }
}