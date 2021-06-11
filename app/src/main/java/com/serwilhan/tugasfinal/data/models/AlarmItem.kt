package com.serwilhan.tugasfinal.data.models

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.AlarmManagerCompat
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.serwilhan.tugasfinal.broadcastreceivers.AlarmReceiver
import com.serwilhan.tugasfinal.utils.CalendarUtil
import com.serwilhan.tugasfinal.utils.Constants
import com.serwilhan.tugasfinal.utils.Constants.Companion.ACTION_ALARM_RECEIVER
import com.serwilhan.tugasfinal.utils.Constants.Companion.ACTION_START_SERVICE
import com.serwilhan.tugasfinal.utils.Constants.Companion.ALARM_ID
import com.serwilhan.tugasfinal.utils.Constants.Companion.ALARM_LABEL
import com.serwilhan.tugasfinal.utils.Constants.Companion.ALARM_TIME
import com.serwilhan.tugasfinal.utils.Messages
import java.util.*

@Entity(
    tableName = "alarm_table",
    indices = [Index(value = ["hour", "minute", "alarmDay"], unique = true)]
)
data class AlarmItem(

    @PrimaryKey
    val alarmId: Long,
    val alarmLabel: String,
    val hour: Int,
    val minute: Int,
    var alarmDay: String,
    var isScheduled: Boolean = true,
    val currentTime: Long
)