package com.yazantarifi.kmm.sopy

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform