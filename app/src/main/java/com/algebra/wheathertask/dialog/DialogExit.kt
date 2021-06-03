package com.algebra.wheathertask.dialog

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.algebra.wheathertask.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class DialogExit(private val title: String): DialogFragment() {

    var listener: ListenerForDialog? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return MaterialAlertDialogBuilder(requireActivity(), R.style.MaterialAlertDialog_OK_color).setView(view)
            .setMessage(title)
            .setPositiveButton("Ok") { dialog, which ->
                listener?.okPress(true)
            }.setNegativeButton("Cancel") { dialog, which ->
                getDialog()?.cancel()
            }.create()
    }

    interface ListenerForDialog{
        fun okPress(isPress: Boolean)
    }

}