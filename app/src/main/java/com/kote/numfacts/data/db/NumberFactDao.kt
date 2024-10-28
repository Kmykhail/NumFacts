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
    @Query("SELECT * FROM NumberFacts ORDER BY number ASC")
    fun getAllFacts() : Flow<List<NumberFact>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFact(numberFact: NumberFact)

    @Update
    suspend fun updateFact(numberFact: NumberFact)
}