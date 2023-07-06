package com.yazantarifi.kmm.sopy.context

expect fun SopyContext.putInt(key: String, value: Int)

expect fun SopyContext.getInt(key: String, default: Int): Int

expect fun SopyContext.putString(key: String, value: String)

expect fun SopyContext.getString(key: String) : String?

expect fun SopyContext.putBool(key: String, value: Boolean)

expect fun SopyContext.getBool(key: String, default: Boolean): Boolean