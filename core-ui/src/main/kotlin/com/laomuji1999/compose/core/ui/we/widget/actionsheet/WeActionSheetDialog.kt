package com.laomuji1999.compose.core.ui.we.widget.actionsheet

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.DialogProperties
import com.laomuji1999.compose.core.ui.BackInvokedCallbackProgress
import com.laomuji1999.compose.core.ui.clickableDebounce
import com.laomuji1999.compose.core.ui.we.WeDialog
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.animated.AnimatedSlideFromBottom
import com.laomuji1999.compose.core.ui.we.animated.AnimatedSlideFromBottomScope

@Composable
fun WeActionSheetDialog(
    onDismissRequest: () -> Unit = {},
    content: @Composable AnimatedSlideFromBottomScope.() -> Unit,
) {
    var progress by remember { mutableStateOf<Float?>(null) }
    WeDialog(
        onDismissRequest = {},
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
        ),
        dimProgress = 1f - (progress?:0f),
        lightStatusBars = (progress?:0f) > 0.5f
    ) {
        BackInvokedCallbackProgress(onBackHandle = {
            onDismissRequest()
        }, onBackProgressChanged = {
            progress = it
        })

        var contentHeight by remember { mutableIntStateOf(0) }
        Box(modifier = Modifier.fillMaxSize()) {
            AnimatedSlideFromBottom {
                Box(
                    modifier = Modifier
                        .clickableDebounce(
                            indication = null,
                            onClick = {
                                hide { onDismissRequest() }
                            },
                        )
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)

                            .onSizeChanged {
                                contentHeight = it.height
                            }
                            .offset {
                                IntOffset(
                                    x = 0, y = if (progress == null) {
                                        0
                                    } else {
                                        (contentHeight * progress!!).toInt()
                                    }
                                )
                            }
                            .clip(
                                RoundedCornerShape(
                                    topStart = WeTheme.dimens.actionSheetRoundedCornerDp,
                                    topEnd = WeTheme.dimens.actionSheetRoundedCornerDp
                                )
                            )
                    ) {
                        content()
                    }
                }
            }
        }
    }
}