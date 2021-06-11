package com.serwilhan.tugasfinal.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.serwilhan.tugasfinal.R
import com.serwilhan.tugasfinal.data.database.AlarmDatabase
import com.serwilhan.tugasfinal.data.models.AlarmItem
import com.serwilhan.tugasfinal.data.repository.AlarmRepository
import com.serwilhan.tugasfinal.databinding.FragmentSetAlarmBinding
import com.serwilhan.tugasfinal.ui.AlarmViewModelFactory
import com.serwilhan.tugasfinal.ui.viewmodels.AlarmViewModel
import com.serwilhan.tugasfinal.utils.AlarmHelper
import com.serwilhan.tugasfinal.utils.CalendarUtil
import com.serwilhan.tugasfinal.utils.TimePickerUtil
import java.util.*
import kotlin.random.Random

private const val TAG = "setAlarmFragment"

class SetAlarmFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentSetAlarmBinding
    private lateinit var viewModel: AlarmViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_set_alarm, container, false)

        val application = requireNotNull(this.activity).application
        val dataDao = AlarmDatabase.getDatabaseInstance(application).alarmDao()
        val repository = AlarmRepository(dataDao)
        val viewModelFactory = AlarmViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(AlarmViewModel::class.java)
        binding.btnSetNewAlarm.setOnClickListener(this)
        binding.btnCancelAndClose.setOnClickListener(this)

        return binding.root
    }

    override fun onClick(v: View?) {

        when (v) {
            binding.btnSetNewAlarm -> setAlarm()
            binding.btnCancelAndClose -> cancelAndClose()
        }
    }

    private fun setAlarm() {
        val alarmHelper = AlarmHelper(requireContext())
        val label = binding.setAlarmLabel.text.toString()
        val (hour, minute) = TimePickerUtil.getTime(binding.itemTimePicker)
        val alarmTimeString = CalendarUtil.formatCalendarTime(hour, minute)
        val alarmDate = CalendarUtil.setCalendar(hour, minute, 0)
        if (alarmDate.before(Calendar.getInstance())) alarmDate.add(Calendar.DATE, 1)
        val alarmDay = CalendarUtil.getAlarmDay(alarmDate)

        val alarmId = Random.nextInt(Int.MAX_VALUE).toLong()

        val alarmItem = AlarmItem(
            alarmId,
            label,
            hour,
            minute,
            alarmDay,
            true,
            System.currentTimeMillis()
        )

        alarmHelper.scheduleAlarm(alarmItem)
        viewModel.insertAlarm(alarmItem)
        findNavController().navigate(R.id.action_setAlarmFragment_to_mainFragment)

    }
    
    private fun cancelAndClose() =
        findNavController().navigate(R.id.action_setAlarmFragment_to_mainFragment)
}