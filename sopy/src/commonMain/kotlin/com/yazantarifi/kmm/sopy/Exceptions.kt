package com.yazantarifi.kmm.sopy

class SopyUnknownException constructor(cause: Throwable): Throwable(cause)
open class SopyException constructor(override val message: String?): Throwable(message)

class SopyNoInternetException: SopyException("No Internet Connection")

class SopyEmptyStringException constructor(
    private val key: String
): SopyException("String Empty : $key")

class SopyConstraintsException constructor(
    val exceptions: ArrayList<Throwable>,
    override val message: String
): SopyException(message)
