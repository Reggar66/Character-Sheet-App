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

class StatAddBottomSheetDialog(val viewModel: StatsViewModel,
                               val charId: Int,
                               val pageNumber: Int) : BottomSheetDialogModel() {

    private lateinit var editTextName: EditText
    private lateinit var editTextValue: EditText
    private lateinit var titleTextView: TextView

    var title: String = ""

    companion object {
        private const val DEFAULT_VALUE = "0"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView: View = inflater.inflate(R.layout.dialog_stat_add, container, false)

        editTextName = rootView.findViewById(R.id.stat_name_dialog_editText)
        editTextValue = rootView.findViewById(R.id.stat_value_dialog_editText)
        titleTextView = rootView.findViewById(R.id.title)
        checkForTitle()

        val addButton: View = rootView.findViewById(R.id.floatingActionButton_add_stat)
        addButton.setOnClickListener(OnAddClickListener())

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextName.requestFocus()
    }

    private fun checkForTitle() {
        if (title.isNotEmpty()) {
            titleTextView.text = title
            titleTextView.visibility = View.VISIBLE
        }
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
                value = Companion.DEFAULT_VALUE
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