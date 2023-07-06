package com.yazantarifi.kmm.sopy.api

/**
 * This Class Used when You Have Only one Request Per Screen or Feature
 * For Example on Auth You have only one Refresh Token Request in this case
 * You will implement it alone
 */
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

    /**
     * Return the Request Url or BaseUrl
     */
    protected abstract fun getRequestUrl(): String

    /**
     * Execute the Request by Headers, Params, and Request Body Params
     * RequestBody can be a Body Payload or anything you need to Pass to Request
     */
    abstract suspend fun executeRequest(
        requestBody: RequestBody,
        headers: List<Pair<String, String>>,
        params: HashMap<String, String>
    )

}