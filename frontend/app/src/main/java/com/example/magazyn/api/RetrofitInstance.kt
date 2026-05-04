package com.example.magazyn.api

import com.example.magazyn.api.interfaces.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8080/" 

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
    
    private val client = OkHttpClient.Builder()
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

    val magazynierApi: MagazynierApi by lazy {
        retrofit.create(MagazynierApi::class.java)
    }
}
