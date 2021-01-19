package com.androiddevs.mvvmnewsapp.util

import android.content.Context
import android.content.pm.PackageManager

class packagecheck {
    companion object {
        fun isApplicationInstalled(context: Context, uri: String): Boolean {
            val pm = context.packageManager
            return try {
                pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }
    }
}