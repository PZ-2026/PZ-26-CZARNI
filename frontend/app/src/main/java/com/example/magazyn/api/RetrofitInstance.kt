package com.example.magazyn.api

import com.example.magazyn.BuildConfig
import com.example.magazyn.api.interfaces.*
import com.example.magazyn.utils.AppSettings
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AuthTokenProvider {
    var token: String? = null
}

object RetrofitInstance {
    @Volatile
    private var baseUrl: String = BuildConfig.BACKEND_URL

    val BASE_URL: String
        get() = baseUrl

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

    val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(logging)
        .build()

    @Volatile
    private var retrofit: Retrofit? = null

    fun configure(url: String) {
        val normalizedUrl = AppSettings.normalizeBackendUrl(url)
        if (baseUrl != normalizedUrl) {
            baseUrl = normalizedUrl
            retrofit = null
        }
    }

    private fun getRetrofit(): Retrofit {
        return retrofit ?: synchronized(this) {
            retrofit ?: Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .also { retrofit = it }
        }
    }

    val uzytkownikApi: UzytkownikApi
        get() = getRetrofit().create(UzytkownikApi::class.java)

    val zamowieniaApi: ZamowieniaApi
        get() = getRetrofit().create(ZamowieniaApi::class.java)

    val dostawcyApi: DostawcyApi
        get() = getRetrofit().create(DostawcyApi::class.java)

    val produktApi: ProduktApi
        get() = getRetrofit().create(ProduktApi::class.java)

    val magazynApi: MagazynApi
        get() = getRetrofit().create(MagazynApi::class.java)

    val adminApi: AdminApi
        get() = getRetrofit().create(AdminApi::class.java)

    val magazynierApi: MagazynierApi
        get() = getRetrofit().create(MagazynierApi::class.java)
}
