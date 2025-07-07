package com.laomuji1999.compose.core.ui.we.widget.actionsheet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutlineType
import com.laomuji1999.compose.core.ui.we.widget.row.WeRow

@Composable
fun WeActionSheet(
    text: String,
    onClick: () -> Unit = {},
    weActionSheetType: WeActionSheetType = WeActionSheetType.Normal,
    weOutlineType: WeOutlineType = WeOutlineType.Full
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        WeRow(
            start = {
                Spacer(modifier = Modifier.weight(1f))
            },
            center = {
                when (weActionSheetType) {
                    WeActionSheetType.Summary -> {
                        Text(
                            text = text,
                            style = WeTheme.typography.desc,
                            color = WeTheme.colorScheme.fontColorLight
                        )
                    }

                    WeActionSheetType.Normal -> {
                        Text(
                            text = text,
                            style = WeTheme.typography.title,
                            color = WeTheme.colorScheme.fontColorHeavy
                        )
                    }

                    WeActionSheetType.Wrong -> {
                        Text(
                            text = text,
                            style = WeTheme.typography.title,
                            color = WeTheme.colorScheme.fontColorError
                        )
                    }

                    WeActionSheetType.Primary -> {
                        Text(
                            text = text,
                            style = WeTheme.typography.title,
                            color = WeTheme.colorScheme.fontColorPrimary
                        )
                    }
                }
            },
            end = {
                Spacer(modifier = Modifier.weight(1f))
            },
            onClick = onClick,
        )
        WeOutline(weOutlineType = weOutlineType)
    }

}
