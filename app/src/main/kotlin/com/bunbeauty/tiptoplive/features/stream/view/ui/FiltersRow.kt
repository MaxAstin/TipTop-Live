package com.bunbeauty.tiptoplive.features.stream.view.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme

private val filters = listOf(
    R.drawable.none_filter,
    R.drawable.which_disnay_filter,
    R.drawable.good_day_filter,
    R.drawable.glitter_filter,
    R.drawable.aqua_glitter_filter,
    R.drawable.doodle_heart_filter,
    R.drawable.nineties_stethic_filter,
    R.drawable.heart_bloom_filter,
)

@Composable
fun FiltersRow(
    onFilterChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember {
        mutableIntStateOf(0)
    }

    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = spacedBy(2.dp)
    ) {
        itemsIndexed(filters) { i, filterResId ->
            val borderColor = if (i == selectedIndex) {
                FakeLiveTheme.colors.icon
            } else {
                Color.Transparent
            }
            Box(
                modifier = Modifier
                    .size(58.dp)
                    .border(2.dp, borderColor, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Image(
                    modifier = Modifier
                        .clickableWithoutIndication {
                            selectedIndex = i
                            onFilterChanged(i)
                        }
                        .size(48.dp)
                        .clip(CircleShape),
                    painter = painterResource(filterResId),
                    contentDescription = "filter"
                )
            }
        }
    }
}

@Preview
@Composable
fun FiltersRowPreview() {
    FakeLiveTheme {
        FiltersRow(
            onFilterChanged = {}
        )
    }
}