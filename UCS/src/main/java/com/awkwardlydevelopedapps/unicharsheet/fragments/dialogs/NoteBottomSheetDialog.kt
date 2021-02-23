package com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.models.BottomSheetDialogModel
import com.awkwardlydevelopedapps.unicharsheet.models.Note
import com.awkwardlydevelopedapps.unicharsheet.viewModels.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NoteBottomSheetDialog() : BottomSheetDialogModel() {

    private lateinit var editTextTitle: EditText
    private lateinit var editTextNote: EditText

    var noticeDialogListener: NoticeDialogListener? = null

    interface NoticeDialogListener {
        fun onPositiveButtonListener(title: String, note: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.dialog_note, container, false)

        editTextTitle = rootView.findViewById(R.id.dialogNote_editText_title)
        editTextNote = rootView.findViewById(R.id.dialogNote_editText_note)
        editTextTitle.requestFocus()

        val fabAdd = rootView.findViewById<FloatingActionButton>(R.id.dialogNote_fab_apply)
        fabAdd.setOnClickListener {
            //createNote()
            noticeDialogListener?.onPositiveButtonListener(
                    editTextTitle.text.toString(),
                    editTextNote.text.toString()
            )
            dismiss() //dismiss dialog
        }

        return rootView
    }
}