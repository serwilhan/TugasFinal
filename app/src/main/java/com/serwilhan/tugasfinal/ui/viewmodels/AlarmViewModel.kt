package com.serwilhan.tugasfinal.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serwilhan.tugasfinal.data.database.AlarmDatabase
import com.serwilhan.tugasfinal.data.models.AlarmItem
import com.serwilhan.tugasfinal.data.repository.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TAG = "viewModel"

class AlarmViewModel(private val repository: AlarmRepository) : ViewModel() {

    fun alarms() = repository.alarmsList()

    fun getAlarm(hour: Int, minute: Int, alarmDay: String) =
        repository.getAlarm(hour, minute, alarmDay)

    fun insertAlarm(alarmItem: AlarmItem) {
        // do the work in background thread with coroutines
        viewModelScope.launch(Dispatchers.IO) {
            repository.insert(alarmItem)
        }
    }

    fun updateAlarm(alarmItem: AlarmItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(alarmItem)
        }
    }

    fun updateScheduled(isScheduled: Boolean, id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateScheduled(isScheduled, id)
        }
    }

    fun deleteAlarm(alarmItem: AlarmItem) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(alarmItem)
        }
    }

    fun deleteAllAlarms() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllAlarms()
        }
    }

}