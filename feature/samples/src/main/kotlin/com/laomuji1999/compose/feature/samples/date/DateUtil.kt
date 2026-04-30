package com.laomuji1999.compose.feature.samples.date

import java.util.Calendar

object DateUtil {
    /**
     * @param year �?
     * @param month �?
     * @param dayList �?周几
     */
    data class DateDetail(
        val year: Int,
        val month: Int,
        val dayList: List<Pair<Int, Int>>
    ){
        // 补全的日�?
        val completedDayList = mutableListOf<Pair<Int, Int>>()

        init {
            initCompletedDayList()
        }

        private fun initCompletedDayList(){
            if(dayList.isEmpty()){
                return
            }
            //补全前面的日�?
            if(dayList[0].second != 1){
                for (i in 1 until dayList[0].second){
                    completedDayList.add(Pair(-1, i))
                }
            }
            //添加日期
            dayList.forEach {
                completedDayList.add(it)
            }
            //补全后面的日�?
            if(dayList[dayList.lastIndex].second != 7){
                for (i in dayList[dayList.lastIndex].second + 1..7){
                    completedDayList.add(Pair(-1, i))
                }
            }
        }
    }


    fun getYearList(startYear:Int, endYear:Int):List<Int>{
        val yearList = mutableListOf<Int>()
        for (i in startYear..endYear){
            yearList.add(i)
        }
        return yearList
    }

    fun getCurrentYear():Int{
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.YEAR)
    }

    fun getCurrentMonth():Int{
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.MONTH) + 1
    }

    fun getCurrentDay():Int{
        val calendar = Calendar.getInstance()
        return calendar.get(Calendar.DAY_OF_MONTH)
    }

    fun getDateDetail(year:Int, month:Int):DateDetail{
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month - 1)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        val dayList = mutableListOf<Pair<Int, Int>>()
        for (i in 1..calendar.getActualMaximum(Calendar.DAY_OF_MONTH)){
            dayList.add(Pair(i, calendar.get(Calendar.DAY_OF_WEEK)))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return DateDetail(year, month, dayList)
    }

    fun getDateDetailList(year:Int):List<DateDetail>{
        val dayListList = mutableListOf<DateDetail>()
        for (i in 1..12){
            dayListList.add(getDateDetail(year, i))
        }
        return dayListList
    }

    fun hasDay(year:Int, month:Int, day:Int):Boolean{
        val dateDetail = getDateDetail(year, month)
        dateDetail.dayList.forEach {
            if(it.first == day){
                return true
            }
        }
        return false
    }
}
