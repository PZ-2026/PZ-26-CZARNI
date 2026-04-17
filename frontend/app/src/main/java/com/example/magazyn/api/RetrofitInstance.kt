package com.example.magazyn.api

import com.example.magazyn.api.interfaces.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://localhost:8080/" // lokalny adres ip komputera, powinno działać
                                                             // dla emulatora adres to: http://10.0.2.2:8080/

    // jezeli adres lokalny nie dziala to:
    // Wpisz komendę: adb reverse tcp:8080 tcp:8080
    // Jeśli terminal nie widzi 'adb', użyj pełnej ścieżki:
    // ~/AppData/Local/Android/Sdk/platform-tools/adb reverse tcp:8080 tcp:8080
    //  Ustaw BASE_URL na "http://localhost:8080/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val uzytkownikApi: UzytkownikApi by lazy {
        retrofit.create(UzytkownikApi::class.java)
    }

    val api: MagazynApi by lazy {
        retrofit.create(MagazynApi::class.java)
    }
    val zamowieniaApi: ZamowieniaApi by lazy {
        retrofit.create(ZamowieniaApi::class.java)
    }
    val dostawcyApi: DostawcyApi by lazy {
        retrofit.create(dostawcyApi::class.java)
    }
    val produktApi: ProduktApi by lazy {
        retrofit.create(ProduktApi::class.java)
    }
}