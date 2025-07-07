package com.laomuji1999.compose.core.ui.we.widget.input

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.widget.row.WeRow

@Composable
fun WeInput(
    modifier: Modifier = Modifier,
    title: String? = null,
    value: String = "",
    tip: String = "",
    onValueChange: (String) -> Unit = {},
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Done
    ),
    onImeAction: () -> Unit = {}
) {
    val focusRequester = remember { FocusRequester() }
    WeRow(
        modifier = modifier,
        center = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                title?.let {
                    Text(
                        text = it,
                        style = WeTheme.typography.title,
                        color = WeTheme.colorScheme.fontColorHeavy,
                        modifier = Modifier.width(WeTheme.dimens.inputLabelWidth)
                    )
                    Spacer(modifier = Modifier.width(WeTheme.dimens.rowInnerPaddingHorizontal))
                }
                BasicTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .fillMaxHeight()
                        .weight(1f),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                innerTextField()
                            }
                            if (value.isEmpty()) {
                                Row(
                                    modifier = Modifier.fillMaxSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = tip,
                                        style = WeTheme.typography.title,
                                        color = WeTheme.colorScheme.fontColorLight
                                    )
                                }
                            }
                        }
                    },
                    textStyle = WeTheme.typography.title.copy(color = WeTheme.colorScheme.fontColorHeavy),
                    cursorBrush = SolidColor(WeTheme.colorScheme.cursorColor),
                    singleLine = true,
                    keyboardOptions = keyboardOptions,
                    keyboardActions = KeyboardActions(onPrevious = {
                        onImeAction()
                    }, onNext = {
                        onImeAction()
                    }, onDone = {
                        onImeAction()
                    }, onSearch = {
                        onImeAction()
                    }, onSend = {
                        onImeAction()
                    }, onGo = {
                        onImeAction()
                    })
                )
            }
        },
        onClick = {
            focusRequester.requestFocus()
        },
        backgroundColor = Color.Transparent,
    )
}