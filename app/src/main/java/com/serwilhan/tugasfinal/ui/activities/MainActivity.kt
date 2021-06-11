package com.serwilhan.tugasfinal.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.serwilhan.tugasfinal.R
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController


private const val TAG = "mainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupActionBarWithNavController(findNavController(R.id.nav_host_fragment))
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.nav_host_fragment).navigateUp() || super.onSupportNavigateUp()
}