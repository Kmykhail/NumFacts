package com.kote.numfacts.data

import com.kote.numfacts.data.db.NumberFactDao
import com.kote.numfacts.model.NumberFact
import kotlinx.coroutines.flow.Flow

interface DatabaseRepository {
    fun getAllFacts() : Flow<List<NumberFact>>

    fun getNumberFactById(id: Int) : Flow<NumberFact?>

    suspend fun addFact(numberFact: NumberFact)

    suspend fun updateFact(id: Int, fact: String)
}

class OfflineDatabaseRepository(
    private val numberFactDao: NumberFactDao
) : DatabaseRepository {
    override fun getAllFacts(): Flow<List<NumberFact>> = numberFactDao.getAllFacts()

    override fun getNumberFactById(id: Int) : Flow<NumberFact?> = numberFactDao.getNumberFactById(id)

    override suspend fun addFact(numberFact: NumberFact) = numberFactDao.addFact(numberFact)

    override suspend fun updateFact(id: Int, fact: String) = numberFactDao.updateFact(id, fact)
}