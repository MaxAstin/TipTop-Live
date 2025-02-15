package com.bunbeauty.tiptoplive.features.billing

import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.features.billing.mapper.toPurchaseResultError
import com.bunbeauty.tiptoplive.features.billing.model.PurchaseResult
import com.bunbeauty.tiptoplive.features.billing.model.PurchasedProduct
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PurchaseResultListener @Inject constructor(
    private val analyticsManager: AnalyticsManager
) : PurchasesUpdatedListener {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val _purchaseResult = MutableSharedFlow<PurchaseResult>()
    val purchaseResult = _purchaseResult.asSharedFlow()

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        val purchase = purchases?.firstOrNull() ?: return
        val productId = purchase.products.firstOrNull() ?: return

        if (result.responseCode == BillingClient.BillingResponseCode.OK) {
            val purchasedProduct = PurchasedProduct(
                id = productId,
                token = purchase.purchaseToken
            )
            analyticsManager.trackPurchaseProduct(productId = productId)
            scope.launch {
                _purchaseResult.emit(
                    PurchaseResult.Success(product = purchasedProduct)
                )
            }
        } else {
            result.toPurchaseResultError()?.let { error ->
                analyticsManager.trackPurchaseFailed(
                    productId = productId,
                    reason = error.reason
                )
                scope.launch {
                    _purchaseResult.emit(error)
                }
            }
        }
    }

}