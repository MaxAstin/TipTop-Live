package com.bunbeauty.tiptoplive.features.billing.domain

import com.bunbeauty.tiptoplive.features.billing.BillingService
import javax.inject.Inject

class IsPremiumAvailableUseCase @Inject constructor(
    private val billingService: BillingService
) {

    suspend operator fun invoke(): Boolean {
        return billingService.getPurchases().isNotEmpty()
    }

}