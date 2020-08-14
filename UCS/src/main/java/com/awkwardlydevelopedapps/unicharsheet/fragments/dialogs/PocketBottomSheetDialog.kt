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
    lateinit var editTextValue: EditText
    lateinit var editTextMaxValue: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_pocket_fragment, container, false)
        editTextValue = rootView.findViewById(R.id.pocketDialog_editText_value)
        editTextMaxValue = rootView.findViewById(R.id.pocketDialog_editText_maxValue)
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
        when (option) {
            CURRENCY -> {
                viewModel.updateCurrencyWithMaxValue(editTextValue.text.toString(),
                        editTextMaxValue.text.toString(),
                        charId,
                        currencyType)
            }
            EXPERIENCE -> {
                viewModel.updateExperienceWithMaxValue(editTextValue.text.toString().toInt(),
                        editTextMaxValue.text.toString().toInt(),
                        charId)
            }
            LEVEL -> {
                viewModel.updateLevel(editTextValue.text.toString().toInt(), charId)
            }
        }
    }

    companion object {
        const val CURRENCY = 0
        const val EXPERIENCE = 1
        const val LEVEL = 2
    }


}