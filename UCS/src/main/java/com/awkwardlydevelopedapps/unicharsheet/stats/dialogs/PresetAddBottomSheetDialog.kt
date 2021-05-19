package com.awkwardlydevelopedapps.unicharsheet.stats.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.common.model.BottomSheetDialogModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PresetAddBottomSheetDialog() : BottomSheetDialogModel() {

    lateinit var listener: OnApplyListener

    interface OnApplyListener {
        fun applyPreset(presetName: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_preset, container, false)
        val editTextName: EditText = rootView.findViewById(R.id.presetDialog_editText_name)
        val fabApply: FloatingActionButton = rootView.findViewById(R.id.presetDialog_fab_apply)
        fabApply.setOnClickListener {
            if (editTextName.text.isEmpty()) {
                Toast.makeText(requireContext(), "Enter a name", Toast.LENGTH_SHORT).show()
            } else {
                listener.applyPreset(editTextName.text.toString())
                dialog?.dismiss()
            }
        }

        editTextName.requestFocus()

        return rootView
    }


}