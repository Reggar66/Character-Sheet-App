package com.awkwardlydevelopedapps.unicharsheet.inventory.equipment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.common.model.BottomSheetDialogModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EquipmentBottomSheetDialog(private val mContext: Context,
                                 private val slot: String
) : BottomSheetDialogModel() {

    lateinit var editTextName: EditText
    lateinit var editTextType: EditText
    lateinit var editTextValue: EditText
    lateinit var editTextAdditionalEffects: EditText

    var oldName = ""
    var oldType = ""
    var oldValue = ""
    var oldAdditionalEffect = ""

    var noticeDialogListener: NoticeDialogListener? = null

    interface NoticeDialogListener {
        fun onPositiveClickListener(name: String, type: String, value: String, additionalEffect: String, slot: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
            //updateEquipment()
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

    fun setOldValues(oldName: String,
                     oldType: String,
                     oldValue: String,
                     oldAdditionalEffect: String) {
        if (oldName != mContext.resources.getString(R.string.eq_no_item_name))
            this.oldName = oldName

        if (oldType != mContext.resources.getString(R.string.eq_type_none))
            this.oldType = oldType

        if (oldValue != "0")
            this.oldValue = oldValue

        if (oldAdditionalEffect != mContext.resources.getString(R.string.eq_no_additional_effects))
            this.oldAdditionalEffect = oldAdditionalEffect
    }
}