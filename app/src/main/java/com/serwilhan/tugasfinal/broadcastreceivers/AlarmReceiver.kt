package com.serwilhan.tugasfinal.broadcastreceivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.serwilhan.tugasfinal.services.AlarmService
import com.serwilhan.tugasfinal.utils.Constants
import com.serwilhan.tugasfinal.utils.Constants.Companion.ACTION_ALARM_RECEIVER
import com.serwilhan.tugasfinal.utils.Constants.Companion.ACTION_START_SERVICE
import com.serwilhan.tugasfinal.utils.Constants.Companion.ALARM_ID
import com.serwilhan.tugasfinal.utils.Constants.Companion.ALARM_LABEL
import com.serwilhan.tugasfinal.utils.Constants.Companion.ALARM_TIME

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        val alarmName = intent?.getStringExtra(ALARM_LABEL)
        val alarmTime = intent?.getStringExtra(ALARM_TIME)
        val alarmId = intent?.getLongExtra(ALARM_ID, 0)

        val serviceIntent = Intent(context, AlarmService::class.java).apply {
            action = ACTION_START_SERVICE
            putExtra(ALARM_LABEL, alarmName)
            putExtra(ALARM_TIME, alarmTime)
            putExtra(ALARM_ID, alarmId)
        }

        ContextCompat.startForegroundService(context, serviceIntent)
    }
}