package com.yazantarifi.kmm.sopy

import com.yazantarifi.kmm.sopy.listeners.SopyRequestInterceptor
import com.yazantarifi.kmm.sopy.listeners.SopyRequestListener
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object SopyApplicationConfigurations {

    var applicationLanguage: String = "en"
    var deviceLanguage: String = "en"
    var appVersion: String = ""
    var appPlatform: String = ""
    var isDebugEnabled: Boolean = false
    var applicationLoggingKey: String = "[Sopy]"
    var requestsListener: SopyRequestListener? = null
    var requestsInterceptor: SopyRequestInterceptor? = null

}