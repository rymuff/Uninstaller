package com.kweisa.uninstaller.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import com.kweisa.uninstaller.data.App
import com.kweisa.uninstaller.data.AppRepository

class MainViewModel(private val appRepository: AppRepository) : ViewModel() {
    fun getApps() = appRepository.getApps()
    fun addApp(app: App) = appRepository.addApp(app)
    fun setCheckedAll(boolean: Boolean) = appRepository.setCheckedAll(boolean)
    fun getCheckedAll() = appRepository.getCheckedAll()
    fun update(context: Context) = appRepository.update(context)
}