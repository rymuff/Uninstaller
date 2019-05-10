package com.kweisa.uninstaller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import com.kweisa.uninstaller.data.AppDatabase
import com.kweisa.uninstaller.data.AppRepository

class PackageReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val appRepository = AppRepository.getInstance(AppDatabase.getInstance().appDao)

        if (intent.action == Intent.ACTION_PACKAGE_REMOVED) {
            intent.dataString?.let { appRepository.removeApp(it.removePrefix("package:")) }
        }
    }
}

val intentFilter = IntentFilter(Intent.ACTION_PACKAGE_REMOVED).apply { addDataScheme("package") }