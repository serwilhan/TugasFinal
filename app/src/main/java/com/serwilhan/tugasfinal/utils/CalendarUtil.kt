package com.serwilhan.tugasfinal.utils

import java.text.DateFormat
import java.util.*


private const val TAG = "calendarUtil"

object CalendarUtil {

    fun setCalendar(hour: Int, minute: Int, second: Int = 0): Calendar {

        return Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, second)
        }
    }

    fun formatCalendarTime(hour: Int, minute: Int): String =
        DateFormat.getTimeInstance(DateFormat.SHORT).format(setCalendar(hour, minute).time)

    fun getAlarmDay(alarmDate: Calendar) =
        if (alarmDate.before(Calendar.getInstance())) {

            alarmDate.add(Calendar.DATE, 1)

            Constants.TOMORROW
        } else Constants.TODAY


    fun isTimePassed(alarmDate: Calendar) =
        alarmDate.before(Calendar.getInstance())

    private fun day(day: Int) = when (day) {

        Calendar.MONDAY -> "Monday"
        Calendar.TUESDAY -> "Tuesday"
        Calendar.WEDNESDAY -> "Wednesday"
        Calendar.THURSDAY -> "Thursday"
        Calendar.FRIDAY -> "Friday"
        Calendar.SATURDAY -> "Saturday"
        Calendar.SUNDAY -> "Sunday"
        else -> "Wrong Day"
    }
}