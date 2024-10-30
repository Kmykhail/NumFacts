package com.kote.numfacts.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kote.numfacts.data.OfflineDatabaseRepository
import com.kote.numfacts.data.OnlineNetworkRepository
import com.kote.numfacts.model.NumberFact
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import kotlin.random.Random

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

    fun fetchNumberFact(userInput: String, needCheck: Boolean = true) {
        if (!needCheck || userInput.isNotEmpty() && isValidNumber(userInput)) {
            viewModelScope.launch {
                try {
                    val validNumberFact = onlineNetworkRepository.getNumberFact(userInput)
//                    _uiState.update { it.copy(numFact = validNumberFact) }

                    _uiState.value.run {
                        historyRequestedFacts.find { it.number == validNumberFact.number}
                            ?.let {
                                if (it.fact != validNumberFact.fact) {
                                    offlineDatabaseRepository.updateFact(it.id, validNumberFact.fact)
                                }
                            }
                            ?: offlineDatabaseRepository.addFact(validNumberFact)
                    }
                } catch (e: IOException) {
                    _uiState.update { it.copy(toastMessage = "Please check the internet connection") }
                } catch (e: HttpException) {
                    _uiState.update { it.copy(toastMessage = "Oops, something went wrong") }
                }
            }
        }
    }

    fun fetchRandomNumberFact() {
        val seed = System.currentTimeMillis()
        val random = Random(seed)
        fetchNumberFact(random.nextInt().toString(), false)
    }

    private fun isValidNumber(input: String): Boolean {
        val regex = Regex("^-?\\d+$")
        return regex.matches(input)
    }

    fun resetToastMessage() {
        _uiState.update { it.copy(toastMessage = null) }
    }
}

data class UiState(
//    val numFact: NumberFact? = null,
    val historyRequestedFacts: List<NumberFact> = emptyList(),
    val toastMessage : String? = null
)