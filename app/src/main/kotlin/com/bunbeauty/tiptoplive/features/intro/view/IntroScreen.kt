package com.bunbeauty.tiptoplive.features.intro.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import com.bunbeauty.tiptoplive.common.navigation.NavigationRote
import com.bunbeauty.tiptoplive.common.ui.LocalePreview
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLivePrimaryButton
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.common.ui.theme.bold
import com.bunbeauty.tiptoplive.features.intro.presentation.Intro
import com.bunbeauty.tiptoplive.features.intro.presentation.IntroViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Composable
fun IntroScreen(navController: NavHostController) {
    val viewModel: IntroViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val onAction = remember {
        { action: Intro.Action ->
            viewModel.onAction(action)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.onEach { event ->
            when (event) {
                is Intro.Event.OpenPreparation -> {
                    navController.navigate(
                        route = NavigationRote.Preparation()
                    ) {
                        popUpTo(NavigationRote.Intro) {
                            inclusive = true
                        }
                    }
                }
            }
        }.launchIn(this)
    }

    IntroContent(
        isChecked = state.isChecked,
        onAction = onAction
    )
}

@Composable
private fun IntroContent(
    isChecked: Boolean,
    onAction: (Intro.Action) -> Unit
) {
    if (isChecked) {
        Column(modifier = Modifier.background(FakeLiveTheme.colors.background)) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = spacedBy(16.dp)
            ) {
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
                        id = R.string.intro_welcome,
                        stringResource(R.string.app_name)
                    ),
                    style = FakeLiveTheme.typography.titleLarge.bold,
                    textAlign = TextAlign.Center
                )
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.intro_description),
                    style = FakeLiveTheme.typography.bodyLarge,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.weight(1f))
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = FakeLiveTheme.colors.borderVariant,
            )
            FakeLivePrimaryButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                text = stringResource(id = R.string.intro_next),
                onClick = {
                    onAction(Intro.Action.NextClick)
                },
            )
        }
    } else {
        Box(modifier = Modifier.fillMaxSize())
    }
}

@LocalePreview
@Composable
fun IntroScreenPreview() {
    IntroContent(
        isChecked = true,
        onAction = {}
    )
}