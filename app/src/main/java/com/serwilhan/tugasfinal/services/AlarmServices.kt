package com.serwilhan.tugasfinal.services

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.serwilhan.tugasfinal.R
import com.serwilhan.tugasfinal.data.database.AlarmDatabase
import com.serwilhan.tugasfinal.data.repository.AlarmRepository
import com.serwilhan.tugasfinal.ui.activities.RingActivity
import com.serwilhan.tugasfinal.utils.Constants
import com.serwilhan.tugasfinal.utils.Constants.Companion.ACTION_START_SERVICE
import com.serwilhan.tugasfinal.utils.Constants.Companion.ALARM_LABEL
import com.serwilhan.tugasfinal.utils.Constants.Companion.ALARM_TIME
import com.serwilhan.tugasfinal.utils.Constants.Companion.CHANNEL_ID
import com.serwilhan.tugasfinal.utils.Constants.Companion.ACTION_STOP_SERVICE
import com.serwilhan.tugasfinal.utils.Constants.Companion.ALARM_ID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TAG = "alarmService"

class AlarmService : Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Thread {

            when (intent?.action) {
                // if the intent contains this action, stop the service
                ACTION_STOP_SERVICE -> stopSelf()
                ACTION_START_SERVICE -> {

                    val alarmName = intent.getStringExtra(ALARM_LABEL)
                    val alarmTime = intent.getStringExtra(ALARM_TIME)
                    val alarmId = intent.getLongExtra(ALARM_ID, 0)

                    val notification = createNotification(alarmName, alarmTime)

                    updateAlarmItem(alarmId)

                    startForeground(alarmId.toInt(), notification)
                    stopForeground(false)
                }
            }

        }.start()

        return START_NOT_STICKY
    }

    private fun updateAlarmItem(alarmId: Long) {
        val dao = AlarmDatabase.getDatabaseInstance(application).alarmDao()
        val repository = AlarmRepository(dao)

        CoroutineScope(Dispatchers.IO).launch {
            repository.updateScheduled(false, alarmId)
        }

    }

    private fun createNotification(
        alarmName: String?,
        alarmTime: String?
    ): Notification {

        // notification intent
        val notificationIntent = Intent(this, RingActivity::class.java).apply {
            putExtra(ALARM_LABEL, alarmName)
            putExtra(ALARM_TIME, alarmTime)
        }

        val (ringPendingIntent, deletePendingIntent) = notificationPendingIntents(
            notificationIntent
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(alarmName)
            .setContentText(alarmTime)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(ringPendingIntent)
            .setDeleteIntent(deletePendingIntent)
            .setAutoCancel(true)
            .build()
    }

    private fun notificationPendingIntents(
        notificationIntent: Intent
    ): Pair<PendingIntent,PendingIntent> {

        val ringPendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val deleteIntent = Intent(this, AlarmService::class.java).apply {
            action = ACTION_STOP_SERVICE
        }
        val deletePendingIntent = PendingIntent.getService(
            this,
            0,
            deleteIntent,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        return Pair(ringPendingIntent, deletePendingIntent)
    }

    override fun onBind(intent: Intent?): IBinder? = null
}