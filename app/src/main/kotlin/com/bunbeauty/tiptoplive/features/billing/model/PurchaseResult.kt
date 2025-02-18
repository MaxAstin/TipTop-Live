package com.bunbeauty.tiptoplive.features.billing.model

sealed interface PurchaseResult {

    data class Success(val product: PurchasedProduct): PurchaseResult
    data class Error(val reason: String): PurchaseResult

    fun onSuccess(block: (Success) -> Unit): PurchaseResult {
        when (this) {
            is Success -> block(this)
            is Error -> Unit
        }

        return this
    }

    fun onError(block: (Error) -> Unit): PurchaseResult {
        when (this) {
            is Success -> Unit
            is Error -> block(this)
        }

        return this
    }
}