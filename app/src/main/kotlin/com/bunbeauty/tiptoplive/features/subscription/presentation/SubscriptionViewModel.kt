package com.bunbeauty.tiptoplive.features.subscription.presentation

import androidx.lifecycle.viewModelScope
import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.features.billing.BillingService
import com.bunbeauty.tiptoplive.features.subscription.mapper.toSubscriptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val analyticsManager: AnalyticsManager,
    private val billingService: BillingService,
) : BaseViewModel<Subscription.State, Subscription.Action, Subscription.Event>(
    initState = {
        Subscription.State(
            subscriptions = emptyList()
        )
    }
) {

    init {
        viewModelScope.launch {
            try {
                val subscriptions = billingService.getProducts(
                    listOf("monthly", "lifetime")
                ).toSubscriptions()
                setState {
                    copy(subscriptions = subscriptions)
                }
            } catch (e: Exception) {
                // TODO handle error
            }
        }
    }

    override fun onAction(action: Subscription.Action) {
        when (action) {
            Subscription.Action.CloseClicked -> {
                analyticsManager.trackPremiumQuite()
                sendEvent(Subscription.Event.HandleCloseClicked)
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
                }

                // TODO handle checkout click
            }
        }
    }

}