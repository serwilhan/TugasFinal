package com.serwilhan.tugasfinal.ui.interfaces

import android.view.MenuItem
import com.serwilhan.tugasfinal.data.models.AlarmItem

interface AlarmViewsOnClickListener {
    fun onSwitchToggle(position: Int)
    fun onOptionsMenuItemClicked(position: Int, menuItem: MenuItem)
}