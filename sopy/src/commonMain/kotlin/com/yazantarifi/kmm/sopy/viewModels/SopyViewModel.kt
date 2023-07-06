package com.yazantarifi.kmm.sopy.viewModels

import com.yazantarifi.kmm.sopy.useCases.SopifyEmptyState
import com.yazantarifi.kmm.sopy.useCases.SopifyState

abstract class SopyViewModel<Action>: SopyBaseViewModel<Action, Unit>() {

    private var errorListener: SopyViewModelListeners? = null
    init {
        initViewModelState()
    }

    fun onAcceptExceptionState(exception: Throwable) {
        this.errorListener?.onErrorScreenEventTriggered(exception)
    }

    fun onAcceptErrorMessage(message: String) {
        this.errorListener?.onErrorMessageTriggered(message)
    }

    fun onAttachListenerInstance(errorListener: SopyViewModelListeners) {
        this.errorListener = errorListener
    }

    final override fun initViewModelState() = Unit
    override fun getInitialState(): SopifyState {
        return SopifyEmptyState
    }

}