package com.awkwardlydevelopedapps.unicharsheet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awkwardlydevelopedapps.unicharsheet.MainActivity
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.adapters.NotesListAdapter
import com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs.DeleteDialog
import com.awkwardlydevelopedapps.unicharsheet.fragments.dialogs.NoteBottomSheetDialog
import com.awkwardlydevelopedapps.unicharsheet.models.Note
import com.awkwardlydevelopedapps.unicharsheet.viewModels.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesFragmentList : Fragment(),
        NotesListAdapter.OnItemClickListener,
        DeleteDialog.NoticeDialogListener {

    val adapter = NotesListAdapter()
    private var characterId = 0
    private lateinit var fabAddNote: FloatingActionButton
    private lateinit var fabDeleteNote: FloatingActionButton

    lateinit var viewModel: NoteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        characterId = (requireActivity() as MainActivity).characterId
        viewModel = ViewModelProvider(this, NoteViewModel.NoteViewModelFactory(requireActivity().application, characterId))
                .get(NoteViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_notes_list, container, false)

        fabAddNote = rootView.findViewById(R.id.add_button_notes)
        fabAddNote.setOnClickListener(AddNoteOnClickListener())

        fabDeleteNote = rootView.findViewById(R.id.floatingActionButton_notes_delete)
        fabDeleteNote.setOnClickListener(DeleteNoteOnClickListener())
        fabDeleteNote.hide()


        adapter.onItemClickListener = this
        adapter.onItemLongClickListener = OnNoteItemLongClickListener()
        val recyclerView = rootView.findViewById<RecyclerView>(R.id.notes_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = this.adapter
        recyclerView.addOnScrollListener(FabHideListener())

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getAllNotes().observe(viewLifecycleOwner, Observer {
            adapter.setNotes(it)
        })

    }

    override fun onDeleteDialogPositiveClick(dialog: DialogFragment?) {
        viewModel.checkAndDeleteSpells(adapter)
        fabAddNote.show()
        fabDeleteNote.hide()
    }

    override fun onDeleteDialogNegativeClick(dialog: DialogFragment?) {
        dialog?.dialog?.cancel()
    }

    override fun onItemClick(itemView: View?, position: Int): Boolean {
        // TODO opening note
        Toast.makeText(context, "click$position", Toast.LENGTH_LONG).show()
        return true
    }


    /**
     * Inner classes
     */

    inner class AddNoteOnClickListener : View.OnClickListener {
        override fun onClick(p0: View?) {
            //TODO showing dialog with note creation
            /*
            viewModel.insert(Note("Really creative name for a note",
                    "Classes, objects, interfaces, constructors, functions, properties and their setters can have visibility modifiers. (Getters always have the same visibility as the property.) There are four visibility modifiers in Kotlin: private, protected, internal and public. The default visibility, used if there is no explicit modifier, is public.",
                    characterId))
             */

            val bottomDialog = NoteBottomSheetDialog(viewModel, characterId)
            bottomDialog.show(parentFragmentManager, "BOTTOM_DIALOG_CREATE_NOTE")
        }

    }

    inner class DeleteNoteOnClickListener : View.OnClickListener {
        override fun onClick(p0: View?) {
            val deleteDialog = DeleteDialog()
            deleteDialog.setTargetFragment(this@NotesFragmentList, 0)
            deleteDialog.show(parentFragmentManager, "DIALOG_DELETE_NOTES")
        }

    }

    inner class FabHideListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            if (adapter.showChecks) {
                return
            }

            if (dy > 0 && fabAddNote.visibility == View.VISIBLE) {
                fabAddNote.hide()
            } else if (dy < 0 && fabAddNote.visibility != View.VISIBLE) {
                fabAddNote.show()
            }
        }
    }

    inner class OnNoteItemLongClickListener : NotesListAdapter.OnLongItemClickListener {
        override fun onItemLongClick(itemView: View?, position: Int): Boolean {
            adapter.notes[position].isChecked = true
            adapter.setShowChecks()
            adapter.notifyItemChanged(position)

            handleFabHiding()
            return true
        }

        private fun handleFabHiding() {
            if (fabDeleteNote.isShown) {
                fabDeleteNote.hide()
                fabAddNote.show()
            } else {
                fabDeleteNote.show()
                fabAddNote.hide()
            }
        }
    }
}