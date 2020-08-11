package com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.models.BottomSheetDialogModel
import com.awkwardlydevelopedapps.unicharsheet.viewModels.EquipmentViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class EquipmentBottomSheetDialog(private val viewModel: EquipmentViewModel,
                                 private val slot: String,
                                 private val charId: Int) : BottomSheetDialogModel() {

    lateinit var editTextName: EditText
    lateinit var editTextType: EditText
    lateinit var editTextValue: EditText
    lateinit var editTextAdditionalEffects: EditText

    var oldName = ""
    var oldType = ""
    var oldValue = ""
    var oldAdditionalEffect = ""


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_equipment, container, false)

        editTextName = rootView.findViewById(R.id.editText_name)
        editTextName.setText(oldName)
        editTextType = rootView.findViewById(R.id.editText_type)
        editTextType.setText(oldType)
        editTextValue = rootView.findViewById(R.id.editText_value)
        editTextValue.setText(oldValue)
        editTextAdditionalEffects = rootView.findViewById(R.id.editText_additionalEffects)
        editTextAdditionalEffects.setText(oldAdditionalEffect)

        val fab: FloatingActionButton = rootView.findViewById(R.id.floatingActionButton)
        fab.setOnClickListener {
            updateEquipment()
        }

        return rootView
    }

    fun setOldValues(oldName: String,
                     oldType: String,
                     oldValue: String,
                     oldAdditionalEffect: String) {

        this.oldName = oldName
        this.oldType = oldType
        this.oldValue = oldValue
        this.oldAdditionalEffect = oldAdditionalEffect
    }

    private fun updateEquipment() {
        viewModel.updateEquipment(editTextName.text.toString(),
                editTextType.text.toString(),
                editTextValue.text.toString(),
                editTextAdditionalEffects.text.toString(),
                slot,
                charId)

        dialog?.dismiss()
    }
}