package com.kweisa.uninstaller

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.kweisa.uninstaller.data.AppDatabase
import com.kweisa.uninstaller.data.AppRepository

// TODO: change receiver name
class MyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val appRepository = AppRepository.getInstance(AppDatabase.getInstance().appDao)

        if (intent.action == Intent.ACTION_PACKAGE_REMOVED) {
            intent.dataString?.let { appRepository.removeApp(it.removePrefix("package:")) }
        }
    }
}

