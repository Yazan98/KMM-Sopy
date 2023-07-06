package com.yazantarifi.kmm.sopy.listeners

/**
 * This Class Used when You Have Logic to Implement when Response returned or Request Failed
 * Such as Sending Non-Fatal in Failed Apis or Report them, or Show Error Screen
 */
interface SopyRequestListener {
    fun onRequestFailed(cause: Throwable, request: String)
    fun onValidateRequest(request: String, responseCode: Int)
}