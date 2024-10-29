package com.kote.numfacts.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kote.numfacts.model.NumberFact
import kotlinx.coroutines.flow.Flow

@Dao
interface NumberFactDao {
    @Query("SELECT * FROM NumberFacts ORDER BY timestamp DESC")
    fun getAllFacts() : Flow<List<NumberFact>>

    @Query("SELECT * FROM NumberFacts WHERE id = :id")
    fun getNumberFactById(id: Int) : Flow<NumberFact?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFact(numberFact: NumberFact)

    @Query("UPDATE NumberFacts SET fact = :fact WHERE id = :id")
    suspend fun updateFact(id: Int, fact: String)
}