package com.yazantarifi.kmm.sopy.listeners

import com.yazantarifi.kmm.sopy.useCases.SopifyState

interface SopyUseCaseListener {

    fun onStateUpdated(newState: SopifyState)

}