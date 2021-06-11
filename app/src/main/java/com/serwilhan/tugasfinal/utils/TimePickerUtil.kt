package com.serwilhan.tugasfinal.utils

import android.os.Build
import android.widget.TimePicker

object TimePickerUtil {

    fun getTime(timePicker: TimePicker): Pair<Int, Int> {

        val hour: Int
        val minute: Int

        if (Build.VERSION.SDK_INT >= 23) {
            hour = timePicker.hour
            minute = timePicker.minute
        }
        else {
            hour = timePicker.currentHour
            minute = timePicker.currentMinute
        }

        return Pair(hour, minute)
    }
}