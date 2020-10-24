package com.awkwardlydevelopedapps.unicharsheet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.awkwardlydevelopedapps.unicharsheet.MainActivity
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.models.Note
import com.awkwardlydevelopedapps.unicharsheet.viewModels.NoteViewModel

class NotesFragmentDisplay() : Fragment() {

    var charId: Int = 0
    var noteId: Int = 0 //set during fragment creation in NotesFragment
    var note: Note? = null
    lateinit var textViewTitle: TextView
    lateinit var viewModel: NoteViewModel
    lateinit var editTextNote: EditText
    lateinit var editButton: ImageView

    var changeFragmentCallback: ChangeFragmentCallback? = null

    interface ChangeFragmentCallback {
        fun changeToList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_note_display, container, false)
        charId = (requireActivity() as MainActivity).characterId

        textViewTitle = rootView.findViewById(R.id.textView_note_display_title)
        editTextNote = rootView.findViewById(R.id.editText_note_display_text)

        val closeButton = rootView.findViewById<ImageView>(R.id.imageView_close_note_display)
        closeButton.setOnClickListener(CloseButtonOnClickListener())
        editButton = rootView.findViewById(R.id.imageView_edit_note_display)
        editButton.setOnClickListener(EditButtonOnClickListener())

        viewModel = NoteViewModel(requireActivity().application, charId)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getNote(noteId).observe(viewLifecycleOwner, Observer {
            textViewTitle.text = it.title
            editTextNote.setText(it.note)
        })
    }

    override fun onPause() {
        super.onPause()
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
                editTextNote.setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.TextOnSecondary))
                editTextNote.requestFocus()
                editTextNote.setSelection(editTextNote.length())

                editButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                        R.drawable.ic_done_black_24dp))

                edit = true
            } else if (edit) {
                editTextNote.isEnabled = false
                editTextNote.setTextColor(ContextCompat.getColor(requireContext(),
                        R.color.colorSecondaryDark))

                editButton.setImageDrawable(ContextCompat.getDrawable(requireContext(),
                        R.drawable.ic_edit_black_24dp))

                viewModel.updateNote(editTextNote.text.toString(), charId, noteId)
                edit = false
            }
        }
    }
}