package com.yazantarifi.kmm.sopy.useCases

interface SopifyState

data class SopifySuccessState(val payload: Any): SopifyState

data class SopifyErrorState(val exception: Throwable): SopifyState

data class SopifyLoadingState(val isLoading: Boolean): SopifyState

object SopifyEmptyState: SopifyState
