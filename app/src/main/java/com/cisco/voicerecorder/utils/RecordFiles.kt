package com.cisco.voicerecorder.utils

import java.io.File


class RecordFiles {

    private val directory: String =
        android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_MUSIC).path
    private var files: MutableList<File>? = null

    fun getAllRecords(): List<File>? {
        val directoryFiles: Array<File> = File(directory).listFiles() ?: return emptyList()

        files = mutableListOf()
        for (file in directoryFiles) {
            val fileName = file.name
            if (file.isFile && fileName.startsWith("record-") && fileName.endsWith(".mp3")) {
                files!!.add(file)
            }
        }

        return files
    }
}