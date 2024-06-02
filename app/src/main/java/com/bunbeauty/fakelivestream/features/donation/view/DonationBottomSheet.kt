package com.bunbeauty.fakelivestream.features.donation.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.fakelivestream.R
import com.bunbeauty.fakelivestream.common.ui.components.bottomsheet.FakeLiveBottomSheet
import com.bunbeauty.fakelivestream.common.ui.components.bottomsheet.FakeLiveBottomSheetContent
import com.bunbeauty.fakelivestream.common.ui.components.button.FakeLivePrimaryButton
import com.bunbeauty.fakelivestream.common.ui.theme.FakeLiveStreamTheme
import com.bunbeauty.fakelivestream.common.ui.theme.FakeLiveTheme
import com.bunbeauty.fakelivestream.features.billing.Product
import com.bunbeauty.fakelivestream.features.donation.presentation.Donation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DonationBottomSheet(
    product: Product?,
    onAction: (Donation.Action) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (product != null) {
        FakeLiveBottomSheet(
            modifier = modifier,
            containerColor = FakeLiveStreamTheme.colors.background,
            onDismissRequest = {
                onAction(Donation.Action.HideDonation)
            }
        ) {
            DonationBottomSheetContent(
                product = product,
                onAction = onAction,
            )
        }
    }
}

@Composable
private fun ColumnScope.DonationBottomSheetContent(
    product: Product,
    onAction: (Donation.Action) -> Unit,
) {
    FakeLiveBottomSheetContent(
        title = product.name,
        titleColor = FakeLiveStreamTheme.colors.onBackground,
        dividerColor = FakeLiveStreamTheme.colors.borderVariant,
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            text = product.description,
            style = FakeLiveStreamTheme.typography.bodyLarge,
            textAlign = TextAlign.Justify
        )
        FakeLivePrimaryButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            text = stringResource(id = R.string.donation_donate, product.price),
            onClick = {
                onAction(Donation.Action.DonateClick(productId = product.id))
            },
        )
    }
}

@Preview
@Composable
private fun NotEmptyQuestionsBottomSheetPreview() {
    FakeLiveTheme {
        Column {
            DonationBottomSheetContent(
                product = Product(
                    id = "1",
                    name = "Good Samaritan \uD83D\uDE4F",
                    description = "Thank you for your generous donation! Your support is invaluable to us. Every contribution helps us move closer to our goals and makes a meaningful impact.",
                    price = "$1.00",
                ),
                onAction = {}
            )
        }
    }
}