package com.awkwardlydevelopedapps.unicharsheet.notes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.common.utils.LogWrapper
import com.awkwardlydevelopedapps.unicharsheet.common.viewModel.DataHolderViewModel
import com.awkwardlydevelopedapps.unicharsheet.notes.model.Note
import com.awkwardlydevelopedapps.unicharsheet.notes.viewModel.NoteViewModel

class NotesFragmentDisplay : Fragment() {

    private var characterID: Int = 0
    private var noteID: Int = 0
    private lateinit var textViewTitle: TextView
    lateinit var viewModel: NoteViewModel
    lateinit var editTextNote: EditText
    lateinit var editButton: ImageView

    private var changeFragmentCallback: ChangeFragmentCallback? = null

    private val dataHolderViewModel: DataHolderViewModel by activityViewModels()

    interface ChangeFragmentCallback {
        fun changeToList()
    }

    companion object {
        const val KEY_NOTE_ID = "NOTE_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        noteID = arguments?.getInt(KEY_NOTE_ID)!!
        LogWrapper.v("INFO", "NotesFragmentDisplay: noteId=$noteID")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            changeFragmentCallback = parentFragment as ChangeFragmentCallback
        } catch (e: ClassCastException) {
            throw ClassCastException(
                parentFragment.toString()
                        + " must implement NotesFragmentDisplay.ChangeFragmentCallback"
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_note_display, container, false)
        characterID = dataHolderViewModel.characterID

        textViewTitle = rootView.findViewById(R.id.textView_note_display_title)
        editTextNote = rootView.findViewById(R.id.editText_note_display_text)

        val closeButton = rootView.findViewById<ImageView>(R.id.imageView_close_note_display)
        closeButton.setOnClickListener(CloseButtonOnClickListener())
        editButton = rootView.findViewById(R.id.imageView_edit_note_display)
        editButton.setOnClickListener(EditButtonOnClickListener())

        viewModel = ViewModelProvider(
            this,
            NoteViewModel.NoteViewModelFactory(requireActivity().application, characterID)
        )
            .get(NoteViewModel::class.java)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNote(noteID).observe(viewLifecycleOwner, Observer {
            textViewTitle.text = it.title
            editTextNote.setText(it.note)
        })
    }

    /**
     * Inner classes
     */

    inner class CloseButtonOnClickListener() : View.OnClickListener {
        override fun onClick(p0: View?) {
            changeFragmentCallback?.changeToList()
        }
    }

    inner class EditButtonOnClickListener() : View.OnClickListener {
        private var edit = false

        override fun onClick(p0: View?) {
            if (!edit) {
                editTextNote.isEnabled = true
                editTextNote.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.TextOnSecondary
                    )
                )
                editTextNote.requestFocus()
                editTextNote.setSelection(editTextNote.length())

                editButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_done_black_24dp
                    )
                )

                edit = true
            } else if (edit) {
                editTextNote.isEnabled = false
                editTextNote.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.colorSecondaryDark
                    )
                )

                editButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_edit_black_24dp
                    )
                )

                viewModel.updateNote(editTextNote.text.toString(), characterID, noteID)
                edit = false
            }
        }
    }
}