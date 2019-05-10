package com.kweisa.uninstaller.data

import android.app.usage.StorageStatsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import java.util.*
import kotlin.collections.ArrayList

class AppSource private constructor() {
    private val calendar = Calendar.getInstance().apply { add(Calendar.YEAR, -1) }

    private lateinit var packageName: String

    private lateinit var packageManager: PackageManager
    private lateinit var storageStatsManager: StorageStatsManager
    private lateinit var usageStatsManager: UsageStatsManager

    fun init(context: Context) {
        packageName = context.packageName

        usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        packageManager = context.packageManager
        storageStatsManager = context.getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
    }

    fun update(): List<App> {
        val usageStatsMap = usageStatsManager.queryAndAggregateUsageStats(calendar.timeInMillis, System.currentTimeMillis())
        val applicationInfoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)

        val appList = ArrayList<App>()
        for (applicationInfo in applicationInfoList) {
            if (applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == 0 && applicationInfo.packageName != packageName) {
                val packageName = applicationInfo.packageName
                val icon = applicationInfo.loadIcon(packageManager)
                val label = applicationInfo.loadLabel(packageManager) as String
                val lastTimeUsed = usageStatsMap[packageName]?.lastTimeUsed ?: 0
                val size = storageStatsManager.queryStatsForUid(applicationInfo.storageUuid, applicationInfo.uid).run { appBytes + cacheBytes + dataBytes }
                appList.add(App(icon, label, lastTimeUsed, packageName, false, size))
            }
        }
        return appList
    }

    companion object {
        @Volatile
        private var instance: AppSource? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: AppSource().also { instance = it }
        }
    }
}