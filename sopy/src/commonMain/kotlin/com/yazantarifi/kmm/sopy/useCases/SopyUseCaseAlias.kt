package com.yazantarifi.kmm.sopy.useCases

abstract class SopyUseCaseAlias<T> {

    private var internalInstance: T? = null

    fun addInstance(instance: T) {
        this.internalInstance = instance
    }

    fun getInstance(): T? {
        return internalInstance
    }

}
