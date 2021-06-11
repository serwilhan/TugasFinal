package com.serwilhan.tugasfinal.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.serwilhan.tugasfinal.R
import com.serwilhan.tugasfinal.services.AlarmService
import com.serwilhan.tugasfinal.databinding.ActivityRingBinding
import com.serwilhan.tugasfinal.utils.Constants.Companion.ALARM_LABEL
import com.serwilhan.tugasfinal.utils.Constants.Companion.ALARM_TIME

private const val TAG = "ringActivity"

class RingActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityRingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ring)

        val alarmName = intent.getStringExtra(ALARM_LABEL)
        val alarmTime = intent.getStringExtra(ALARM_TIME)

        binding.tvRingAlarmName.text = alarmName
        binding.tvRingTime.text = alarmTime

        binding.btnRingDismiss.setOnClickListener(this)
        binding.btnRingSnooze.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v) {
            binding.btnRingDismiss -> {

                val intent = Intent(this, AlarmService::class.java)
                stopService(intent)

                val startMain = Intent(this, MainActivity::class.java)
                startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(startMain)

                finish()
            }
        }
    }
}