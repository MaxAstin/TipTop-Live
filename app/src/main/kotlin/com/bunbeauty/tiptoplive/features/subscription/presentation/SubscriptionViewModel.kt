package com.bunbeauty.tiptoplive.features.subscription.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.features.billing.BillingService
import com.bunbeauty.tiptoplive.features.billing.PurchasesListener
import com.bunbeauty.tiptoplive.features.billing.model.PurchaseData
import com.bunbeauty.tiptoplive.features.subscription.mapper.toSubscriptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val analyticsManager: AnalyticsManager,
    private val billingService: BillingService,
    private val purchasesListener: PurchasesListener
) : BaseViewModel<Subscription.State, Subscription.Action, Subscription.Event>(
    initState = {
        Subscription.State(
            subscriptions = emptyList()
        )
    }
) {

    init {
        // TODO remove
        loadPurchases()
        loadSubscriptions()
        subscribeOnPurchaseFlow()
    }

    override fun onAction(action: Subscription.Action) {
        when (action) {
            Subscription.Action.CloseClicked -> {
                analyticsManager.trackPremiumQuite()
                sendEvent(Subscription.Event.NavigateBack)
            }

            is Subscription.Action.SubscriptionClick -> {
                setState {
                    copy(
                        subscriptions = subscriptions.map {
                            if (it.id == action.id) {
                                it.copy(isSelected = true)
                            } else {
                                it.copy(isSelected = false)
                            }
                        }
                    )
                }
            }

            Subscription.Action.CheckoutClick -> {
                currentState.selectedSubscription?.let { subscription ->
                    analyticsManager.trackCheckoutClick(productId = subscription.id)
                    sendEvent(
                        Subscription.Event.StartCheckout(
                            purchaseData = PurchaseData(
                                productId = subscription.id,
                                offerToken = subscription.offerToken
                            )
                        )
                    )
                }
            }
        }
    }

    private fun loadPurchases() {
        viewModelScope.launch {
            val purchase = billingService.getPurchases().firstOrNull()
            if (purchase != null) {
                sendEvent(
                    Subscription.Event.NavigateToPurchase(
                        subscriptionName = purchase.id
                    )
                )
            }
        }
    }

    private fun loadSubscriptions() {
        viewModelScope.launch {
            val subscriptions = billingService.getProducts(
                listOf("monthly", "lifetime")
            ).toSubscriptions()
            setState {
                copy(subscriptions = subscriptions)
            }
        }
    }

    private fun subscribeOnPurchaseFlow() {
        purchasesListener.purchasedProduct.onEach { product ->
            billingService.acknowledgePurchase(purchasedProduct = product)
            currentState.subscriptions.find { subscription ->
                subscription.id == product.id
            }?.let { purchasedSubscription ->
                sendEvent(
                    Subscription.Event.NavigateToPurchase(
                        subscriptionName = purchasedSubscription.name
                    )
                )
            }
        }.launchIn(viewModelScope)
    }

}