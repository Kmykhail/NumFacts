package com.kote.numfacts.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NumberFacts")
data class NumberFact(
    @PrimaryKey val number: Int,
    val fact: String
)