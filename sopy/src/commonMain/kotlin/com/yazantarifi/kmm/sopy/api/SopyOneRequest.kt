package com.yazantarifi.kmm.sopy.api

abstract class SopyOneRequest<RequestBody, ResponseValue>: SopyRequestManager() {

    protected var requestListener: SopyRequestListener<ResponseValue>? = null

    fun addRequestListener(requestListener: SopyRequestListener<ResponseValue>) {
        this.requestListener = requestListener
    }

    fun isRequestListenerAttachNeeded(): Boolean {
        return this.requestListener == null
    }

    override fun clear() {
        super.clear()
        requestListener = null
    }

    protected abstract fun getRequestUrl(): String

    abstract suspend fun executeRequest(requestBody: RequestBody, headers: List<Pair<String, String>>)

}