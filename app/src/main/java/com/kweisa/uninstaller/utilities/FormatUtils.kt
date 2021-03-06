package com.kweisa.uninstaller.utilities

import com.kweisa.uninstaller.data.App
import com.kweisa.uninstaller.data.AppItem
import java.text.DateFormat
import java.util.*
import kotlin.collections.ArrayList

object FormatUtils {
    fun dateTimeFormat(lastTimeUsed: Long) = if (lastTimeUsed == 0L) " never" else " " + DateFormat.getDateTimeInstance().format(Date(lastTimeUsed))
    fun toAppItems(apps: List<App>) = ArrayList<AppItem>().apply { apps.forEach { add(AppItem(it)) } } as List<AppItem>
    fun mbFormat(size: Long) = "Size: %.2f MB".format(size / 1024.0 / 1024)
}
