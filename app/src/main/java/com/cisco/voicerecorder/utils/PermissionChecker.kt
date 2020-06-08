package com.cisco.voicerecorder.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object PermissionChecker {

    @JvmStatic
    fun permissionChecking(
        context: Context,
        recordAudio: String,
        writeExternalStorage: String,
        readExternalStorage: String
    ): Boolean {
        return (ContextCompat.checkSelfPermission(context, recordAudio)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, writeExternalStorage)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, readExternalStorage)
                != PackageManager.PERMISSION_GRANTED)
    }
}