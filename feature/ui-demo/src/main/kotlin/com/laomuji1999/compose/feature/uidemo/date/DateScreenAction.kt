package com.laomuji1999.compose.feature.uidemo.date

sealed interface DateScreenAction {
    data class OnYearClick(val year: Int) : DateScreenAction
    data class OnMonthClick(val month: Int) : DateScreenAction
    data class OnDayClick(val day: Int) : DateScreenAction
    data class OnDateSelect(val year: Int, val month: Int, val day: Int) : DateScreenAction
}
