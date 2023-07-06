package com.yazantarifi.kmm.sopy.useCases.types

import com.yazantarifi.kmm.sopy.SopyConstraintsException
import com.yazantarifi.kmm.sopy.useCases.SopifyErrorState
import com.yazantarifi.kmm.sopy.useCases.SopifyLoadingState
import com.yazantarifi.kmm.sopy.useCases.SopyScope
import com.yazantarifi.kmm.sopy.useCases.SopifySuccessState
import com.yazantarifi.kmm.sopy.listeners.SopyUseCaseListener
import com.yazantarifi.kmm.sopy.useCases.SopyUseCaseType
import com.yazantarifi.kmm.sopy.useCases.constraints.SopifyType
import io.ktor.client.HttpClient
import io.ktor.utils.io.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * This is the Most Important Part of the Mobile Applications Logic
 * From Here is the Start Point to Lead the App
 *
 * Mobile Applications Should depend on UseCases to Lead them and This UseCase
 * Control Kotlin Coroutines and State, Constraints
 *
 * 1. Constraints: A Validation Control on Input Parts if Valid or Not to Decide how to Execute the UseCase
 * More Info in : SopifyConstraint
 *
 * 2. Any Client Application Should Only Call execute, addHttpClient or clear Methods
 *
 * 3. State Management -> Supported States -> Success, Failed, Loading
 *
 * Example:
 * 1. UI - User Click on Button
 * 2. UI Send Action Event to ViewModel
 * 3. ViewModel will Validate that Action and Pick the Write UseCase
 * 4. UseCase Will Start Validating the Constraints
 * 5. Constraints Valid, Start Execution Now
 * 6. UseCase will check the Data if Exists in Local Database, Will return the State Success Directly
 * 7. If Not - Will Send Loading State and Send Api Request
 * 8. Map the Response and Send Failed or Success Json
 */
abstract class SopyUseCase<RequestValue, ResponseValue : Any>: CoroutineScope, SopyUseCaseType {

    private var httpClient: HttpClient? = null
    private var listener: SopyUseCaseListener? = null
    private val coroutineScope = SopyScope().getCoroutineDispatcher()
    override val coroutineContext: CoroutineContext get() = currentJob + Dispatchers.Default
    private val currentJob: Job by lazy {
        SupervisorJob()
    }

    /**
     * Clients Can Only Call this Method to Start UseCase Execution
     *
     * Params:
     * 1. RequestInput
     * 2. Results Listener
     */
    fun execute(requestValue: RequestValue, listener: SopyUseCaseListener) {
        this.listener = listener
        if (!isConstraintsSupported()) {
            executeUseCase(requestValue)
            return
        }

        val constraintValidationErrors = arrayListOf<Throwable>()
        getSupportedConstraints(requestValue).forEach {
            if (!it.isConstraintValid()) {
                constraintValidationErrors.add(it.getInvalidConstraintException())
            }
        }

        if (constraintValidationErrors.isNotEmpty()) {
            onSubmitExceptionState(SopyConstraintsException(constraintValidationErrors, "UseCases Constraints Not Match, Invalid"))
        } else {
            executeUseCase(requestValue)
        }
    }

    fun addHttpClient(httpClient: HttpClient) {
        this.httpClient = httpClient
    }

    private fun executeUseCase(requestValue: RequestValue) {
        launch(coroutineScope) {
            build(requestValue)
        }
    }

    /**
     * Return the Supported Constraints for this UseCase and Execute the UseCase
     * Will Collect Constraints and Start Validating Them
     */
    open fun getSupportedConstraints(requestValue: RequestValue): ArrayList<SopifyType> {
        return arrayListOf()
    }

    /**
     * A Boolean Value to return if the Constraints Logic Supported in this UseCase or Not
     * If Not UseCase.Build Function will start Directly without Any Constraints
     */
    abstract fun isConstraintsSupported(): Boolean

    /**
     * Build Your Own UseCase Logic by accepting RequestValue as a Input and Return Output in Registered Listener
     */
    abstract suspend fun build(requestValue: RequestValue)

    /**
     * Control Loading State By Sending Loading State and the Current Loading Behavior
     */
    protected fun onSubmitLoadingState(isLoading: Boolean) {
        launch(coroutineScope) {
            listener?.onStateUpdated(SopifyLoadingState(isLoading))
        }
    }

    /**
     * Submit Exception to ViewModel Layer, then ViewModel Gonna Decide
     * if the Exception can be Repeated or a Dead End Exception to Show Error Message to User
     */
    protected fun onSubmitExceptionState(throwable: Throwable) {
        launch(coroutineScope) {
            listener?.onStateUpdated(SopifyErrorState(throwable))
        }
    }

    /**
     * Submit Success State with the Payload Data
     * Success State Depends on Any Value, That's Mean Clients
     * Should always Know which Object To Cast to
     *
     * Add: this Function Will also Remove the Loading in The UI by sending False to Remove the Loading
     * Ref: onSubmitSuccessStateOnly
     */
    protected fun onSubmitSuccessState(response: ResponseValue) {
        launch(coroutineScope) {
            listener?.onStateUpdated(SopifySuccessState(response))
            listener?.onStateUpdated(SopifyLoadingState(false))
        }
    }

    /**
     * Submit Success State with the Payload Data
     * Success State Depends on Any Value, That's Mean Clients
     * Should always Know which Object To Cast to
     */
    protected fun onSubmitSuccessStateOnly(response: ResponseValue) {
        launch(coroutineScope) {
            listener?.onStateUpdated(SopifySuccessState(response))
        }
    }

    /**
     * Get the HttpClient Instance to Pass it to ApiManagers inside Each useCase
     */
    protected fun getHttpClientInstance(): HttpClient? {
        return this.httpClient
    }

    /**
     * Use This Method when You have to Cancel the Internal Requests remove the Listeners
     * AKA: Memory Leaks Instances
     */
    override fun clear() {
        this.cancel(CancellationException("This UseCase has Been Canceled"))
        this.listener = null
        this.httpClient = null
    }

    /**
     * Use This Method when You have to Cancel the Internal Requests remove the Listeners
     * AKA: Memory Leaks Instances
     * Provide Cancelation Message to CoroutineScope
     */
    override fun clear(message: String) {
        this.cancel(CancellationException(message))
        this.listener = null
    }

}