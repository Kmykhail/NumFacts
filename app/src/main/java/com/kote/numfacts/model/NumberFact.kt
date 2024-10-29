package com.kote.numfacts.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "NumberFacts")
data class NumberFact(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @SerialName("number")
    val number: Long,

    @SerialName("text")
    val fact: String,

    val timestamp: Long = System.currentTimeMillis()
)