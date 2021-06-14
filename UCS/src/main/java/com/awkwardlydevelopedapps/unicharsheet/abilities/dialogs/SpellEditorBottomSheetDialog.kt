package com.awkwardlydevelopedapps.unicharsheet.abilities.dialogs

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.common.model.BottomSheetDialogModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SpellEditorBottomSheetDialog : BottomSheetDialogModel() {

    private var option: Int? = null
    private var oldValue: String = ""
    private lateinit var editText: EditText
    private var noticeDialogListener: NoticeDialogListener? = null

    interface NoticeDialogListener {
        fun onPositiveDialogListener(
            dialog: DialogFragment,
            option: Int,
            valueToUpdate: String
        )
    }

    companion object {
        const val DESCRIPTION = 0
        const val DAMAGE_VALUE = 1
        const val DAMAGE_ADD_EFFECT = 2
        const val COST_VALUE = 3
        const val COST_ADD_VALUE = 4
        const val SPECIAL_NOTES = 5

        private const val KEY_OPTION = "OPTION"
        private const val KEY_OLD_VALUE = "OLD_VALUE"

        fun newInstance(option: Int, oldValue: String): SpellEditorBottomSheetDialog {
            return SpellEditorBottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putInt(KEY_OPTION, option)
                    putString(KEY_OLD_VALUE, oldValue)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        option = arguments?.getInt(KEY_OPTION)
        oldValue = arguments?.getString(KEY_OLD_VALUE).toString()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            noticeDialogListener = parentFragment as NoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                parentFragment.toString()
                        + " must implement SpellEditorBottomSheetDialog.NoticeDialogListener"
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.dialog_spell_editor, container, false)

        editText = rootView.findViewById(R.id.dialogSpellEditor_editText_value)
        val textView: TextView = rootView.findViewById(R.id.dialogSpellEditor_textView_description)
        val fabApply: FloatingActionButton = rootView.findViewById(R.id.dialogSpellEditor_fab_apply)
        fabApply.setOnClickListener {
            option?.let { option ->
                noticeDialogListener?.onPositiveDialogListener(
                    this,
                    option, editText.text.toString()
                )
            }
            dialog?.dismiss()
        }

        when (option) {
            DESCRIPTION -> {
                textView.setText(R.string.enter_description)
                editText.inputType = InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_FLAG_MULTI_LINE
            }
            DAMAGE_VALUE, COST_VALUE -> {
                textView.setText(R.string.enter_value)
                editText.inputType = InputType.TYPE_NUMBER_FLAG_SIGNED +
                        InputType.TYPE_NUMBER_FLAG_DECIMAL +
                        InputType.TYPE_CLASS_NUMBER
            }
            DAMAGE_ADD_EFFECT -> {
                textView.setText(R.string.enter_additional_effect)
                editText.inputType = InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_FLAG_MULTI_LINE
            }
            COST_ADD_VALUE -> {
                textView.setText(R.string.enter_additional_cost)
                editText.inputType = InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_FLAG_MULTI_LINE
            }
            SPECIAL_NOTES -> {
                textView.setText(R.string.enter_special_notes)
                editText.inputType = InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_FLAG_MULTI_LINE
            }

        }

        editText.setText(oldValue)
        editText.requestFocus()

        return rootView
    }
}