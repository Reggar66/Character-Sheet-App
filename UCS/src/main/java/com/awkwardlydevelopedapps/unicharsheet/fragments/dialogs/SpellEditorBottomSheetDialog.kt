package com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.models.BottomSheetDialogModel
import com.awkwardlydevelopedapps.unicharsheet.viewModels.SpellsViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class SpellEditorBottomSheetDialog(private val viewModel: SpellsViewModel,
                                   private val charId: Int,
                                   private val spellId: Int) : BottomSheetDialogModel() {


    var option = 0
    var oldValue: String = ""
    private lateinit var editText: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_spell_editor, container, false)

        editText = rootView.findViewById(R.id.dialogSpellEditor_editText_value)
        val textView: TextView = rootView.findViewById(R.id.dialogSpellEditor_textView_description)
        val fabApply: FloatingActionButton = rootView.findViewById(R.id.dialogSpellEditor_fab_apply)
        fabApply.setOnClickListener {
            applyChanges()
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

    private fun applyChanges() {
        when (option) {
            DESCRIPTION -> viewModel.updateDescription(editText.text.toString(), charId, spellId)
            DAMAGE_VALUE -> viewModel.updateDmgValue(editText.text.toString(), charId, spellId)
            DAMAGE_ADD_EFFECT -> viewModel.updateAddEffectValue(editText.text.toString(), charId, spellId)
            COST_VALUE -> viewModel.updateCostValue(editText.text.toString(), charId, spellId)
            COST_ADD_VALUE -> viewModel.updateAddCostValue(editText.text.toString(), charId, spellId)
            SPECIAL_NOTES -> viewModel.updateSpecialNotes(editText.text.toString(), charId, spellId)
        }
    }

    companion object {
        const val DESCRIPTION = 0
        const val DAMAGE_VALUE = 1
        const val DAMAGE_ADD_EFFECT = 2
        const val COST_VALUE = 3
        const val COST_ADD_VALUE = 4
        const val SPECIAL_NOTES = 5
    }
}