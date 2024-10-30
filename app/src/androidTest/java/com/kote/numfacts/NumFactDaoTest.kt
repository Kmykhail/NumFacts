package com.kote.numfacts

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.kote.numfacts.data.db.AppDataBase
import com.kote.numfacts.data.db.NumberFactDao
import com.kote.numfacts.model.NumberFact
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class NumFactDaoTest {
    private lateinit var numFactDao: NumberFactDao
    private lateinit var appDatabase: AppDataBase
    private val fact1 = NumberFact(1, "42", "Fact about number 42", 1234)
    private val fact2 = NumberFact(2, "777", "Fact about number 777", 5678)


    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDataBase::class.java)
            .allowMainThreadQueries()
            .build()
        numFactDao = appDatabase.numberFactDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        appDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun daoAddFact_toDB() = runBlocking {
        addOneFact()
        val allFacts = numFactDao.getAllFacts().first()
        assertEquals(allFacts[0], fact1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetFactById_fromDB() = runBlocking {
        addOneFact()
        val searchedFact = numFactDao.getNumberFactById(fact1.id)
        assertEquals(searchedFact.first(), fact1)
    }

    @Test
    @Throws(Exception::class)
    fun daoGetAllFacts_fromDB() = runBlocking {
        addTwoFacts()
        val allFacts = numFactDao.getAllFacts().first()

        // Ordered by timestamp
        assertEquals(allFacts[0], fact2)
        assertEquals(allFacts[1], fact1)
    }

    @Test
    @Throws
    fun datUpdateFact_DB() = runBlocking {
        addOneFact()

        val updatedMessage = "UPDATE, Fact about number 42"
        val updatedTimestamps = System.currentTimeMillis()
        numFactDao.updateFact(fact1.id, updatedMessage, updatedTimestamps)

        val allFacts = numFactDao.getAllFacts().first()
        assertEquals(allFacts[0], NumberFact(fact1.id, fact1.number, updatedMessage, updatedTimestamps))
    }

    private suspend fun addOneFact() {
        numFactDao.addFact(fact1)
    }

    private suspend fun addTwoFacts() = runBlocking {
        numFactDao.addFact(fact1)
        numFactDao.addFact(fact2)
    }
}