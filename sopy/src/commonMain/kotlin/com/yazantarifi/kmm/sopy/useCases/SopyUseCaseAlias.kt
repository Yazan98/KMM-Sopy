package com.yazantarifi.kmm.sopy.useCases

/**
 * Return a Instance of a Class Internal From Another Singelton Class
 * Ref: Hilt Dependency Injection in Android Apps by Providing Instances in Diff Scopes
 */
abstract class SopyUseCaseAlias<T> {

    private var internalInstance: T? = null

    fun addInstance(instance: T) {
        this.internalInstance = instance
    }

    fun getInstance(): T? {
        return internalInstance
    }

}
