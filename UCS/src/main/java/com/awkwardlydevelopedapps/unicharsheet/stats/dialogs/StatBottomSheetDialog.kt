package com.awkwardlydevelopedapps.unicharsheet.stats.dialogs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.common.model.BottomSheetDialogModel
import com.awkwardlydevelopedapps.unicharsheet.stats.model.Stat

class StatBottomSheetDialog : BottomSheetDialogModel() {

    var charId: Int = NOT_SET
    var pageNumber: Int = NOT_SET

    private var option: Int = NOT_SET
    private var title: String = ""
    private var oldStatId: Int? = null
    private var oldStatName = ""
    private var oldStatValue = ""

    private lateinit var editTextName: EditText
    private lateinit var editTextValue: EditText
    private lateinit var titleTextView: TextView

    var statNoticeDialogListener: StatNoticeDialogListener? = null

    interface StatNoticeDialogListener {
        fun onPositiveClickAddStat(dialog: DialogFragment, stat: Stat)
        fun onPositiveClickEditStat(dialog: DialogFragment, stat: Stat)
        fun onNegativeClick(dialog: DialogFragment)
    }

    companion object {
        private const val NOT_SET = -1
        private const val DEFAULT_VALUE = "0"
        const val OPTION_ADD = 0
        const val OPTION_EDIT = 1

        private const val KEY_CHAR_ID = "CHAR_ID"
        private const val KEY_PAGE_NUMBER = "PAGE_NUMBER"
        private const val KEY_OPTION = "OPTION"
        private const val KEY_OLD_ID = "OLD_ID"
        private const val KEY_OLD_NAME = "OLD_NAME"
        private const val KEY_OLD_VALUE = "OLD_VALUE"
    }

    /**
     * Creates new instance of StatBottomDialog.
     * Parameters for old stat can be null if option is OPTION_EDIT.
     * @param option use OPTION_ADD or OPTION_EDIT.
     */
    fun newInstance(
        charId: Int,
        pageNumber: Int,
        option: Int,
        oldStatId: Int?,
        oldStatName: String?,
        oldStatValue: String?
    ): StatBottomSheetDialog {
        return StatBottomSheetDialog().apply {
            arguments = Bundle().apply {
                putInt(KEY_CHAR_ID, charId)
                putInt(KEY_PAGE_NUMBER, pageNumber)
                putInt(KEY_OPTION, option)

                if (oldStatId != null) {
                    putInt(KEY_OLD_ID, oldStatId)
                    putString(KEY_OLD_NAME, oldStatName)
                    putString(KEY_OLD_VALUE, oldStatValue)
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            statNoticeDialogListener = parentFragment as StatNoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(parentFragment.toString() + "must implement StatNoticeDialogListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.charId = requireArguments().getInt(KEY_CHAR_ID)
        this.pageNumber = requireArguments().getInt(KEY_PAGE_NUMBER)
        this.option = requireArguments().getInt(KEY_OPTION)

        if (option == OPTION_EDIT) {
            this.oldStatId = requireArguments().getInt(KEY_OLD_ID)
            this.oldStatName = requireArguments().getString(KEY_OLD_NAME).toString()
            this.oldStatValue = requireArguments().getString(KEY_OLD_VALUE).toString()
        }

        when (option) {
            // TODO use resources instead
            OPTION_ADD -> title = "Stat Creation"
            OPTION_EDIT -> title = "Stat Edit"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
        if (oldStatValue.isEmpty()) {
            oldStatValue = DEFAULT_VALUE
        }

        editTextName.setText(oldStatName)
        editTextValue.setText(oldStatValue)
    }


    //****
    // Inner classes
    //****

    inner class OnAddClickListener : View.OnClickListener {
        override fun onClick(v: View?) {
            when (option) {
                OPTION_ADD -> {
                    statNoticeDialogListener?.onPositiveClickAddStat(
                        this@StatBottomSheetDialog,
                        getNewStat()
                    )
                }
                OPTION_EDIT -> {
                    statNoticeDialogListener?.onPositiveClickEditStat(
                        this@StatBottomSheetDialog,
                        getUpdatedStat()
                    )
                }
            }
        }

        private fun getNewStat(): Stat {
            val name = editTextName.text.toString()
            var value = editTextValue.text.toString()

            if (value.isEmpty()) {
                value = DEFAULT_VALUE
            }

            val stat = Stat(
                name,
                value,
                charId,
                pageNumber
            )
            resetFields()
            return stat
        }

        private fun getUpdatedStat(): Stat {
            val name = editTextName.text.toString()
            var value = editTextValue.text.toString()

            if (value.isEmpty()) {
                value = DEFAULT_VALUE
            }

            val stat = Stat(
                name,
                value,
                charId,
                pageNumber
            )
            stat.id = oldStatId!!
            return stat
        }

        private fun resetFields() {
            editTextName.text.clear()
            editTextValue.text.clear()
            editTextName.requestFocus()
        }

    }
}