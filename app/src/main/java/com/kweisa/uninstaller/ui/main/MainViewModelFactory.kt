package com.kweisa.uninstaller.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kweisa.uninstaller.data.AppRepository

class MainViewModelFactory(private val appRepository: AppRepository) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(appRepository) as T
    }
}