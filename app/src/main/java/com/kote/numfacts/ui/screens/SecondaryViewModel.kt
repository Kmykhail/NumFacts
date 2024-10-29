package com.kote.numfacts.ui.screens

import android.view.View
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kote.numfacts.data.OfflineDatabaseRepository
import com.kote.numfacts.model.NumberFact
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class SecondaryViewModel(
    savedStateHandle: SavedStateHandle,
    private val offlineDatabaseRepository: OfflineDatabaseRepository
): ViewModel() {
    private val factId: Int = checkNotNull(savedStateHandle[SecondaryScreenDestination.itemIdArg])
    val numberFact : StateFlow<NumberFact?> = offlineDatabaseRepository.getNumberFactById(factId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = null
    )
}