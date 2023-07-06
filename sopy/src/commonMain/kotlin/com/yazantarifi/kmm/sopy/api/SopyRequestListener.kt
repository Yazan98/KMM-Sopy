package com.yazantarifi.kmm.sopy.api

/**
 * This Callback is the Main Callback Between Api Requests and UseCases
 * This Interface Will Deliver the Api Response from Api to UseCase
 * and UseCases will Continue
 */
interface SopyRequestListener<ResponseValue> {

    fun onSuccess(responseValue: ResponseValue)

    fun onError(error: Throwable)

}
