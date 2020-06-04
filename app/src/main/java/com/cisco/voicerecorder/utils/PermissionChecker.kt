package com.cisco.voicerecorder.utils

import android.app.AlertDialog
import android.content.Context

object PermissionChecker {

    @JvmStatic
    fun permissionMessage(context: Context) {
        val dialogBuilder = AlertDialog.Builder(context)
        dialogBuilder.setMessage("Go to setting to give permission to use this application.")
            .setCancelable(false)
            .setPositiveButton("Ok") { dialog, _ ->
                dialog.dismiss()
            }

        val alert = dialogBuilder.create()
        alert.setTitle("Alert")
        alert.show()
    }
}