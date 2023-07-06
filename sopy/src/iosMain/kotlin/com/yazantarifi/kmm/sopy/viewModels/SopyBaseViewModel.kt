package com.yazantarifi.kmm.sopy.viewModels

import com.yazantarifi.kmm.sopy.useCases.SopyScope
import com.yazantarifi.kmm.sopy.useCases.SopifyState
import com.yazantarifi.kmm.sopy.useCases.SopyUseCaseType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

actual abstract class SopyBaseViewModel<Action, StateType> {

    actual var state: StateType? = null
    actual val coroutineDispatch: CoroutineDispatcher = SopyScope().getCoroutineDispatcher()
    actual val scope: CoroutineScope = object: CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = Job() + coroutineDispatch
    }

    actual fun execute(action: Action) {
        scope.launch(coroutineDispatch) {
            onNewActionTriggered(action)
        }
    }

    actual abstract fun initViewModelState()
    actual abstract fun getInitialState(): SopifyState
    actual abstract suspend fun onNewActionTriggered(action: Action)
    actual fun getSupportedUseCases(): ArrayList<SopyUseCaseType> {
        return arrayListOf()
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
