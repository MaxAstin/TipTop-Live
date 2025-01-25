package com.bunbeauty.tiptoplive.features.subscription.presentation

import com.bunbeauty.tiptoplive.common.analytics.AnalyticsManager
import com.bunbeauty.tiptoplive.common.presentation.BaseViewModel
import com.bunbeauty.tiptoplive.features.subscription.view.SubscriptionItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SubscriptionViewModel @Inject constructor(
    private val analyticsManager: AnalyticsManager
) : BaseViewModel<Subscription.State, Subscription.Action, Subscription.Event>(
        initState = {
            Subscription.State(
                subscriptions = listOf(
                    SubscriptionItem(
                        id = "1",
                        title = "Weekly",
                        price = "$2,99/week",
                        oldPrice = "$10,99/month",
                        label = "Save 70%",
                        isSelected = true
                    ),
                    SubscriptionItem(
                        id = "2",
                        title = "Lifetime",
                        price = "$6,99",
                        oldPrice = "$20,99/month",
                        label = "Pay once and use forever",
                        isSelected = false
                    )
                )
            )
        }
    ) {

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