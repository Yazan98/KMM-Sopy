package com.yazantarifi.kmm.sopy.useCases

import kotlinx.coroutines.CoroutineDispatcher

expect class SopyScope() {
    fun getCoroutineDispatcher(): CoroutineDispatcher
}