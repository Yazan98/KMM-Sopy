package com.yazantarifi.kmm.sopy.viewModels

import com.yazantarifi.kmm.sopy.useCases.SopifyState
import com.yazantarifi.kmm.sopy.useCases.SopyUseCaseType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope

expect abstract class SopyBaseViewModel<Action, StateType>() {

    var state: StateType?
    val coroutineDispatch: CoroutineDispatcher
    val scope: CoroutineScope

    fun execute(action: Action)

    abstract fun initViewModelState()

    abstract fun getInitialState(): SopifyState

    abstract suspend fun onNewActionTriggered(action: Action)

    fun getSupportedUseCases(): ArrayList<SopyUseCaseType>

    fun clear()

    protected fun getCurrentState(): StateType?

}