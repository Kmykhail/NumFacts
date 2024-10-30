package com.kote.numfacts.ui.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kote.numfacts.data.OfflineDatabaseRepository
import com.kote.numfacts.model.NumberFact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SecondaryViewModel(
    savedStateHandle: SavedStateHandle,
    private val offlineDatabaseRepository: OfflineDatabaseRepository
): ViewModel() {
    private val factId: Int = checkNotNull(savedStateHandle[SecondaryScreenDestination.itemIdArg])
    private val _numberFact = MutableStateFlow<NumberFact?>(null)
    val numberFact :StateFlow<NumberFact?> = _numberFact

    init {
        viewModelScope.launch {
            offlineDatabaseRepository.getNumberFactById(factId).collect {
                _numberFact.value = it
            }
        }
    }
}