package com.cisco.voicerecorder.utils

import android.app.AlertDialog
import android.content.Context
import com.cisco.voicerecorder.R

object DialogMessageBuilder {

    @JvmStatic
    fun messageBuilding(context: Context, message: Int, title: Int) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage(message)
            .setCancelable(false)
            .setPositiveButton(R.string.positive_button_text) { dialog, _ ->
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.setTitle(title)
        alert.show()
    }
}