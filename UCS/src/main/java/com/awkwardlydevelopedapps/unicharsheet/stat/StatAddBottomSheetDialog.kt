package com.awkwardlydevelopedapps.unicharsheet.stat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.models.BottomSheetDialogModel
import com.awkwardlydevelopedapps.unicharsheet.viewModels.StatsViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class StatAddBottomSheetDialog(val viewModel: StatsViewModel,
                               val charId: Int,
                               val pageNumber: Int) : BottomSheetDialogModel() {

    lateinit var editTextName: EditText
    lateinit var editTextValue: EditText

    val DEFAULT_VALUE = "0"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.dialog_stat_add, container, false)

        editTextName = rootView.findViewById(R.id.stat_name_dialog_editText)
        editTextValue = rootView.findViewById(R.id.stat_value_dialog_editText)

        val addButton: View = rootView.findViewById(R.id.floatingActionButton_add_stat)
        addButton.setOnClickListener(OnAddClickListener())

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextName.requestFocus()
    }

    /**
     * Inner Classes
     */

    inner class OnAddClickListener : View.OnClickListener {
        override fun onClick(v: View?) {
            addStat()
        }

        private fun addStat() {
            val name = editTextName.text.toString()
            var value = editTextValue.text.toString()

            if (value.isEmpty()) {
                value = DEFAULT_VALUE
            }

            viewModel.insert(Stat(name, value, charId, pageNumber))

            resetFields()
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