package com.yazantarifi.kmm.sopy.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yazantarifi.kmm.sopy.useCases.SopyScope
import com.yazantarifi.kmm.sopy.useCases.SopifyState
import com.yazantarifi.kmm.sopy.useCases.SopyUseCaseType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

actual abstract class SopyBaseViewModel<Action, StateType>: ViewModel() {

    actual var state: StateType? = null
    actual val coroutineDispatch: CoroutineDispatcher = SopyScope().getCoroutineDispatcher()
    actual val scope: CoroutineScope = viewModelScope

    actual fun execute(action: Action) {
        scope.launch(coroutineDispatch) {
            onNewActionTriggered(action)
        }
    }

    actual abstract fun initViewModelState()
    actual abstract fun getInitialState(): SopifyState
    actual abstract suspend fun onNewActionTriggered(action: Action)
    actual open fun getSupportedUseCases(): ArrayList<SopyUseCaseType> {
        return arrayListOf()
    }

    override fun onCleared() {
        super.onCleared()
        clear()
    }

    actual fun clear() {
        getSupportedUseCases().forEach {
            it.clear()
        }
    }

    protected actual fun getCurrentState(): StateType? {
        return this.state
    }

}