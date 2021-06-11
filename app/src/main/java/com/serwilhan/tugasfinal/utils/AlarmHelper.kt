package com.serwilhan.tugasfinal.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.AlarmManagerCompat
import com.serwilhan.tugasfinal.broadcastreceivers.AlarmReceiver
import com.serwilhan.tugasfinal.data.models.AlarmItem
import com.serwilhan.tugasfinal.utils.Constants.Companion.ACTION_ALARM_RECEIVER
import com.serwilhan.tugasfinal.utils.Constants.Companion.ALARM_ID
import com.serwilhan.tugasfinal.utils.Constants.Companion.ALARM_LABEL
import com.serwilhan.tugasfinal.utils.Constants.Companion.ALARM_TIME
import java.util.*

class AlarmHelper(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleAlarm(alarmItem: AlarmItem): AlarmItem {

        val alarmDate = CalendarUtil.setCalendar(alarmItem.hour, alarmItem.minute)

        if (alarmDate.before(Calendar.getInstance())) alarmDate.add(Calendar.DATE, 1)

        val pendingIntent = alarmPendingIntent(alarmItem)

        AlarmManagerCompat.setAndAllowWhileIdle(
            alarmManager,
            AlarmManager.RTC_WAKEUP,
            alarmDate.timeInMillis,
            pendingIntent
        )

        alarmItem.isScheduled = true

        showMessage(alarmItem, "Scheduled")

        return alarmItem
    }

    fun cancelAlarm(alarmItem: AlarmItem): AlarmItem {

        alarmManager.cancel(alarmPendingIntent(alarmItem))

        alarmItem.isScheduled = false

        showMessage(alarmItem, "Cancelled")

        return alarmItem
    }


    private fun alarmPendingIntent(alarmItem: AlarmItem): PendingIntent {

        val alarmTimeString = CalendarUtil.formatCalendarTime(alarmItem.hour, alarmItem.minute)

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            action = ACTION_ALARM_RECEIVER
            putExtra(ALARM_ID, alarmItem.alarmId)
            putExtra(ALARM_LABEL, alarmItem.alarmLabel)
            putExtra(ALARM_TIME, alarmTimeString)
        }

        val pendingIntentRequestCode = alarmItem.alarmId.toInt()

        return PendingIntent.getBroadcast(context, pendingIntentRequestCode, intent, 0)
    }


    private fun showMessage(alarmItem: AlarmItem, state: String) {
        val calendar = CalendarUtil.setCalendar(alarmItem.hour, alarmItem.minute, 0)
        alarmItem.alarmDay = CalendarUtil.getAlarmDay(calendar)
        val alarmTimeString = CalendarUtil.formatCalendarTime(alarmItem.hour, alarmItem.minute)

        Messages.showToast(
            context,
            "Alarm $state for ${alarmItem.alarmDay} at: $alarmTimeString"
        )
    }
}