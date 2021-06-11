package com.awkwardlydevelopedapps.unicharsheet.inventory.equipment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.common.model.BottomSheetDialogModel
import com.awkwardlydevelopedapps.unicharsheet.common.utils.LogWrapper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EquipmentBottomSheetDialog : BottomSheetDialogModel() {

    lateinit var editTextName: EditText
    lateinit var editTextType: EditText
    lateinit var editTextValue: EditText
    lateinit var editTextAdditionalEffects: EditText

    private var slot: String = ""

    var oldName = ""
    var oldType = ""
    var oldValue = ""
    var oldAdditionalEffect = ""

    private var noticeDialogListener: NoticeDialogListener? = null

    interface NoticeDialogListener {
        fun onPositiveClickListener(
            name: String,
            type: String,
            value: String,
            additionalEffect: String,
            slot: String
        )
    }

    companion object {
        private const val KEY_SLOT = "SLOT"
        private const val KEY_OLD_NAME = "OLD_NAME"
        private const val KEY_OLD_TYPE = "OLD_TYPE"
        private const val KEY_OLD_VALUE = "OLD_VALUE"
        private const val KEY_OLD_ADD_EFFECT = "OLD_ADD_EFFECT"

        fun newInstance(
            slot: String,
            oldName: String,
            oldType: String,
            oldValue: String,
            oldAdditionalEffect: String
        ): EquipmentBottomSheetDialog {
            return EquipmentBottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putString(KEY_SLOT, slot)
                    putString(KEY_OLD_NAME, oldName)
                    putString(KEY_OLD_TYPE, oldType)
                    putString(KEY_OLD_VALUE, oldValue)
                    putString(KEY_OLD_ADD_EFFECT, oldAdditionalEffect)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            noticeDialogListener = parentFragment as NoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                parentFragment.toString()
                        + " must implement EquipmentBottomSheetDialog.NoticeDialogListener."
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.slot = arguments?.getString(KEY_SLOT).toString()
        this.oldName = arguments?.getString(KEY_OLD_NAME).toString()
        this.oldType = arguments?.getString(KEY_OLD_TYPE).toString()
        this.oldValue = arguments?.getString(KEY_OLD_VALUE).toString()
        this.oldAdditionalEffect = arguments?.getString(KEY_OLD_ADD_EFFECT).toString()

        LogWrapper.v(
            "INFO",
            "EquipmentDialog: Slot=$slot, oldName=$oldName," +
                    " oldType=$oldType, oldVal=$oldValue, oldAddEff=$oldAdditionalEffect"
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.dialog_equipment, container, false)

        editTextName = rootView.findViewById(R.id.editText_name)
        editTextName.setText(oldName)
        editTextName.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b)
                editTextName.setSelection(editTextName.length())

        }

        editTextType = rootView.findViewById(R.id.editText_type)
        editTextType.setText(oldType)
        editTextType.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b)
                editTextType.setSelection(editTextType.length())
        }


        editTextValue = rootView.findViewById(R.id.editText_value)
        editTextValue.setText(oldValue)
        editTextValue.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b)
                editTextValue.setSelection(editTextValue.length())

        }

        editTextAdditionalEffects = rootView.findViewById(R.id.editText_additionalEffects)
        editTextAdditionalEffects.setText(oldAdditionalEffect)
        editTextAdditionalEffects.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b)
                editTextAdditionalEffects.setSelection(editTextAdditionalEffects.length())
        }


        val fab: FloatingActionButton = rootView.findViewById(R.id.floatingActionButton)
        fab.setOnClickListener {
            noticeDialogListener?.onPositiveClickListener(
                editTextName.text.toString(),
                editTextType.text.toString(),
                editTextValue.text.toString(),
                editTextAdditionalEffects.text.toString(),
                slot
            )
            dismiss()
        }
        return rootView
    }
}