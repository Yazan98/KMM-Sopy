package com.yazantarifi.kmm.sopy

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.engine.darwin.DarwinLegacy
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpRequest
import io.ktor.client.request.header
import io.ktor.client.request.headers
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class SopyLegacyHttpBaseClient() {
    val httpClient: HttpClient = HttpClient(DarwinLegacy) {
        engine {
            configureRequest {
                setAllowsCellularAccess(true)
            }
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
            headers {
                append("Content-Type", "application/json")
                append("Accept", "application/json")
            }
        }

        developmentMode = SopyApplicationConfigurations.isDebugEnabled
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                ignoreUnknownKeys = true
                allowSpecialFloatingPointValues = true
                isLenient = true
            })
        }

        if (SopyApplicationConfigurations.isDebugEnabled) {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }

        install(DefaultRequest) {
            SopyApplicationConfigurations.requestsInterceptor?.getHeaders()?.let {
                for ((key, value) in it) {
                    header(key, value)
                }
            }
        }

        HttpResponseValidator {
            validateResponse { response ->
                SopyApplicationConfigurations.requestsListener?.onValidateRequest(response.request.url.toString(), response.status.value)
            }

            handleResponseExceptionWithRequest { cause: Throwable, request: HttpRequest ->
                if (SopyApplicationConfigurations.isDebugEnabled) {
                    println("[${SopyApplicationConfigurations.applicationLoggingKey}][ERROR] : ${cause.message}")
                    cause.printStackTrace()
                }

                SopyApplicationConfigurations.requestsListener?.onRequestFailed(cause, request.url.toString())
            }
        }
    }
}