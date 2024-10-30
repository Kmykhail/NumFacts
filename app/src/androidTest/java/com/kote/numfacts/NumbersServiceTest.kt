package com.kote.numfacts

import com.kote.numfacts.data.OnlineNetworkRepository
import com.kote.numfacts.data.network.NumbersApiService
import com.kote.numfacts.model.NumberFact
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

object FakeDataSource {
    val numberFact = NumberFact(
        number = "42",
        fact = "Fact about number 42",
        timestamp = 12345
    )
}

class FakeNumbersApiService : NumbersApiService {
    override suspend fun getNumberFact(number: String): NumberFact {
        return FakeDataSource.numberFact
    }
}

class FakeRepositoryTest {
    @Test
    fun networkRepository_getNumberFact() =
        runTest {
            val repository = OnlineNetworkRepository(
                numberService = FakeNumbersApiService()
            )

            println("WTF ${repository.getNumberFact("42")}, ${FakeDataSource.numberFact}")
            assertEquals(FakeDataSource.numberFact, repository.getNumberFact("42"))
        }
}