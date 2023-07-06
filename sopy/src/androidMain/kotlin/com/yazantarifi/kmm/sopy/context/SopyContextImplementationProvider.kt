package com.yazantarifi.kmm.sopy.context

import android.content.Context

actual fun SopyContext.putInt(key: String, value: Int) {
    getSpEditor().putInt(key, value).apply()
}

actual fun SopyContext.getInt(key: String, default: Int): Int {
    return  getSp().getInt(key, default )
}

actual fun SopyContext.putString(key: String, value: String) {
    getSpEditor().putString(key, value).apply()
}

actual fun SopyContext.getString(key: String): String? {
    return  getSp().getString(key, null)
}

actual fun SopyContext.putBool(key: String, value: Boolean) {
    getSpEditor().putBoolean(key, value).apply()
}

actual fun SopyContext.getBool(key: String, default: Boolean): Boolean {
    return getSp().getBoolean(key, default)
}

private fun SopyContext.getSp() = getSharedPreferences(SopyStorageKeys.STORAGE_MAIN_KEY, Context.MODE_PRIVATE)

private fun SopyContext.getSpEditor() = getSp().edit()
