package com.awkwardlydevelopedapps.unicharsheet.inventory.pocket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.common.model.BottomSheetDialogModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PocketBottomSheetDialog() : BottomSheetDialogModel() {

    var option: Int = 0
    var currencyType: String? = ""

    var oldValue = "0"
    var oldMaxValue = "0"

    lateinit var editTextValue: EditText
    lateinit var editTextMaxValue: EditText

    var noticeDialogListener: NoticeDialogListener? = null

    interface NoticeDialogListener {
        fun onPositiveClickListener(option: Int, updateValue: String, updateMaxValue: String, currencyType: String?)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_pocket_fragment, container, false)
        editTextValue = rootView.findViewById(R.id.pocketDialog_editText_value)
        editTextValue.setText(oldValue)
        editTextMaxValue = rootView.findViewById(R.id.pocketDialog_editText_maxValue)
        editTextMaxValue.setText(oldMaxValue)
        val textViewMaxVal = rootView.findViewById<TextView>(R.id.pocketDialog_textView_maxValue)
        val fabApply: FloatingActionButton = rootView.findViewById(R.id.pocketDialog_fab_apply)
        fabApply.setOnClickListener {
            noticeDialogListener?.onPositiveClickListener(
                    option,
                    getUpdateValue(),
                    getUpdateMaxValue(),
                    currencyType
            )
            dialog?.dismiss()
        }

        if (option == LEVEL) {
            textViewMaxVal.visibility = View.GONE
            editTextMaxValue.visibility = View.GONE
        }

        editTextValue.requestFocus()


        return rootView
    }

    private fun getUpdateValue(): String {
        var updateValue = "0"
        if (editTextValue.text.isNotEmpty()) {
            updateValue = editTextValue.text.toString()
        }

        return updateValue
    }

    private fun getUpdateMaxValue(): String {
        var updateMaxValue = "0"
        if (editTextMaxValue.text.isNotEmpty()) {
            updateMaxValue = editTextMaxValue.text.toString()
        }

        return updateMaxValue
    }

    companion object {
        const val CURRENCY = 0
        const val EXPERIENCE = 1
        const val LEVEL = 2
    }


}