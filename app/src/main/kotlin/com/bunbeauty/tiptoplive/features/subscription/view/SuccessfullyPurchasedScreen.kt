package com.bunbeauty.tiptoplive.features.subscription.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme

@Composable
fun SuccessfullyPurchasedScreen(
    navController: NavHostController,
    subscriptionName: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FakeLiveTheme.colors.background)
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(24.dp)
                .clickableWithoutIndication {
                    navController.popBackStack()
                },
            painter = painterResource(R.drawable.ic_close),
            tint = FakeLiveTheme.colors.onSurface,
            contentDescription = "Top icon"
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "You successfully purchased $subscriptionName",
        )
    }
}