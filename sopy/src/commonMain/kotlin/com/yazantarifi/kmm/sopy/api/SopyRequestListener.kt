package com.yazantarifi.kmm.sopy.api

interface SopyRequestListener<ResponseValue> {

    fun onSuccess(responseValue: ResponseValue)

    fun onError(error: Throwable)

}
