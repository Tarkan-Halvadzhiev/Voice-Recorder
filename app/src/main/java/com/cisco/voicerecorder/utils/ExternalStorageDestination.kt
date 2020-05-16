package com.cisco.voicerecorder.utils

import android.os.Environment

object ExternalStorageDestination {

    @JvmStatic
    private val file: String =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).path

    fun getPath(): String = file
}