package com.bunbeauty.tiptoplive.features.subscription.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLivePrimaryButton
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme

@Composable
fun SuccessfullyPurchasedScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .background(FakeLiveTheme.colors.background)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier
                    .background(
                        color = FakeLiveTheme.colors.positive,
                        shape = CircleShape
                    )
                    .padding(32.dp)
                    .size(64.dp),
                painter = painterResource(R.drawable.ic_check),
                tint = FakeLiveTheme.colors.onSurface,
                contentDescription = "Success check"
            )
            Text(
                modifier = Modifier.padding(top = 64.dp),
                text = stringResource(R.string.subscription_success_title),
                style = FakeLiveTheme.typography.titleMedium,
                color = FakeLiveTheme.colors.onBackground,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = stringResource(R.string.subscription_success_description),
                style = FakeLiveTheme.typography.bodyMedium,
                color = FakeLiveTheme.colors.onBackground,
                textAlign = TextAlign.Center
            )
        }
        FakeLivePrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            text = stringResource(R.string.subscription_success_start_using),
            onClick = {
                navController.popBackStack()
            }
        )
    }
}

@LocalePreview
@Composable
private fun SuccessfullyPurchasedScreenPreview() {
    FakeLiveTheme {
        SuccessfullyPurchasedScreen(
            navController = rememberNavController()
        )
    }
}