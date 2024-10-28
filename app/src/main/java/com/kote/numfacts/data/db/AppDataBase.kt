package com.kote.numfacts.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.kote.numfacts.model.NumberFact

@Database(entities = [NumberFact::class], version = 1, exportSchema = false)
abstract class AppDataBase: RoomDatabase() {
    abstract fun numberFactDao(): NumberFactDao

    companion object {
        private var INSTANCE: AppDataBase? = null

        fun getDataBase(context: Context) : AppDataBase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(context, AppDataBase::class.java, "num_fact_database")
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}