package com.kote.numfacts.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kote.numfacts.data.OfflineDatabaseRepository
import com.kote.numfacts.data.OnlineNetworkRepository
import com.kote.numfacts.model.NumberFact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class MainViewModel(
    private val onlineNetworkRepository: OnlineNetworkRepository,
    private val offlineDatabaseRepository: OfflineDatabaseRepository
): ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    private val historyRequestedFactsFlow: StateFlow<List<NumberFact>> = offlineDatabaseRepository.getAllFacts().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = emptyList()
    )

    init {
        viewModelScope.launch {
            historyRequestedFactsFlow.collect { collected ->
                _uiState.update { it.copy(historyRequestedFacts = collected) }
            }
        }
    }

    fun fetchNumberFact(userInput: String) {
        if (userInput.isNotEmpty() && userInput.isNotBlank()) {
            viewModelScope.launch {
                try {
                    val validResponse = onlineNetworkRepository.getNumberFact(userInput)
                    _uiState.update { it.copy(numFact = validResponse, requestState = RequestState.SUCCESS) }

                    if (_uiState.value.historyRequestedFacts.contains(validResponse)) {
                        offlineDatabaseRepository.updateFact(validResponse)
                    } else {
                        offlineDatabaseRepository.addFact(validResponse)
                    }
                } catch (e: IOException) {
                    _uiState.update { it.copy(requestState = RequestState.ERROR) }
                } catch (e: HttpException) {
                    _uiState.update { it.copy(requestState = RequestState.ERROR) }
                }
            }
        }
    }
}

enum class RequestState{
    SUCCESS, LOADING, ERROR
}

data class UiState(
    val userInput: String = "",
    val numFact: NumberFact? = null,
    val historyRequestedFacts: List<NumberFact> = emptyList(),
    val requestState: RequestState = RequestState.LOADING
)