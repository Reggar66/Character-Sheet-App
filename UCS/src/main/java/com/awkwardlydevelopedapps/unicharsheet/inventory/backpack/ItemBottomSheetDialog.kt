package com.awkwardlydevelopedapps.unicharsheet.inventory.backpack

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.common.model.BottomSheetDialogModel
import com.awkwardlydevelopedapps.unicharsheet.common.utils.LogWrapper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ItemBottomSheetDialog : BottomSheetDialogModel() {

    private lateinit var editTextName: EditText
    private lateinit var editTextQuantity: EditText

    private var option = 0
    private var oldItemId: Int? = null
    private var oldItemName: String? = null
    private var oldItemValue: String? = null

    var noticeDialogListener: NoticeDialogListener? = null

    interface NoticeDialogListener {
        fun onPositiveClickListener(
            option: Int,
            itemName: String,
            quantity: String,
            oldItemId: Int?
        )
    }

    companion object {
        const val ADD = 0
        const val EDIT = 1

        private const val KEY_OPTION = "OPTION"
        private const val KEY_OLD_ITEM_ID = "OLD_ID"
        private const val KEY_OLD_ITEM_NAME = "OLD_NAME"
        private const val KEY_OLD_ITEM_VALUE = "OLD_VALUE"

        fun newInstance(
            option: Int,
            oldItemId: Int?,
            oldItemName: String?,
            oldItemValue: String?
        ): ItemBottomSheetDialog {
            return ItemBottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putInt(KEY_OPTION, option)
                    oldItemId?.let { oldId ->
                        putInt(KEY_OLD_ITEM_ID, oldId)
                        LogWrapper.v("INFO", "ItemBottomSheetDialog: bundled old item id")
                    }
                    oldItemName?.let { oldName ->
                        putString(KEY_OLD_ITEM_NAME, oldName)
                        LogWrapper.v("INFO", "ItemBottomSheetDialog: bundled old item name")
                    }
                    oldItemValue?.let { oldValue ->
                        putString(KEY_OLD_ITEM_VALUE, oldValue)
                        LogWrapper.v("INFO", "ItemBottomSheetDialog: bundled old item value")
                    }
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            noticeDialogListener = parentFragment as NoticeDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                parentFragment.toString()
                        + " must implement ItemBottomSheetDialog.NoticeDialogListener"
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.option = arguments?.getInt(KEY_OPTION)!!

        if (option == EDIT) {
            this.oldItemId = arguments?.getInt(KEY_OLD_ITEM_ID)
            this.oldItemName = arguments?.getString(KEY_OLD_ITEM_NAME)
            this.oldItemValue = arguments?.getString(KEY_OLD_ITEM_VALUE)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.dialog_items, container, false)

        editTextName = rootView.findViewById(R.id.itemsDialog_editText_name)
        editTextName.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b)
                editTextName.setSelection(editTextName.length())
        }
        editTextQuantity = rootView.findViewById(R.id.itemsDialog_editText_Quantity)
        editTextQuantity.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
            if (b)
                editTextQuantity.setSelection(editTextQuantity.length())
        }

        if (option == EDIT) {
            editTextName.setText(oldItemName)
            editTextQuantity.setText(oldItemValue)
        }
        editTextName.requestFocus()

        val fabApply: FloatingActionButton = rootView.findViewById(R.id.itemsDialog_fab_apply)
        fabApply.setOnClickListener {
            noticeDialogListener?.onPositiveClickListener(
                option,
                editTextName.text.toString(),
                editTextQuantity.text.toString(),
                oldItemId
            )
            dialog?.dismiss()
        }

        return rootView
    }
}