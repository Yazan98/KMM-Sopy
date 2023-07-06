package com.yazantarifi.kmm.sopy.api

import io.ktor.client.HttpClient
import io.ktor.client.statement.HttpResponseContainer
import io.ktor.client.statement.HttpResponsePipeline
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.KotlinxSerializationConverter
import io.ktor.serialization.suitableCharset
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.platformType
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

/**
 * This Class has the Base Code Implementation for All Api Requests in any App
 * This one Check on Response Status, HttpClient, and DeSerialize the Response,
 * Mainly No Direct Access to This Class by Clients
 *
 * If Clients Need to Use This Class Use
 * 1. SopyOneRequest
 * 2. SopyCrudRequest
 */
abstract class SopyRequestManager {

    protected var httpClient: HttpClient? = null
    companion object {
        private const val SUCCESS_START_CODE = 200
        private const val SUCCESS_END_CODE = 300
    }

    fun addHttpClient(httpClient: HttpClient?) {
        if (this.httpClient == null) {
            this.httpClient = httpClient
            addRequestPiplineInterceptor()
        }
    }

    private fun addRequestPiplineInterceptor() {
        val converter = getSerializable()
        httpClient?.responsePipeline?.intercept(HttpResponsePipeline.Transform) { (info, body) ->
            if (body !is ByteReadChannel) return@intercept

            val response = context.response
            val apiResponse = if (response.status.value in SUCCESS_START_CODE until SUCCESS_END_CODE) {
                SopyApplicationState.Success(
                    converter.deserialize(
                        context.request.headers.suitableCharset(),
                        info.ofInnerClassParameter(),
                        body
                    )
                )
            } else {
                SopyApplicationState.Error(responseCode = response.status.value)
            }

            proceedWith(HttpResponseContainer(info, apiResponse))
        }
    }

    open fun clear() {
        this.httpClient = null
    }

    private fun TypeInfo.ofInnerClassParameter(): TypeInfo {
        val typeProjection = kotlinType?.arguments?.get(0)
        val kType = typeProjection!!.type!!
        return TypeInfo(kType.classifier as KClass<*>, kType.platformType)
    }

    protected fun isSuccessResponse(responseCode: HttpStatusCode): Boolean {
        return responseCode == HttpStatusCode.Accepted || responseCode == HttpStatusCode.OK || responseCode == HttpStatusCode.Created
    }

    private fun getSerializable(): KotlinxSerializationConverter {
        return KotlinxSerializationConverter(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            allowSpecialFloatingPointValues = true
            isLenient = true
        })
    }

}