package com.bunbeauty.tiptoplive.features.subscription.view

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.navigation.NavigationRote
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLivePrimaryButton
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.bold
import com.bunbeauty.tiptoplive.features.billing.model.PurchaseData
import com.bunbeauty.tiptoplive.features.subscription.presentation.Subscription
import com.bunbeauty.tiptoplive.features.subscription.presentation.SubscriptionViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

private val blurredBackground: Color
    @Composable
    get() = FakeLiveTheme.colors.background.copy(alpha = 0.7f)

data class SubscriptionItem(
    val id: String,
    val offerToken: String?,
    val name: String,
    val currentPrice: String,
    val previousPrice: String?,
    val discountPercent: String?,
    val isLifetime: Boolean,
    val isSelected: Boolean,
)

@Composable
fun SubscriptionScreen(
    navController: NavHostController,
    startCheckout: (PurchaseData) -> Unit
) {
    val viewModel: SubscriptionViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = remember {
        { action: Subscription.Action ->
            viewModel.onAction(action)
        }
    }

    BackHandler {
        onAction(Subscription.Action.CloseClicked)
    }

    LaunchedEffect(Unit) {
        viewModel.event.onEach { event ->
            when (event) {
                Subscription.Event.NavigateBack -> {
                    navController.popBackStack()
                }

                is Subscription.Event.StartCheckout -> {
                    startCheckout(event.purchaseData)
                }

                is Subscription.Event.NavigateToPurchase -> {
                    navController.navigate(
                        NavigationRote.SuccessfullyPurchased(subscriptionName = event.subscriptionName)
                    )
                }
            }
        }.launchIn(this)
    }

    SubscriptionContent(
        state = state,
        onAction = onAction
    )
}

@Composable
private fun SubscriptionContent(
    state: Subscription.State,
    onAction: (Subscription.Action) -> Unit
) {
    Box {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(R.drawable.img_social_networks),
            contentScale = ContentScale.Crop,
            contentDescription = "Background"
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(FakeLiveTheme.colors.background.copy(alpha = 0.5f))
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Image(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(blurredBackground),
                painter = painterResource(R.drawable.img_logo_without_bg),
                contentDescription = "Logo"
            )
            Text(
                text = stringResource(R.string.subscription_unlimited_access),
                color = FakeLiveTheme.colors.onBackground,
                style = FakeLiveTheme.typography.titleLarge.bold,
                textAlign = TextAlign.Center
            )

            val shape = RoundedCornerShape(16.dp)
            Column(
                modifier = Modifier
                    .clip(shape)
                    .background(blurredBackground)
                    .padding(16.dp),
                verticalArrangement = spacedBy(8.dp)
            ) {
                FeatureText(
                    text = stringResource(R.string.subscription_unlimited_time)
                )
                FeatureText(
                    text = stringResource(R.string.subscription_comments_and_reactions)
                )
                FeatureText(
                    text = stringResource(R.string.subscription_questions)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.padding(bottom = 72.dp),
                verticalArrangement = spacedBy(8.dp)
            ) {
                state.subscriptions.forEach { subscriptionItem ->
                    SubscriptionItem(
                        subscriptionItem = subscriptionItem,
                        onAction = onAction
                    )
                }
            }
        }

        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(24.dp)
                .clickableWithoutIndication {
                    onAction(Subscription.Action.CloseClicked)
                },
            painter = painterResource(R.drawable.ic_close),
            tint = FakeLiveTheme.colors.onSurface,
            contentDescription = "Top icon"
        )

        FakeLivePrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            text = stringResource(R.string.subscription_checkout),
            onClick = {
                onAction(Subscription.Action.CheckoutClick)
            }
        )
    }
}

@Composable
private fun FeatureText(text: String) {
    Text(
        text = text,
        color = FakeLiveTheme.colors.onBackground,
        style = FakeLiveTheme.typography.bodyLarge
    )
}

@Composable
private fun SubscriptionItem(
    subscriptionItem: SubscriptionItem,
    onAction: (Subscription.Action) -> Unit,
) {
    val borderColor = if (subscriptionItem.isSelected) {
        FakeLiveTheme.colors.interactive
    } else {
        Color.Transparent
    }
    val shape = RoundedCornerShape(8.dp)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(blurredBackground)
            .border(
                width = 1.5.dp,
                color = borderColor,
                shape = shape
            )
            .clickableWithoutIndication {
                onAction(Subscription.Action.SubscriptionClick(subscriptionItem.id))
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = subscriptionItem.name,
                color = FakeLiveTheme.colors.onBackgroundVariant,
                style = FakeLiveTheme.typography.bodyMedium.bold,
            )
            Column(modifier = Modifier.padding(top = 8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = subscriptionItem.currentPrice,
                        color = FakeLiveTheme.colors.onBackground,
                        style = FakeLiveTheme.typography.titleMedium,
                    )
                    if (subscriptionItem.isLifetime) {
                        Label(
                            modifier = Modifier.padding(start = 8.dp),
                            text = stringResource(R.string.subscription_use_forever)
                        )
                    } else {
                        subscriptionItem.discountPercent?.let { discountPercent ->
                            Label(
                                modifier = Modifier.padding(start = 8.dp),
                                text = stringResource(R.string.subscription_save, discountPercent)
                            )
                        }
                    }
                }
                subscriptionItem.previousPrice?.let {
                    Text(
                        modifier = Modifier.padding(top = 2.dp),
                        text = subscriptionItem.previousPrice,
                        color = FakeLiveTheme.colors.onBackgroundVariant,
                        style = FakeLiveTheme.typography.bodyMedium.copy(
                            textDecoration = TextDecoration.LineThrough
                        )
                    )
                }
            }
        }
        RadioButton(
            modifier = Modifier.align(Alignment.TopEnd),
            selected = subscriptionItem.isSelected,
            colors = RadioButtonDefaults.colors(
                selectedColor = FakeLiveTheme.colors.interactive,
                unselectedColor = FakeLiveTheme.colors.onSurfaceVariant,
            ),
            onClick = {},
        )
    }
}

@Composable
private fun Label(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(
                FakeLiveTheme.colors.interactive.copy(alpha = 0.1f)
            )
            .padding(
                horizontal = 6.dp,
                vertical = 2.dp
            )
    ) {
        Text(
            text = text,
            color = FakeLiveTheme.colors.interactive,
            style = FakeLiveTheme.typography.bodySmall,
        )
    }
}

@Preview
@Composable
private fun SubscriptionScreenPreview() {
    FakeLiveTheme {
        SubscriptionContent(
            state = Subscription.State(
                subscriptions = listOf(
                    SubscriptionItem(
                        id = "1",
                        offerToken = "",
                        name = "Weekly",
                        currentPrice = "$2,99/week",
                        previousPrice = "$10,99/month",
                        discountPercent = "Save 70%",
                        isLifetime = false,
                        isSelected = true
                    ),
                    SubscriptionItem(
                        id = "2",
                        offerToken = null,
                        name = "Lifetime",
                        currentPrice = "$6,99",
                        previousPrice = "$20,99/month",
                        discountPercent = null,
                        isLifetime = true,
                        isSelected = false
                    )
                )
            ),
            onAction = {}
        )
    }
}