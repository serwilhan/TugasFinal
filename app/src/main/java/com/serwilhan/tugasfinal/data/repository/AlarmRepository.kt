package com.serwilhan.tugasfinal.data.repository

import com.serwilhan.tugasfinal.data.database.AlarmDao
import com.serwilhan.tugasfinal.data.models.AlarmItem

class AlarmRepository(private var alarmDao: AlarmDao) {

    fun alarmsList() = alarmDao.getAllAlarms()

    fun getAlarm(hour: Int, minute: Int, alarmDay: String) = alarmDao.getAlarm(hour, minute, alarmDay)

    suspend fun insert(alarmItem: AlarmItem) = alarmDao.insert(alarmItem)

    suspend fun update(alarmItem: AlarmItem) = alarmDao.update(alarmItem)

    suspend fun updateScheduled(isScheduled: Boolean, id: Long) = alarmDao.updateScheduled(isScheduled, id)

    suspend fun delete(alarmItem: AlarmItem) = alarmDao.delete(alarmItem)

    suspend fun deleteAllAlarms() = alarmDao.deleteAllAlarms()


}