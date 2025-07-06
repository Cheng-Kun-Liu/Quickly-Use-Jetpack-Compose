package com.laomuji1999.compose.feature.date

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.laomuji1999.compose.core.ui.theme.QuicklyTheme
import com.laomuji1999.compose.core.ui.we.WeTheme
import com.laomuji1999.compose.core.ui.we.widget.outline.WeOutline
import com.laomuji1999.compose.core.ui.we.widget.scaffold.WeScaffold
import com.laomuji1999.compose.core.ui.we.widget.topbar.WeTopBar
import kotlin.math.abs

@Composable
fun DateScreen(
    viewModel: DateScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    DateScreenUi(
        uiState = uiState, onAction = viewModel::onAction
    )
}

@Composable
private fun DateScreenUi(
    uiState: DateScreenUiState, onAction: (DateScreenAction) -> Unit
) {
    WeScaffold(topBar = {
        WeTopBar(title = stringResource(com.laomuji1999.compose.res.R.string.string_demo_screen_date))
        SelectYearUi(
            currentYear = uiState.currentYear,
            yearList = uiState.yearList,
            onYearClick = { onAction(DateScreenAction.OnYearClick(it)) })
        WeOutline(
            modifier = Modifier.fillMaxWidth()
        )
    }, bottomBar = {
        WeOutline(
            modifier = Modifier.fillMaxWidth()
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(WeTheme.dimens.bottomBarHeight),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${uiState.currentYear}/${uiState.currentMonth}/${uiState.currentDay}",
                style = WeTheme.typography.emTitle,
                color = WeTheme.colorScheme.fontColorPrimary,
                modifier = Modifier
            )
        }
    }) {
        MonthDayUi(
            uiState = uiState,
            onYearClick = { onAction(DateScreenAction.OnYearClick(it)) },
            onMonthClick = { onAction(DateScreenAction.OnMonthClick(it)) },
            onDayClick = { onAction(DateScreenAction.OnDayClick(it)) })
    }
}

@Composable
private fun SelectYearUi(
    currentYear: Int,
    yearList: List<Int> = emptyList(),
    onYearClick: (year: Int) -> Unit,
    lazyListState: LazyListState = rememberLazyListState(),
    spacesDp: Dp = 20.dp
) {
    var screenWidth by remember { mutableIntStateOf(0) }
    var itemWidth by remember { mutableIntStateOf(0) }
    val moveToCurrentYear = suspend {
        when (val index = yearList.indexOf(currentYear)) {
            -1 -> {
                // Nothing
            }

            else -> {
                lazyListState.animateScrollToItem(index, -(screenWidth - itemWidth) / 2)
            }
        }
    }
    LaunchedEffect(currentYear) {
        moveToCurrentYear()
    }
    LazyRow(modifier = Modifier
        .background(WeTheme.colorScheme.background)
        .onGloballyPositioned {
            if (screenWidth == 0 && it.size.width > 0) {
                screenWidth = it.size.width
            }
        }
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(spacesDp),
        state = lazyListState) {
        items(yearList) { item ->
            Text(
                text = "$item",
                style = WeTheme.typography.emTitle,
                modifier = Modifier
                    .onGloballyPositioned {
                        if (itemWidth == 0 && it.size.width > 0) {
                            itemWidth = it.size.width
                        }
                    }
                    .padding(vertical = spacesDp)
                    .clickable {
                        onYearClick(item)
                    },
                color = if (currentYear == item) WeTheme.colorScheme.fontColorHeavy else WeTheme.colorScheme.fontColorLight
            )
        }
    }
}

@Composable
private fun MonthDayUi(
    uiState: DateScreenUiState,
    dragWidth: Dp = 30.dp,
    onYearClick: (year: Int) -> Unit,
    onMonthClick: (month: Int) -> Unit,
    onDayClick: (day: Int) -> Unit
) {
    val dragWidthPx = with(LocalDensity.current) {
        dragWidth.toPx()
    }
    var dragStartX by remember { mutableFloatStateOf(0f) }
    Column(modifier = Modifier
        .pointerInput(uiState.currentYear) {
            detectHorizontalDragGestures(onDragStart = {
                dragStartX = 0f
            }, onDragEnd = {
                if (abs(dragStartX) > dragWidthPx) {
                    onYearClick(uiState.currentYear + if (dragStartX < 0) 1 else -1)
                }
            }, onHorizontalDrag = { _, dragAmount ->
                dragStartX += dragAmount
            })

        }
        .verticalScroll(rememberScrollState())) {
        for (i in 0 until uiState.dateDetailList.size) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                for (j in 0 until 2) {
                    val index = i * 2 + j
                    if (index < uiState.dateDetailList.size) {
                        Box(modifier = Modifier.weight(1f)) {
                            MonthDetailUi(
                                uiState = uiState,
                                dateDetail = uiState.dateDetailList[index],
                                onMonthClick = onMonthClick,
                                onDayClick = onDayClick
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MonthDetailUi(
    uiState: DateScreenUiState,
    dateDetail: DateUtil.DateDetail,
    onMonthClick: (month: Int) -> Unit,
    onDayClick: (day: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${dateDetail.month}",
            style = WeTheme.typography.title,
            color = WeTheme.colorScheme.fontColorLight,
            modifier = Modifier.clickable {
                onMonthClick(dateDetail.month)
            })
        val weekList =
            stringArrayResource(id = com.laomuji1999.compose.res.R.array.string_date_screen_week_day)
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            weekList.forEach {
                Box(modifier = Modifier.weight(1f)) {
                    Text(
                        text = it,
                        style = WeTheme.typography.footnote,
                        modifier = Modifier.align(Alignment.Center),
                        color = WeTheme.colorScheme.fontColorLight
                    )
                }
            }
        }
        if (dateDetail.dayList.isEmpty()) {
            return
        }

        Column {
            for (i in 0 until dateDetail.completedDayList.size / 7) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (j in 0 until 7) {
                        val index = i * 7 + j
                        val day = dateDetail.completedDayList[index].first
                        Box(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${if (day == -1) "" else day}",
                                style = WeTheme.typography.small,
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .clickable {
                                        onMonthClick(dateDetail.month)
                                        onDayClick(day)
                                    },
                                color = if (uiState.currentYear == dateDetail.year && uiState.currentMonth == dateDetail.month && uiState.currentDay == day) WeTheme.colorScheme.primaryButton else WeTheme.colorScheme.fontColorHeavy
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewDateScreen() {
    QuicklyTheme {
        @SuppressLint("ViewModelConstructorInComposable")
        DateScreen(viewModel = DateScreenViewModel())
    }
}

