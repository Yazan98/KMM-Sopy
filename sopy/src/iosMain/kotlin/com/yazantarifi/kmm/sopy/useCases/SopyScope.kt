package com.yazantarifi.kmm.sopy.useCases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

actual class SopyScope {
    actual fun getCoroutineDispatcher(): CoroutineDispatcher = Dispatchers.Default
}