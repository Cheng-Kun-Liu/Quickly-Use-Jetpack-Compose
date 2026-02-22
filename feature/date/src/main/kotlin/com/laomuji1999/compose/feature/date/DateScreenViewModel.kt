package com.laomuji1999.compose.feature.date

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.laomuji1999.compose.core.ui.extension.stateInTimeout
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class DateScreenViewModel @Inject constructor():ViewModel() {
    private val _currentYear = MutableStateFlow(DateUtil.getCurrentYear())
    private val _currentMonth = MutableStateFlow(DateUtil.getCurrentMonth())
    private val _currentDay = MutableStateFlow(DateUtil.getCurrentDay())
    private val _yearList = MutableStateFlow(DateUtil.getYearList(DateUtil.getCurrentYear() - 20, DateUtil.getCurrentYear() + 20))
    private val _dateDetailList = MutableStateFlow(DateUtil.getDateDetailList(_currentYear.value))

    val uiState = combine(
        _currentYear,
        _currentMonth,
        _currentDay,
        _yearList,
        _dateDetailList
    ){ currentYear, currentMonth, currentDay, yearList, dateDetailList->
        DateScreenUiState(
            currentYear = currentYear,
            currentMonth = currentMonth,
            currentDay = currentDay,
            yearList = yearList,
            dateDetailList = dateDetailList
        )
    }.stateInTimeout(viewModelScope, DateScreenUiState())

    fun onAction(action: DateScreenAction){
        when(action){
            is DateScreenAction.OnYearClick -> onYearClick(action.year)
            is DateScreenAction.OnMonthClick -> onMonthClick(action.month)
            is DateScreenAction.OnDayClick -> onDayClick(action.day)
        }
    }

    private fun onYearClick(year:Int){
        _currentYear.value = year
        _dateDetailList.value = DateUtil.getDateDetailList(year)
        checkAndSetDay(year, _currentMonth.value, _currentDay.value)
    }

    private fun onMonthClick(month:Int){
        _currentMonth.value = month
        checkAndSetDay(_currentYear.value, month, _currentDay.value)
    }

    private fun onDayClick(day:Int){
        _currentDay.value = day
        checkAndSetDay(_currentYear.value, _currentMonth.value, day)
    }

    private fun checkAndSetDay(year:Int, month:Int, day:Int){
        if (!DateUtil.hasDay(year, month, day)) {
            _currentDay.value = 1
        }
    }
}