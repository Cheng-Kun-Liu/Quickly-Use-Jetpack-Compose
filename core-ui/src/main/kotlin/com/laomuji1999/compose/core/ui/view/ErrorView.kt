package com.laomuji1999.compose.core.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.widget.button.WeButton
import com.laomuji1999.compose.core.ui.we.widget.button.WeButtonColor
import com.laomuji1999.compose.core.ui.we.widget.button.WeButtonType
import com.laomuji1999.compose.core.ui.we.widget.scaffold.WeScaffold
import com.laomuji1999.compose.res.R

class ErrorViewContent {
    @Composable
    fun uniqueKey(): String {
        val saveData = rememberSaveable {
            "${System.currentTimeMillis()}"
        }
        return saveData
    }
}

@Composable
fun ErrorView(copyContent: @Composable ErrorViewContent.() -> Unit) {
    var isClickRefresh by rememberSaveable { mutableStateOf(false) }
    if (isClickRefresh) {
        copyContent(ErrorViewContent())
    } else {
        WeScaffold {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    modifier = Modifier.width(180.dp),
                    contentScale = ContentScale.FillWidth,
                    colorFilter = ColorFilter.tint(WeTheme.colorScheme.fontColorError)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = stringResource(id = R.string.string_error_view_title),
                    color = WeTheme.colorScheme.fontColorError,
                    style = WeTheme.typography.title
                )
                Text(
                    text = stringResource(id = R.string.string_error_view_content),
                    color = WeTheme.colorScheme.fontColorError,
                    style = WeTheme.typography.body
                )
                Spacer(modifier = Modifier.height(10.dp))
                WeButton(
                    weButtonType = WeButtonType.Warp,
                    weButtonColor = WeButtonColor.Wrong,
                    text = stringResource(id = R.string.string_error_view_refresh),
                    onClick = { isClickRefresh = true }
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
fun PreviewErrorView() {
    QuicklyTheme {
        ErrorView {

        }
    }
}