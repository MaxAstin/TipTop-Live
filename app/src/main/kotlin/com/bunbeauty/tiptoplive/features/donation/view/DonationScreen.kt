package com.bunbeauty.tiptoplive.features.donation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLiveIconButton
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLivePrimaryButton
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.bold
import com.bunbeauty.tiptoplive.features.billing.Product
import com.bunbeauty.tiptoplive.features.donation.presentation.Donation
import com.bunbeauty.tiptoplive.features.donation.presentation.DonationViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun DonationScreen(
    navController: NavHostController,
    onDonateClick: (String) -> Unit,
) {
    val viewModel: DonationViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = remember {
        { action: Donation.Action ->
            viewModel.onAction(action)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.onEach { event ->
            when (event) {
                Donation.Event.GoBack -> {
                    navController.popBackStack()
                }

                is Donation.Event.StartPurchaseFlow -> {
                    onDonateClick(event.productId)
                }
            }
        }.launchIn(this)
    }

    DonationContent(
        state = state,
        onAction = onAction,
    )
}

@Composable
private fun DonationContent(
    state: Donation.State,
    onAction: (Donation.Action) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = FakeLiveTheme.colors.background
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = spacedBy(16.dp)
            ) {
                FakeLiveIconButton(
                    iconId = R.drawable.ic_back,
                    iconTint = FakeLiveTheme.colors.onBackground,
                    contentDescription = "Back",
                    withBorder = false,
                    onClick = {
                        onAction(Donation.Action.BackClick)
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        modifier = Modifier.weight(4f),
                        painter = painterResource(R.drawable.img_logo),
                        contentDescription = "Logo"
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(
                        id = R.string.donation_title,
                        stringResource(R.string.app_name)
                    ),
                    style = FakeLiveTheme.typography.titleLarge.bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.donation_descriptio),
                    style = FakeLiveTheme.typography.bodyLarge,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = spacedBy(4.dp)
            ) {
                repeat(state.productList.size / 2) { i ->
                    Row(horizontalArrangement = spacedBy(12.dp)) {
                        repeat(2) { j ->
                            state.productList.getOrNull(i * 2 + j)?.let { product ->
                                FakeLivePrimaryButton(
                                    modifier = Modifier.weight(1f),
                                    text = product.price,
                                    onClick = {
                                        onAction(Donation.Action.DonationClick(productId = product.id))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        DonationBottomSheet(
            product = state.shownProduct,
            onAction = onAction
        )

        SuccessDonationBottomSheet(
            product = state.shownSuccessDonation,
            onAction = onAction
        )
    }
}

@LocalePreview
@Composable
fun IntroScreenPreview() {
    DonationContent(
        state = Donation.State(
            productList = listOf(
                Product(
                    id = "1",
                    name = "",
                    description = "",
                    price = "$1",
                ),
                Product(
                    id = "2",
                    name = "",
                    description = "",
                    price = "$2",
                ),
                Product(
                    id = "3",
                    name = "",
                    description = "",
                    price = "$3",
                ),
                Product(
                    id = "4",
                    name = "",
                    description = "",
                    price = "$4",
                ),
            ).toImmutableList(),
            shownProduct = null
        ),
        onAction = {},
    )
}