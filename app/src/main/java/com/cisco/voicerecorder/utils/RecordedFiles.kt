package com.cisco.voicerecorder.utils

import java.io.File


object RecordedFiles {

    @JvmStatic
    private val directory: String = ExternalStorageDestination.getPath()
    @JvmStatic
    private var files: MutableList<File>? = null

    @JvmStatic
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