package com.yazantarifi.kmm.sopy.listeners

abstract class SopyRequestInterceptor {
    abstract fun getHeaders(): HashMap<String, String>
}
