package com.laomuji1999.compose.core.ui.we.widget.actionsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.laomuji1999.compose.core.ui.BackInvokedCallbackProgress
import com.laomuji1999.compose.core.ui.we.WeDialog
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.animated.AnimatedSlideFromBottom
import com.laomuji1999.compose.core.ui.we.animated.AnimatedState
import com.laomuji1999.compose.core.ui.we.animated.AnimatedState.Companion.rememberAnimatedState
import com.laomuji1999.compose.core.ui.we.colorscheme.LocalWeColorScheme

@Composable
fun WeActionSheetDialog(
    onDismissRequest: () -> Unit = {},
    content: @Composable (AnimatedState) -> Unit,
) {

    val dragRate = 1.5f
    var progress by remember { mutableStateOf<Float?>(null) }
    val isDarkFront = LocalWeColorScheme.current.isDarkFont

    val state = rememberAnimatedState(startDuration = 200, endDuration = 300)
    LaunchedEffect(Unit) {
        state.show()
    }

    WeDialog(
        onDismissRequest = onDismissRequest,
        dimProgress = 1f - (progress ?: 0f) * dragRate,
        lightStatusBars = if (isDarkFront) (progress ?: 0f) * dragRate > 0.5f else false,
    ) {
        BackInvokedCallbackProgress(
            onBackHandle = {
                onDismissRequest()
            },
            onBackProgressChanged = {
                progress = it
            },
            onBackCancel = {
                progress = null
            },
        )

        var contentHeight by remember { mutableIntStateOf(0) }
        Column(modifier = Modifier.align(Alignment.BottomCenter)) {
            AnimatedSlideFromBottom(
                state = state
            ) {
                Column(
                    modifier = Modifier
                        .onSizeChanged {
                            contentHeight = it.height
                        }
                        .offset {
                            IntOffset(
                                x = 0,
                                y = if (progress == null) {
                                    0
                                } else {
                                    (contentHeight * progress!! * dragRate).toInt()
                                }
                            )
                        }
                        .clip(
                            RoundedCornerShape(
                                topStart = WeTheme.dimens.actionSheetRoundedCornerDp,
                                topEnd = WeTheme.dimens.actionSheetRoundedCornerDp
                            )
                        )
                        .background(WeTheme.colorScheme.background)
                ) {
                    content(state)
                }
            }
        }
    }
}