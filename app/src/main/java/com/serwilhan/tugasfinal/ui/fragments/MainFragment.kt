package com.serwilhan.tugasfinal.ui.fragments

import android.app.AlarmManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.serwilhan.tugasfinal.R
import com.serwilhan.tugasfinal.data.database.AlarmDatabase
import com.serwilhan.tugasfinal.data.models.AlarmItem
import com.serwilhan.tugasfinal.data.repository.AlarmRepository
import com.serwilhan.tugasfinal.databinding.FragmentMainBinding
import com.serwilhan.tugasfinal.ui.AlarmViewModelFactory
import com.serwilhan.tugasfinal.ui.adapters.AlarmsListAdapter
import com.serwilhan.tugasfinal.ui.interfaces.AlarmViewsOnClickListener
import com.serwilhan.tugasfinal.ui.viewmodels.AlarmViewModel
import com.serwilhan.tugasfinal.utils.AlarmHelper
import com.serwilhan.tugasfinal.utils.DialogHelper
import com.serwilhan.tugasfinal.utils.Messages

private const val TAG = "mainFragment"

class MainFragment : Fragment(), AlarmViewsOnClickListener {

    private lateinit var binding: FragmentMainBinding
    private lateinit var viewModel: AlarmViewModel
    private var alarmList = mutableListOf<AlarmItem>()
    private lateinit var alarmManager: AlarmManager
    private lateinit var adapter: AlarmsListAdapter
    private lateinit var rvMain: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)


        val application = requireNotNull(this.activity).application
        val dataDao = AlarmDatabase.getDatabaseInstance(application).alarmDao()
        val repository = AlarmRepository(dataDao)
        val viewModelFactory = AlarmViewModelFactory(repository)

        viewModel = ViewModelProvider(this, viewModelFactory).get(AlarmViewModel::class.java)
        alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        adapter = AlarmsListAdapter(requireContext(), this)
        rvMain = binding.rvMainFragment
        rvMain.layoutManager = LinearLayoutManager(context)
        rvMain.adapter = adapter

        viewModel.alarms().observe(viewLifecycleOwner, { listOfAlarms ->

            this.alarmList = listOfAlarms.toMutableList()
            adapter.submitList(listOfAlarms)

            if (listOfAlarms.isEmpty()) binding.tvNoAlarms.visibility = View.VISIBLE
            else binding.tvNoAlarms.visibility = View.GONE

        })

        binding.fabAddNewAlarm.setOnClickListener { btn ->
            btn.findNavController().navigate(R.id.action_mainFragment_to_setAlarmFragment)
        }


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onSwitchToggle(position: Int) {
        val alarmItem = alarmList[position]

        val alarmHelper = AlarmHelper(requireContext())


        if (alarmItem.isScheduled) {

            val updatedAlarm = alarmHelper.cancelAlarm(alarmItem)

            viewModel.updateAlarm(updatedAlarm)

        } else {

            val updatedAlarm = alarmHelper.scheduleAlarm(alarmItem)

            viewModel.updateAlarm(updatedAlarm)
            rvMain.post { adapter.notifyItemChanged(position) }

        }
    }

    override fun onOptionsMenuItemClicked(position: Int, menuItem: MenuItem) {
        val alarmItem = alarmList[position]

        val alarmHelper = AlarmHelper(requireContext())

        when (menuItem.itemId) {
            R.id.delete_alarm -> {

                if (alarmItem.isScheduled) alarmHelper.cancelAlarm(alarmItem)

                viewModel.deleteAlarm(alarmItem)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.action_bar_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_all_alarm -> {

                if (alarmList.isNotEmpty()) {
                    DialogHelper.showDialog(requireContext(), object : DialogHelper.DialogInterface {
                        override fun getRespond(respond: Int) {
                            if (respond == 1) {
                                val alarmHelper = AlarmHelper(requireContext())
                                alarmList.forEach {
                                    if (it.isScheduled) alarmHelper.cancelAlarm(it)
                                }

                                viewModel.deleteAllAlarms()
                            }
                        }
                    })
                } else {
                    Messages.showSnack(binding.root, "List is empty")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}