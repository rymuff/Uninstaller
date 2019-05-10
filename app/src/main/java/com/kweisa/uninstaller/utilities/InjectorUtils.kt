package com.kweisa.uninstaller.utilities

import com.kweisa.uninstaller.data.AppDatabase
import com.kweisa.uninstaller.data.AppRepository
import com.kweisa.uninstaller.ui.main.MainViewModelFactory

object InjectorUtils {
    fun provideMainViewModelFactory(): MainViewModelFactory {
        val appRepository = AppRepository.getInstance(AppDatabase.getInstance().appDao)
        return MainViewModelFactory(appRepository)
    }
}