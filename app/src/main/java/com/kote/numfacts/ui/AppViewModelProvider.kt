package com.kote.numfacts.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kote.numfacts.NumFactsApplication
import com.kote.numfacts.ui.screens.MainViewModel
import com.kote.numfacts.ui.screens.SecondaryViewModel

object AppViewModelProvider {
    val FACTORY = viewModelFactory {
        initializer {
            MainViewModel(
                numFactApplication().container.onlineNetworkRepository,
                numFactApplication().container.offlineDatabaseRepository
            )
        }

        initializer {
            SecondaryViewModel(
                this.createSavedStateHandle(),
                numFactApplication().container.offlineDatabaseRepository
            )
        }
    }
}

fun CreationExtras.numFactApplication() : NumFactsApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as NumFactsApplication)