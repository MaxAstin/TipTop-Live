package com.bunbeauty.tiptoplive.features.billing

import android.app.Activity
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.QueryProductDetailsParams
import com.android.billingclient.api.QueryPurchasesParams
import com.android.billingclient.api.queryProductDetails
import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.features.billing.mapper.inAppProductToProduct
import com.bunbeauty.tiptoplive.features.billing.mapper.subscriptionToProduct
import com.bunbeauty.tiptoplive.features.billing.mapper.toPurchaseResultError
import com.bunbeauty.tiptoplive.features.billing.model.Product
import com.bunbeauty.tiptoplive.features.billing.model.PurchaseData
import com.bunbeauty.tiptoplive.features.billing.model.PurchaseResult
import com.bunbeauty.tiptoplive.features.billing.model.PurchasedProduct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class BillingService @Inject constructor(
    private val billingClient: BillingClient,
    private val analyticsManager: AnalyticsManager,
) {

    private val mutex = Mutex()

    suspend fun getProducts(ids: List<String>): List<Product> {
        return try {
            val subscriptions = getProducts(
                type = BillingClient.ProductType.SUBS,
                ids = ids
            ).mapNotNull { productDetails ->
                productDetails.subscriptionToProduct(percent = 25)
            }
            val inAppProducts = getProducts(
                type = BillingClient.ProductType.INAPP,
                ids = ids
            ).mapNotNull { productDetails ->
                productDetails.inAppProductToProduct(percent = 65)
            }

            subscriptions + inAppProducts
        } catch (exception: Exception) {
            analyticsManager.trackError(exception)
            emptyList()
        }
    }

    suspend fun getPurchases(): List<PurchasedProduct> {
        return try {
            val isSuccessful = init()
            if (isSuccessful) {
                (queryPurchases(BillingClient.ProductType.SUBS) +
                    queryPurchases(BillingClient.ProductType.INAPP))
                    .filter { purchase ->
                        purchase.purchaseState == Purchase.PurchaseState.PURCHASED
                    }.mapNotNull { purchase ->
                        purchase.products.firstOrNull()?.let { product ->
                            PurchasedProduct(
                                id = product,
                                token = purchase.purchaseToken
                            )
                        }
                    }
            } else {
                emptyList()
            }
        } catch (exception: Exception) {
            analyticsManager.trackError(exception)
            emptyList()
        }
    }

    suspend fun launchBillingFlow(
        activity: Activity,
        purchaseData: PurchaseData
    ): Boolean {
        val product = getProducts(
            type = BillingClient.ProductType.SUBS,
            ids = listOf(purchaseData.productId)
        ).firstOrNull() ?: getProducts(
            type = BillingClient.ProductType.INAPP,
            ids = listOf(purchaseData.productId)
        ).firstOrNull()
        if (product == null) {
            analyticsManager.trackProductNotFound(productId = purchaseData.productId)
            return false
        }

        val productDetailsParams = listOf(
            BillingFlowParams.ProductDetailsParams.newBuilder()
                .setProductDetails(product)
                .apply {
                    if (purchaseData.offerToken != null) {
                        setOfferToken(purchaseData.offerToken)
                    }
                }
                .build()
        )
        val billingFlowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(productDetailsParams)
            .build()
        val result = billingClient.launchBillingFlow(activity, billingFlowParams)
        val isSuccessful = result.responseCode == BillingClient.BillingResponseCode.OK
        if (isSuccessful) {
            analyticsManager.trackStartBillingFlow(productId = purchaseData.productId)
        } else {
            analyticsManager.trackFailBillingFlow(productId = purchaseData.productId)
        }

        return isSuccessful
    }

    suspend fun acknowledgePurchase(purchasedProduct: PurchasedProduct): PurchaseResult {
        val acknowledgePurchaseParams = AcknowledgePurchaseParams.newBuilder()
            .setPurchaseToken(purchasedProduct.token)
            .build()

        return suspendCancellableCoroutine { continuation ->
            billingClient.acknowledgePurchase(acknowledgePurchaseParams) { billingResult ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    analyticsManager.trackAcknowledgeProduct(productId = purchasedProduct.id)
                    continuation.resume(PurchaseResult.Success(product = purchasedProduct))
                } else {
                    billingResult.toPurchaseResultError()?.let { error ->
                        analyticsManager.trackAcknowledgementFailed(
                            productId = purchasedProduct.id,
                            reason = error.reason
                        )
                        continuation.resume(error)
                    } ?: continuation.cancel()
                }
            }
        }
    }

    private suspend fun getProducts(
        type: String,
        ids: List<String>
    ): List<ProductDetails> {
        val isSuccessful = init()
        return if (isSuccessful) {
            getProductDetails(
                type = type,
                ids = ids,
            ).orEmpty()
        } else {
            emptyList()
        }
    }

    private suspend fun init(): Boolean {
        mutex.withLock {
            if (billingClient.isReady) {
                return true
            }

            return suspendCoroutine { continuation ->
                billingClient.startConnection(
                    object : BillingClientStateListener {
                        override fun onBillingSetupFinished(billingResult: BillingResult) {
                            val isSuccessful = billingResult.responseCode == BillingClient.BillingResponseCode.OK
                            if (isSuccessful) {
                                analyticsManager.trackBillingConnectionSuccess()
                            } else {
                                billingResult.toPurchaseResultError()
                                    ?.let { error ->
                                        analyticsManager.trackBillingConnectionFailed(
                                            state = error.reason
                                        )
                                    }
                            }

                            continuation.resume(isSuccessful)
                        }

                        override fun onBillingServiceDisconnected() {
                            analyticsManager.trackBillingDisconnection()
                        }
                    }
                )
            }
        }
    }

    private suspend fun getProductDetails(type: String, ids: List<String>): List<ProductDetails>? {
        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(
                ids.map { id ->
                    getProductDetailsParams(
                        id = id,
                        type = type,
                    )
                }
            )
            .build()

        return withContext(Dispatchers.IO) {
            val result = billingClient.queryProductDetails(params)
            if (result.billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                result.productDetailsList
            } else {
                null
            }
        }
    }

    private fun getProductDetailsParams(
        type: String,
        id: String,
    ): QueryProductDetailsParams.Product {
        return QueryProductDetailsParams.Product.newBuilder()
            .setProductId(id)
            .setProductType(type)
            .build()
    }

    private suspend fun queryPurchases(productType: String): List<Purchase> {
        val params = QueryPurchasesParams.newBuilder()
            .setProductType(productType)
            .build()

        return suspendCoroutine { continuation ->
            billingClient.queryPurchasesAsync(params) { billingResult, purchasesList ->
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    continuation.resume(purchasesList)
                } else {
                    continuation.resume(emptyList())
                }
            }
        }
    }

}