package com.kote.numfacts.data

import android.content.Context
import androidx.room.Database
import com.kote.numfacts.data.db.AppDataBase

interface AppContainer {
    val onlineNetworkRepository: OnlineNetworkRepository
    val offlineDatabaseRepository: OfflineDatabaseRepository
}

class AppdataContainer(private val context: Context): AppContainer {
    override val offlineDatabaseRepository: OfflineDatabaseRepository by lazy {
        OfflineDatabaseRepository(AppDataBase.getDataBase(context).numberFactDao())
    }

    override val onlineNetworkRepository: OnlineNetworkRepository by lazy {
        OnlineNetworkRepository()
    }
}