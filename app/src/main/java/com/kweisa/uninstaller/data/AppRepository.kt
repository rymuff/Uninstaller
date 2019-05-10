package com.kweisa.uninstaller.data

import android.content.Context

class AppRepository private constructor(private val appDao: AppDao) {
    private var appSource: AppSource = AppSource.getInstance()

    fun addApp(app: App) = appDao.addApp(app)
    fun removeApp(packageName: String) = appDao.removeApp(packageName)
    fun getApps() = appDao.getApps()
    fun setCheckedAll(boolean: Boolean) = appDao.setCheckedAll(boolean)
    fun getCheckedAll() = appDao.getCheckedAll()
    fun update(context: Context) = appSource.apply { init(context) }.update().forEach { addApp(it) }

    companion object {
        @Volatile
        private var instance: AppRepository? = null

        fun getInstance(appDao: AppDao) = instance ?: synchronized(this) {
            instance ?: AppRepository(appDao).also { instance = it }
        }
    }
}