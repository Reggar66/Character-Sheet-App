package com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.models.BottomSheetDialogModel
import com.awkwardlydevelopedapps.unicharsheet.models.Item
import com.awkwardlydevelopedapps.unicharsheet.viewModels.BackpackViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ItemBottomSheetDialog() : BottomSheetDialogModel() {

    var option = 0
    private lateinit var editTextName: EditText
    private lateinit var editTextQuantity: EditText
    var oldItem: Item? = null

    var noticeDialogListener: NoticeDialogListener? = null

    interface NoticeDialogListener {
        fun onPositiveClickListener(option: Int, itemName: String, quantity: String, oldItem: Item?)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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

        if (option == EDIT && oldItem != null) {
            editTextName.setText(oldItem!!.name)
            editTextQuantity.setText(oldItem!!.quantity)
        }
        editTextName.requestFocus()

        val fabApply: FloatingActionButton = rootView.findViewById(R.id.itemsDialog_fab_apply)
        fabApply.setOnClickListener {
            noticeDialogListener?.onPositiveClickListener(
                    option,
                    editTextName.text.toString(),
                    editTextQuantity.text.toString(),
                    oldItem)
            dialog?.dismiss()
        }

        return rootView
    }

    companion object {
        const val ADD = 0
        const val EDIT = 1
    }
}