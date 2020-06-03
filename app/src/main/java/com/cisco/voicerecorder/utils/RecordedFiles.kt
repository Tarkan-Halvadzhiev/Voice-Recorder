package com.cisco.voicerecorder.utils

import java.io.File


object RecordedFiles {

    @JvmStatic
    private val directory: String = ExternalStorageDestination.getPath()

    @JvmStatic
    private var files: MutableList<File>? = null

    @JvmStatic
    fun getAllRecords(): MutableList<File>? {
        val directoryFiles: Array<File> = File(directory).listFiles() ?: return mutableListOf()

        files = mutableListOf()
        for (file in directoryFiles) {
            val fileName = file.name
            if (file.isFile && fileName.startsWith("record-") && fileName.endsWith("-voice.mp3")) {
                files!!.add(file)
            }
        }

        return files
    }

    @JvmStatic
    fun getFilesCounting(): MutableList<Int> {
        val directoryFiles: MutableList<File>? = getAllRecords()
        val fileCountingNumbers: MutableList<Int> = mutableListOf()
        var fileName: String = ""

        if (directoryFiles != null) {
            for (file in directoryFiles) {
                fileName = file.name
                fileName = fileName.replace("record-", "").replace("-voice.mp3", "")
                fileCountingNumbers.add(fileName.toInt())
            }
        }

        return fileCountingNumbers
    }

    @JvmStatic
    fun isContinuingNumberExist(number: Int): Boolean {
        return getFilesCounting().contains(number)
    }

    @JvmStatic
    fun generateFileCounting(): Int {
        var filesSize: Int = getAllRecords()?.size ?: 1
        while (true) {
            if (!isContinuingNumberExist(filesSize)) {
                return filesSize
            }
            filesSize++
        }
    }
}