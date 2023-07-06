package com.yazantarifi.kmm.sopy.context

import platform.Foundation.NSUserDefaults

actual fun SopyContext.putInt(key: String, value: Int) {
    NSUserDefaults.standardUserDefaults.setInteger(value.toLong(), key)
}

actual fun SopyContext.getInt(key: String, default: Int): Int {
    return NSUserDefaults.standardUserDefaults.integerForKey(key).toInt()
}

actual fun SopyContext.putString(key: String, value: String) {
    NSUserDefaults.standardUserDefaults.setObject(value, key)
}

actual fun SopyContext.getString(key: String): String? {
    return NSUserDefaults.standardUserDefaults.stringForKey(key)
}

actual fun SopyContext.putBool(key: String, value: Boolean) {
    NSUserDefaults.standardUserDefaults.setBool(value, key)
}

actual fun SopyContext.getBool(key: String, default: Boolean): Boolean {
    return NSUserDefaults.standardUserDefaults.boolForKey(key)
}
