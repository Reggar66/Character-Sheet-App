package com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.models.BottomSheetDialogModel
import com.awkwardlydevelopedapps.unicharsheet.viewModels.PocketViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PocketBottomSheetDialog(private val viewModel: PocketViewModel,
                              private val charId: Int) : BottomSheetDialogModel() {

    var option: Int = 0
    var currencyType: String? = ""

    var oldValue = "0"
    var oldMaxValue = "0"

    lateinit var editTextValue: EditText
    lateinit var editTextMaxValue: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_pocket_fragment, container, false)
        editTextValue = rootView.findViewById(R.id.pocketDialog_editText_value)
        editTextValue.setText(oldValue)
        editTextMaxValue = rootView.findViewById(R.id.pocketDialog_editText_maxValue)
        editTextMaxValue.setText(oldMaxValue)
        val textViewMaxVal = rootView.findViewById<TextView>(R.id.pocketDialog_textView_maxValue)
        val fabApply: FloatingActionButton = rootView.findViewById(R.id.pocketDialog_fab_apply)
        fabApply.setOnClickListener {
            applyChanges()
            dialog?.dismiss()
        }

        if (option == LEVEL) {
            textViewMaxVal.visibility = View.GONE
            editTextMaxValue.visibility = View.GONE
        }

        editTextValue.requestFocus()


        return rootView
    }

    private fun applyChanges() {

        var updateValue = "0"
        var updateMaxValue = "0"

        if (editTextValue.text.isNotEmpty()) {
            updateValue = editTextValue.text.toString()
        }

        if (editTextMaxValue.text.isNotEmpty()) {
            updateMaxValue = editTextMaxValue.text.toString()
        }

        when (option) {
            CURRENCY -> {
                viewModel.updateCurrencyWithMaxValue(updateValue,
                        updateMaxValue,
                        charId,
                        currencyType)
            }
            EXPERIENCE -> {
                viewModel.updateExperienceWithMaxValue(updateValue.toInt(),
                        updateMaxValue.toInt(),
                        charId)
            }
            LEVEL -> {
                viewModel.updateLevel(updateValue.toInt(), charId)
            }
        }
    }

    companion object {
        const val CURRENCY = 0
        const val EXPERIENCE = 1
        const val LEVEL = 2
    }


}