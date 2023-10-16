package com.thatwaz.shoppuppet.presentation.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.thatwaz.shoppuppet.R

class CustomIconDialogFragment : DialogFragment() {


    interface CustomIconDialogListener {
        fun onIconTextEntered(text: String)
    }

    private var listener: CustomIconDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // If the dialog is shown from an activity
        if (context is CustomIconDialogListener) {
            listener = context
        }
        // If the dialog is shown from a fragment
        else if (parentFragment is CustomIconDialogListener) {
            listener = parentFragment as CustomIconDialogListener
        }
        else {
            throw ClassCastException("$context must implement CustomIconDialogListener")
        }
    }



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_custom_icon, null)

            builder.setView(view)
                .setTitle("Custom Shop Icon")
                .setPositiveButton("OK") { _, _ ->
                    val editText = view.findViewById<EditText>(R.id.et_custom_icon)
                    listener?.onIconTextEntered(editText.text.toString())
                }
                .setNegativeButton("Cancel") { _, _ -> dismiss() }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
