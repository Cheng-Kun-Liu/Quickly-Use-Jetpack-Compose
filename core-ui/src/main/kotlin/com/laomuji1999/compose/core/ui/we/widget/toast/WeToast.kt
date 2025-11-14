package com.laomuji1999.compose.core.ui.we.widget.toast

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.laomuji1999.compose.core.ui.clickableDebounce
import com.laomuji1999.compose.core.ui.we.WeDialog
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.icons.Done
import com.laomuji1999.compose.core.ui.we.icons.Error
import com.laomuji1999.compose.core.ui.we.icons.Loading
import com.laomuji1999.compose.core.ui.we.icons.WeIcons

@Composable
fun WeToast(
    weToastType: WeToastType, message: String, onDismissRequest: () -> Unit = {}
) {
    WeDialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickableDebounce(indication = null, onClick = onDismissRequest)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .clip(RoundedCornerShape(12.dp))
                    .background(WeTheme.colorScheme.toastBackgroundColor)
                    .size(WeTheme.dimens.toastSize),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (weToastType) {
                    WeToastType.Done -> {
                        Image(
                            imageVector = WeIcons.Done,
                            contentDescription = null,
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier.size(WeTheme.dimens.toastIconSize)
                        )
                    }

                    WeToastType.Loading -> {
                        val infiniteTransition =
                            rememberInfiniteTransition(label = "WeToastType.Loading")
                        val rotateDegree by infiniteTransition.animateFloat(
                            initialValue = 0f,
                            targetValue = 360f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(durationMillis = 1000, easing = LinearEasing),
                                repeatMode = RepeatMode.Restart
                            ),
                            label = "WeToastType.Loading"
                        )

                        Image(
                            imageVector = WeIcons.Loading,
                            contentDescription = null,
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier
                                .size(WeTheme.dimens.toastIconSize)
                                .rotate(rotateDegree)
                        )
                    }

                    WeToastType.Error -> {
                        Image(
                            imageVector = WeIcons.Error,
                            contentDescription = null,
                            contentScale = ContentScale.FillHeight,
                            modifier = Modifier.size(WeTheme.dimens.toastIconSize)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(WeTheme.dimens.toastDividerHeight))
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = WeTheme.dimens.rowInnerPaddingHorizontal),
                    text = message,
                    maxLines = 4,
                    textAlign = TextAlign.Center,
                    overflow = TextOverflow.Ellipsis,
                    style = WeTheme.typography.title,
                    color = WeTheme.colorScheme.onToastBackgroundColor
                )
            }
        }
    }
}