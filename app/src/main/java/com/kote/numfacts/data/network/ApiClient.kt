package com.kote.numfacts.data.network

import retrofit2.Retrofit

object ApiClient {
    private val baseUrl = "http://numbersapi.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .build()

    val numberService: NumbersApiService by lazy { retrofit.create(NumbersApiService::class.java) }
}