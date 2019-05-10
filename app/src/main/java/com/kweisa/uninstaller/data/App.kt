package com.kweisa.uninstaller.data

import android.graphics.drawable.Drawable

data class App(
        val icon: Drawable,
        val label: String,
        val lastTimeUsed: Long,
        val packageName: String,
        var isChecked: Boolean,
        val size: Long
)