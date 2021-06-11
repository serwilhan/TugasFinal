package com.serwilhan.tugasfinal.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.serwilhan.tugasfinal.data.repository.AlarmRepository
import com.serwilhan.tugasfinal.ui.viewmodels.AlarmViewModel

class AlarmViewModelFactory(private val repository: AlarmRepository) :
    ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AlarmViewModel::class.java)) {
            return AlarmViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
