package com.example.currencyapp.ui.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currencyapp.domain.pojo.SymbolsResponse
import com.example.currencyapp.domain.repository.MainRepository
import com.example.currencyapp.utils.Resource
import com.example.currencyapp.utils.ResultState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repo: MainRepository) : ViewModel() {

    private val _symbolsStateFlow: MutableStateFlow<ResultState<SymbolsResponse>> =
        MutableStateFlow(ResultState.Loading)

    val symbolsStateFlow: StateFlow<ResultState<SymbolsResponse>> = _symbolsStateFlow

    private var job: Job? = null

    fun getSymbols(accessKey: String) {
        repo.getSymbols(accessKey).onEach {
            when (it) {
                is Resource.Loading -> {
                    _symbolsStateFlow.emit(ResultState.Loading)
                }
                is Resource.Fail -> {
                    _symbolsStateFlow.emit(ResultState.Fail(it.message))
                }
                is Resource.Success -> {
                    _symbolsStateFlow.emit(ResultState.Success(it.data))
                }
            }
        }.launchIn(viewModelScope)
    }

    fun convert(from: String, to: String, amount: Double) {
        job?.cancel()
        job = viewModelScope.launch {
            delay(500L)
            repo.convert(from, to, amount)

        }
    }


}