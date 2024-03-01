package com.thatwaz.shoppuppet.presentation.fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.thatwaz.shoppuppet.R

class CustomIconDialogFragment : DialogFragment() {
    interface CustomIconDialogListener {
        fun onIconTextEntered(text: String)
    }

    private var listener: CustomIconDialogListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // Assign the listener based on whether parentFragment or context implements CustomIconDialogListener
        listener =
            parentFragment as? CustomIconDialogListener ?: context as? CustomIconDialogListener

        // Check if listener is still null (neither parentFragment nor context implements the interface)
        if (listener == null) {
            throw ClassCastException("$context must implement CustomIconDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = MaterialAlertDialogBuilder(requireContext(), R.style.CustomAlertDialog)

            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_custom_icon, null)
            val editText = view.findViewById<EditText>(R.id.et_custom_icon)

            builder.setView(view)
                .setTitle("Custom Shop Icon Using Letters")
                .setPositiveButton("OK", null) // Set to null here
                .setNegativeButton("Cancel") { _, _ -> dismiss() }

            val dialog = builder.create()

            dialog.setOnShowListener {
                val buttonOk = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                buttonOk.setOnClickListener {
                    val text = editText.text.toString()
                    if (text.isBlank()) {
                        // Show a Snackbar message if no text is entered
                        Snackbar.make(view, "Input required. Please enter valid letters or select 'Cancel' to return.", Snackbar.LENGTH_LONG).show()
                    } else {
                        // If text is entered, use the listener to handle the text and dismiss the dialog
                        listener?.onIconTextEntered(text)
                        dialog.dismiss()
                    }
                }
            }

            dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}

