package com.kote.numfacts.ui.screens

import android.util.Log
import androidx.compose.ui.platform.LocalDensity
import androidx.core.text.isDigitsOnly
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
                _uiState.value.historyRequestedFacts.forEach {
                    println("NumFact number: ${it.number}")
                }
            }
        }
    }

    fun fetchNumberFact(userInput: String, needCheck: Boolean = true) {
        if (!needCheck || userInput.isNotEmpty() && userInput.isDigitsOnly()) {
            viewModelScope.launch {
                try {
                    val validNumberFact = onlineNetworkRepository.getNumberFact(userInput)
                    _uiState.update { it.copy(numFact = validNumberFact, requestState = RequestState.SUCCESS) }

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
                    _uiState.update { it.copy(requestState = RequestState.ERROR) }
                } catch (e: HttpException) {
                    _uiState.update { it.copy(requestState = RequestState.ERROR) }
                }
            }
        }
    }

    fun fetchRandomNumberFact() {
        val seed = System.currentTimeMillis()
        val random = Random(seed)
        fetchNumberFact(random.nextInt().toString(), false)
    }
}

enum class RequestState{
    SUCCESS, LOADING, ERROR
}

data class UiState(
//    var userInput: String = "",
    val numFact: NumberFact? = null,
    val historyRequestedFacts: List<NumberFact> = emptyList(),
    val requestState: RequestState = RequestState.LOADING
)