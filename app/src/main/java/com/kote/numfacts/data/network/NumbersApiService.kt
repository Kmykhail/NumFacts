package com.kote.numfacts.data.network

import com.kote.numfacts.model.NumberFact
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NumbersApiService {
    @GET("{number}?json")
    suspend fun getNumberFact(@Path("number") number: String) : NumberFact
}