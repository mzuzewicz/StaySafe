package com.example.staysafe.maps.api


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {
    private const val BASE_URL = "https://maps.googleapis.com/"

    fun provideDirectionsApiService(): DirectionsApiService {
        // Define logging interceptor at class level if not already defined
         val loggingInterceptor = HttpLoggingInterceptor().apply {
            // Set logging level - BODY shows the most detailed logs including headers and body
            level =  HttpLoggingInterceptor.Level.BODY
        }

         val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .readTimeout(1L, TimeUnit.MINUTES)
            .callTimeout(1L, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .build()


        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DirectionsApiService::class.java)
    }


}