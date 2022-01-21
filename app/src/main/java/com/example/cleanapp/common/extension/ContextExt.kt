package com.example.cleanapp.common.extension

import android.app.AlertDialog
import android.content.Context
import android.widget.Toast

fun Context.showToast(message: String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showGenericAlertDialog(message: String){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("OK"){d,_ ->
            d.cancel()
        }
    }.show()
}