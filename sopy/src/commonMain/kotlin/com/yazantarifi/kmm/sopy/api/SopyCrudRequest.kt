package com.yazantarifi.kmm.sopy.api

/**
 * This Class Used when You Have only One Crud Operation
 * for Example if you have a User Api and You need to do more Than One Operation
 * You can Use This Class
 *
 * 1. Create User - onPostRequest
 * 2. Get User - onGetRequest
 * 3. Update User - onUpdateRequest
 * 4. Delete User - onDeleteRequest
 */
abstract class SopyCrudRequest<ResponseValue>: SopyRequestManager() {

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

    abstract fun onGetRequest(headers: List<Pair<String, String>>, params: HashMap<String, String>)

    abstract fun onUpdateRequest(headers: List<Pair<String, String>>, params: HashMap<String, String>, requestBody: Any)

    abstract fun onDeleteRequest(headers: List<Pair<String, String>>, params: HashMap<String, String>)

    abstract fun onPostRequest(headers: List<Pair<String, String>>, params: HashMap<String, String>, requestBody: Any)

}