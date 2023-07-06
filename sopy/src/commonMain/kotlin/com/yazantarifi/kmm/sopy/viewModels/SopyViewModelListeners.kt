package com.yazantarifi.kmm.sopy.viewModels

interface SopyViewModelListeners {

    fun onErrorScreenEventTriggered(exception: Throwable)

    fun onErrorMessageTriggered(message: String)

}