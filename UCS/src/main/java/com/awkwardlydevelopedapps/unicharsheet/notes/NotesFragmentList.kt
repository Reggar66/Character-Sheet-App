package com.awkwardlydevelopedapps.unicharsheet.notes

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.common.DeleteDialog
import com.awkwardlydevelopedapps.unicharsheet.common.viewModel.DataHolderViewModel
import com.awkwardlydevelopedapps.unicharsheet.notes.adapters.NotesListAdapter
import com.awkwardlydevelopedapps.unicharsheet.notes.model.Note
import com.awkwardlydevelopedapps.unicharsheet.notes.viewModel.NoteSortStateViewModel
import com.awkwardlydevelopedapps.unicharsheet.notes.viewModel.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesFragmentList : Fragment(),
    NotesListAdapter.OnItemClickListener,
    DeleteDialog.NoticeDialogListener,
    NoteBottomSheetDialog.NoticeDialogListener {

    val adapter = NotesListAdapter()
    private var characterID = 0
    private lateinit var fabAddNote: FloatingActionButton
    private lateinit var fabDeleteNote: FloatingActionButton
    private var changeFragmentCallback: ChangeFragmentCallback? = null

    lateinit var viewModel: NoteViewModel
    private lateinit var noteSortStateViewModel: NoteSortStateViewModel
    private val dataHolderViewModel: DataHolderViewModel by activityViewModels()

    interface ChangeFragmentCallback {
        fun changeToDisplayNote(noteId: Int)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        characterID = dataHolderViewModel.characterID
        viewModel = ViewModelProvider(
            this,
            NoteViewModel.NoteViewModelFactory(requireActivity().application, characterID)
        )
            .get(NoteViewModel::class.java)

        noteSortStateViewModel =
            ViewModelProvider(requireActivity()).get(NoteSortStateViewModel::class.java)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            changeFragmentCallback = parentFragment as ChangeFragmentCallback
        } catch (e: ClassCastException) {
            throw ClassCastException(
                parentFragment.toString()
                        + " must implement NotesFragment.ChangeFragmentCallback"
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        noteSortStateViewModel.sortOrderLiveData.observe(viewLifecycleOwner, Observer { orderBy ->
            viewModel.orderBy(orderBy)
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
        val notes = viewModel.getAllNotes().value
        if (notes != null) {
            changeFragmentCallback?.changeToDisplayNote(notes[position].id)
        }
        return true
    }

    override fun onPositiveButtonListener(title: String, note: String) {
        viewModel.insert(
            Note(
                title,
                note,
                characterID
            )
        )
    }

    /**
     * Inner classes
     */

    inner class AddNoteOnClickListener : View.OnClickListener {
        override fun onClick(p0: View?) {
            val bottomDialog = NoteBottomSheetDialog()
            bottomDialog.show(childFragmentManager, "BOTTOM_DIALOG_CREATE_NOTE")
        }

    }

    inner class DeleteNoteOnClickListener : View.OnClickListener {
        override fun onClick(p0: View?) {
            val deleteDialog =
                DeleteDialog()
            deleteDialog.setNoticeDialogListener(this@NotesFragmentList)
                .show(parentFragmentManager, "DIALOG_DELETE_NOTES")
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