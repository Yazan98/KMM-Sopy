package com.yazantarifi.kmm.sopy

import io.ktor.client.HttpClient

expect class SopyHttpBaseClient() {
    val httpClient: HttpClient
}