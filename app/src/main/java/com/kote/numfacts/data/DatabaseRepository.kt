package com.kote.numfacts.data

import com.kote.numfacts.data.db.NumberFactDao
import com.kote.numfacts.model.NumberFact
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    fun getAllFacts() : Flow<List<NumberFact>>

    suspend fun addFact(numberFact: NumberFact)

    suspend fun updateFact(numberFact: NumberFact)
}

class OfflineDatabaseRepository(
    private val numberFactDao: NumberFactDao
) : DatabaseRepository {
    override fun getAllFacts(): Flow<List<NumberFact>> = numberFactDao.getAllFacts()

    override suspend fun addFact(numberFact: NumberFact) = numberFactDao.addFact(numberFact)

    override suspend fun updateFact(numberFact: NumberFact) = numberFactDao.updateFact(numberFact)
}