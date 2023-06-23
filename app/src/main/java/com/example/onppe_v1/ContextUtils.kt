package com.example.onppe_v1

import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import android.os.LocaleList
import java.util.*

class ContextUtils(base: Context) : ContextWrapper(base) {
    companion object {
        fun updateLocale(context: Context, localeToSwitchTo: Locale): ContextUtils {
            val resources = context.resources
            val configuration = resources.configuration
            val localeList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                LocaleList(localeToSwitchTo)
            } else {
                TODO("VERSION.SDK_INT < N")
            }
            LocaleList.setDefault(localeList)
            configuration.setLocales(localeList)
            val updatedContext = context.createConfigurationContext(configuration)
            return ContextUtils(updatedContext)
        }
    }
}