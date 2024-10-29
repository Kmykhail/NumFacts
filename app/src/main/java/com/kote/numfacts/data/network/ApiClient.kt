package com.kote.numfacts.data.network

import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory

object ApiClient {
    private val baseUrl = "http://numbersapi.com/"

    private val json = Json{
        ignoreUnknownKeys = true
        isLenient = true
    }

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType())) // Add custom converter
        .baseUrl(baseUrl)
        .build()

    val numberService: NumbersApiService by lazy { retrofit.create(NumbersApiService::class.java) }
}