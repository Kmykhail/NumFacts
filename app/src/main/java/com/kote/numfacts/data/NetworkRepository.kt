package com.kote.numfacts.data

import com.kote.numfacts.data.network.ApiClient
import com.kote.numfacts.model.NumberFact
import com.kote.numfacts.data.network.NumbersApiService
import retrofit2.http.Path

interface NetworkRepository {
    suspend fun getNumberFact(@Path("number") number: String) : NumberFact
}

class OnlineNetworkRepository(
    private val numberService: NumbersApiService = ApiClient.numberService
) : NetworkRepository {
    override suspend fun getNumberFact(number: String): NumberFact = numberService.getNumberFact(number)
}