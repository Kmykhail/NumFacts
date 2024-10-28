package com.kote.numfacts.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kote.numfacts.NumFactsApplication

class AppViewModelProvider {
    val FACTORY = viewModelFactory {
        initializer {
            MainViewModel(
                numFactApplication().container.onlineNetworkRepository,
                numFactApplication().container.offlineDatabaseRepository
            )
        }
    }
}

fun CreationExtras.numFactApplication() : NumFactsApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NumFactsApplication)