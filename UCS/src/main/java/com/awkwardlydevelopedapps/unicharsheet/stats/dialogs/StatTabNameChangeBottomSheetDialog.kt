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

class StatTabNameChangeBottomSheetDialog : BottomSheetDialogModel() {

    lateinit var listener: OnApplyStatTabNameListener

    interface OnApplyStatTabNameListener {
        fun applyStatTabName(tabName: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_stat_tab_name_change, container, false)
        val editTextName: EditText = rootView.findViewById(R.id.statTabNameChangeDialog_editText_name)
        val fabApply: FloatingActionButton = rootView.findViewById(R.id.statTabNameChangeDialog_fab_apply)

        editTextName.requestFocus()

        fabApply.setOnClickListener {
            if (editTextName.text.isEmpty()) {
                Toast.makeText(requireContext(), "Enter a name", Toast.LENGTH_SHORT).show()
            } else {
                listener.applyStatTabName(editTextName.text.toString())
                dialog?.dismiss()
            }
        }


        return rootView
    }
}