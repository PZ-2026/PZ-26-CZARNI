package com.example.magazyn.api

import com.example.magazyn.api.interfaces.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthTokenProvider {
    var token: String? = null
}
// jezeli adres lokalny nie dziala to:
// Wpisz komendę: adb reverse tcp:8080 tcp:8080
// Jeśli terminal nie widzi 'adb', użyj pełnej ścieżki:
// ~/AppData/Local/Android/Sdk/platform-tools/adb reverse tcp:8080 tcp:8080
//  Ustaw BASE_URL na "http://localhost:8080/"

object RetrofitInstance {
    const val BASE_URL = "http://10.0.2.2:8080/" 
  
        private val retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
        }

        private val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        private val authInterceptor = Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()
            AuthTokenProvider.token?.let { token ->
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(requestBuilder.build())
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()

        val uzytkownikApi: UzytkownikApi by lazy {
            retrofit.create(UzytkownikApi::class.java)
        }

        val zamowieniaApi: ZamowieniaApi by lazy {
            retrofit.create(ZamowieniaApi::class.java)
        }

        val dostawcyApi: DostawcyApi by lazy {
            retrofit.create(DostawcyApi::class.java)
        }

        val produktApi: ProduktApi by lazy {
            retrofit.create(ProduktApi::class.java)
        }

        val magazynApi: MagazynApi by lazy {
            retrofit.create(MagazynApi::class.java)
        }

        val adminApi: AdminApi by lazy {
            retrofit.create(AdminApi::class.java)
        }

        val magazynierApi: MagazynierApi by lazy {
            retrofit.create(MagazynierApi::class.java)
        }
    }
