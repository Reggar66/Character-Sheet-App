package com.awkwardlydevelopedapps.unicharsheet.inventory.pocket

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.common.model.BottomSheetDialogModel
import com.awkwardlydevelopedapps.unicharsheet.common.utils.LogWrapper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PocketBottomSheetDialog() : BottomSheetDialogModel() {

    var option: Int = 0
    var currencyType: String? = null

    var oldValue: String? = "0"
    var oldMaxValue: String? = "0"

    lateinit var editTextValue: EditText
    lateinit var editTextMaxValue: EditText

    private var noticeDialogListener: NoticeDialogListener? = null

    interface NoticeDialogListener {
        fun onPositiveClickListener(
            option: Int,
            updateValue: String,
            updateMaxValue: String,
            currencyType: String?
        )
    }

    companion object {
        const val CURRENCY = 0
        const val EXPERIENCE = 1
        const val LEVEL = 2

        private const val KEY_OPTION = "OPTION"
        private const val KEY_CURRENCY_TYPE = "CURRENCY_TYPE"
        private const val KEY_OLD_VALUE = "OLD_VALUE"
        private const val KEY_OLD_MAX_VALUE = "OLD_MAX_VALUE"

        fun newInstance(
            option: Int,
            currencyType: String?,
            oldValue: String?,
            oldMaxValue: String?
        ): PocketBottomSheetDialog {
            return PocketBottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putInt(KEY_OPTION, option)
                    currencyType?.let {
                        putString(KEY_CURRENCY_TYPE, it)
                        LogWrapper.v("INFO", "PocketBottomSheetDialog: bundled currency type.")
                    }
                    oldValue?.let {
                        putString(KEY_OLD_VALUE, it)
                        LogWrapper.v("INFO", "PocketBottomSheetDialog: bundled old value.")
                    }
                    oldMaxValue?.let {
                        putString(KEY_OLD_MAX_VALUE, it)
                        LogWrapper.v("INFO", "PocketBottomSheetDialog: bundled old max value.")
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            noticeDialogListener = parentFragment as NoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(parentFragment.toString() + " must implement PocketBottomSheetDialog.NoticeDialogListener.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.option = arguments?.getInt(KEY_OPTION)!!

        when (option) {
            CURRENCY -> {
                this.currencyType = arguments?.getString(KEY_CURRENCY_TYPE)
                this.oldValue = arguments?.getString(KEY_OLD_VALUE)
                this.oldMaxValue = arguments?.getString(KEY_OLD_MAX_VALUE)
            }
            EXPERIENCE -> {
                this.oldValue = arguments?.getString(KEY_OLD_VALUE)
                this.oldMaxValue = arguments?.getString(KEY_OLD_MAX_VALUE)
            }
            LEVEL -> {
                this.oldValue = arguments?.getString(KEY_OLD_VALUE)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
}