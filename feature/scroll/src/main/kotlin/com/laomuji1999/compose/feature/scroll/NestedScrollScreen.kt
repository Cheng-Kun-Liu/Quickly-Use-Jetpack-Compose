package com.laomuji1999.compose.feature.scroll

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import com.laomuji1999.compose.core.ui.we.widget.click.WeClick
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutlineType
import com.laomuji1999.compose.core.ui.we.widget.scaffold.WeScaffold
import com.laomuji1999.compose.core.ui.we.widget.topbar.WeTopBar

@Composable
internal fun NestedScrollScreen() {
    var showConnectionScreen by rememberSaveable { mutableStateOf(false) }
    var showDispatcherScreen by rememberSaveable { mutableStateOf(false) }

    BackHandler(showConnectionScreen || showDispatcherScreen) {
        showConnectionScreen = false
        showDispatcherScreen = false
    }

    if (showConnectionScreen) {
        NestedScrollConnectionScreen()
        return
    }
    if (showDispatcherScreen) {
        NestedScrollDispatcherScreen()
        return
    }
    WeScaffold(
        topBar = {
            WeTopBar()
        }
    ) {
        WeOutline(weOutlineType = WeOutlineType.Full)
        WeClick(
            title = stringResource(com.laomuji1999.compose.res.R.string.string_demo_screen_scroll_connect),
            onClick = {
                showConnectionScreen = true
            },
        )
        WeOutline(weOutlineType = WeOutlineType.PaddingHorizontal)
        WeClick(
            title = stringResource(com.laomuji1999.compose.res.R.string.string_demo_screen_scroll_dispatcher),
            onClick = {
                showDispatcherScreen = true
            }
        )
        WeOutline(weOutlineType = WeOutlineType.Full)
    }
}