package com.kote.numfacts

import android.app.Application
import com.kote.numfacts.data.AppContainer
import com.kote.numfacts.data.AppdataContainer

class NumFactsApplication: Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppdataContainer(this)
    }
}