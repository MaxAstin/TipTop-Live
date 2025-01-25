package com.bunbeauty.tiptoplive.features.subscription.presentation

import com.bunbeauty.tiptoplive.common.presentation.Base
import com.bunbeauty.tiptoplive.features.subscription.view.SubscriptionItem

interface Subscription {

    data class State(
        val subscriptions: List<SubscriptionItem>
    ): Base.State {

        val selectedSubscription: SubscriptionItem?
            get() = subscriptions.find { it.isSelected }

    }

    sealed interface Action: Base.Action {
        data object CloseClicked: Action
        data class SubscriptionClick(val id: String): Action
        data object CheckoutClick: Action
    }

    sealed interface Event: Base.Event {
        data object HandleCloseClicked: Event
    }

}