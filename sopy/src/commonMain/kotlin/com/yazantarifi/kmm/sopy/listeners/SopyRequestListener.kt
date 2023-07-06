package com.yazantarifi.kmm.sopy.listeners

interface SopyRequestListener {
    fun onRequestFailed(cause: Throwable, request: String)
    fun onValidateRequest(request: String, responseCode: Int)
}