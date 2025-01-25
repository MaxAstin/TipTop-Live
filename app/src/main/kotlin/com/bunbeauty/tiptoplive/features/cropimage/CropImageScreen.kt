package com.bunbeauty.tiptoplive.features.cropimage

import android.net.Uri
import android.widget.LinearLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bunbeauty.tiptoplive.R
import com.bunbeauty.tiptoplive.common.navigation.NavigationRote
import com.bunbeauty.tiptoplive.common.ui.clickableWithoutIndication
import com.bunbeauty.tiptoplive.common.ui.components.button.FakeLiveDialogButton
import com.bunbeauty.tiptoplive.common.ui.theme.FakeLiveTheme
import com.bunbeauty.tiptoplive.features.main.CropImageDefaults
import com.canhub.cropper.CropImageView
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun CropImageScreen(
    navController: NavController,
    uri: Uri?,
    onMockClick: () -> Unit
) {
    val cropEvent = remember {
        Channel<Unit>()
    }

    Column(
        modifier = Modifier
            .background(FakeLiveTheme.colors.background)
            .fillMaxSize()
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Icon(
                modifier = Modifier
                    .size(28.dp)
                    .clickableWithoutIndication {
                        navController.popBackStack()
                    },
                imageVector = ImageVector.vectorResource(R.drawable.ic_close),
                contentDescription = "Close",
                tint = FakeLiveTheme.colors.onBackground,
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                modifier = Modifier
                    .size(28.dp)
                    .clickableWithoutIndication {
                        onMockClick()
                    },
                imageVector = ImageVector.vectorResource(R.drawable.ic_magic_wand),
                contentDescription = "Magic wand",
                tint = FakeLiveTheme.colors.onBackground,
            )
        }
        CropImageView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            navController = navController,
            uri = uri,
            cropEvent = cropEvent.consumeAsFlow()
        )

        val scope = rememberCoroutineScope()
        FakeLiveDialogButton(
            modifier = Modifier
                .align(Alignment.End)
                .padding(12.dp),
            text = stringResource(R.string.crop_image_next),
            iconId = R.drawable.ic_forward,
            shape = RoundedCornerShape(40.dp),
            background = FakeLiveTheme.colors.interactive,
            onClick = {
                scope.launch {
                    cropEvent.send(Unit)
                }
            }
        )
    }
}

@Composable
private fun CropImageView(
    navController: NavController,
    uri: Uri?,
    cropEvent: Flow<Unit>,
    modifier: Modifier = Modifier
) {
    uri ?: return

    val scope = rememberCoroutineScope()
    Box(modifier = modifier.fillMaxSize()) {
        AndroidView(
            modifier = Modifier.align(Alignment.Center),
            factory = { context ->
                val cropImageView = CropImageView(context)

                scope.launch {
                    cropEvent.first()
                    cropImageView.croppedImageAsync()
                }

                LinearLayout(context).apply {
                    cropImageView.apply {
                        setImageCropOptions(CropImageDefaults.options())
                        setImageUriAsync(uri)
                    }.also { view ->
                        val listener = CropImageView.OnCropImageCompleteListener { _, result ->
                            navController.navigate(
                                route = NavigationRote.Preparation(
                                    uri = result.uriContent.toString()
                                )
                            ) {
                                popUpTo<NavigationRote.Preparation> {
                                    inclusive = true
                                }
                            }
                        }
                        view.setOnCropImageCompleteListener(listener)
                    }
                    addView(cropImageView)
                }
            }
        )
    }
}

@Preview
@Composable
private fun CropImageScreenPreview() {
    FakeLiveTheme {
        CropImageScreen(
            navController = rememberNavController(),
            uri = Uri.EMPTY,
            onMockClick = {}
        )
    }
}