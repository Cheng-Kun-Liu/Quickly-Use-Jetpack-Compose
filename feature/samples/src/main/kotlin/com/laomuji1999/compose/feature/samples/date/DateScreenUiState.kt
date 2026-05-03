package com.laomuji1999.compose.feature.samples.date

data class DateScreenUiState(
    val yearList:List<Int> = emptyList(),
    val dateDetailList:List<DateUtil.DateDetail> = emptyList(),
    val currentYear: Int = -1,
    val currentMonth: Int = -1,
    val currentDay: Int = -1,
)
