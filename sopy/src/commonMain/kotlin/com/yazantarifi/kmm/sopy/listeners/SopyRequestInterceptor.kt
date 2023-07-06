package com.yazantarifi.kmm.sopy.listeners

/**
 * This Class Used when You have A Common Headers that Passed in Each Request
 * and You Need to Insert them Once, This Class is Useful and Should be Registered at: SopyApplicationConfigurations
 */
abstract class SopyRequestInterceptor {

    /**
     * Return the Common Headers in All Api Requests Only
     */
    abstract fun getHeaders(): HashMap<String, String>
}
