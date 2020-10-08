package com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.models.BottomSheetDialogModel
import com.awkwardlydevelopedapps.unicharsheet.models.Stat
import com.awkwardlydevelopedapps.unicharsheet.viewModels.StatsViewModel

class StatBottomSheetDialog(val viewModel: StatsViewModel,
                            val charId: Int,
                            val pageNumber: Int) : BottomSheetDialogModel() {

    private lateinit var editTextName: EditText
    private lateinit var editTextValue: EditText
    private lateinit var titleTextView: TextView

    var option = 0
    var title: String = ""
    lateinit var oldStat: Stat

    companion object {
        private const val DEFAULT_VALUE = "0"
        const val OPTION_ADD = 0
        const val OPTION_EDIT = 1
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.dialog_stat_add, container, false)

        editTextName = rootView.findViewById(R.id.stat_name_dialog_editText)
        editTextName.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b)
                editTextName.setSelection(editTextName.length())
        }

        editTextValue = rootView.findViewById(R.id.stat_value_dialog_editText)
        editTextValue.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b)
                editTextValue.setSelection(editTextValue.length())
        }


        titleTextView = rootView.findViewById(R.id.title)

        checkForTitle()

        if (option == OPTION_EDIT) {
            setOldValues()
        }

        editTextName.setSelection(editTextName.text.length)
        editTextName.requestFocus()

        val addButton: View = rootView.findViewById(R.id.floatingActionButton_add_stat)
        addButton.setOnClickListener(OnAddClickListener())

        return rootView
    }

    private fun checkForTitle() {
        if (title.isNotEmpty()) {
            titleTextView.text = title
            titleTextView.visibility = View.VISIBLE
        }
    }

    private fun setOldValues() {
        val name = oldStat.name.toString()
        var value = oldStat.value.toString()

        if (value.isEmpty()) {
            value = DEFAULT_VALUE
        }

        editTextName.setText(name)
        editTextValue.setText(value)
    }


    /**
     * Inner Classes
     */

    inner class OnAddClickListener : View.OnClickListener {
        override fun onClick(v: View?) {
            when (option) {
                OPTION_ADD -> {
                    addStat()
                }
                OPTION_EDIT -> {
                    editStat()
                }
            }
        }

        private fun addStat() {
            val name = editTextName.text.toString()
            var value = editTextValue.text.toString()

            if (value.isEmpty()) {
                value = Companion.DEFAULT_VALUE
            }

            viewModel.insert(Stat(name, value, charId, pageNumber))

            resetFields()
        }

        private fun editStat() {
            val name = editTextName.text.toString()
            var value = editTextValue.text.toString()

            if (value.isEmpty()) {
                value = Companion.DEFAULT_VALUE
            }

            viewModel.updateStatValues(name,
                    value,
                    oldStat.charId,
                    oldStat.id)
            dialog?.dismiss()
        }

        private fun resetFields() {
            editTextName.text.clear()
            editTextValue.text.clear()
            editTextName.requestFocus()
        }

    }

    inner class OnDismissClickListener : View.OnClickListener {
        override fun onClick(v: View?) {
            dialog?.cancel()
        }

    }
}