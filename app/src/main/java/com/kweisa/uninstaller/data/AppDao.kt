package com.kweisa.uninstaller.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class AppDao {
    private val appList = mutableListOf<App>()
    private val apps = MutableLiveData<List<App>>()

    init {
        apps.value = appList
    }

    fun addApp(app: App) {
        appList.removeIf { t -> t.packageName == app.packageName }
        appList.add(app)
        apps.value = appList
    }

    fun removeApp(packageName: String) {
        appList.removeIf { app -> app.packageName == packageName }
        apps.value = appList
    }

    fun getApps() = apps as LiveData<List<App>>

    fun getSortedApps() = apps.apply { value = appList.apply { sortBy { it.lastTimeUsed } } } as LiveData<List<App>>

    fun setCheckedAll(boolean: Boolean) {
        appList.forEach { app -> if (boolean != app.isChecked) app.isChecked = boolean }
        apps.value = appList
    }

    fun getCheckedAll() = appList.filter { app -> app.isChecked }

}