package com.kweisa.uninstaller.data

class AppDatabase private constructor() {
    var appDao = AppDao()
        private set

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: AppDatabase().also { instance = it }
        }
    }
}