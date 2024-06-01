package com.bunbeauty.fakelivestream.features.billing

import android.content.Context
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.PendingPurchasesParams
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BillingModule {

    @Singleton
    @Provides
    fun providesBillingClient(
        @ApplicationContext context: Context,
        purchasesListener: PurchasesListener
    ): BillingClient {
        val pendingPurchasesParams = PendingPurchasesParams.newBuilder()
            .enableOneTimeProducts()
            .build()

        return BillingClient.newBuilder(context)
            .enablePendingPurchases(pendingPurchasesParams)
            .setListener(purchasesListener)
            .build()
    }

}