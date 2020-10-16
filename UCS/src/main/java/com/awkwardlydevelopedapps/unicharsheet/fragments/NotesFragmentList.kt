package com.awkwardlydevelopedapps.unicharsheet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awkwardlydevelopedapps.unicharsheet.MainActivity
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.adapters.NotesListAdapter
import com.awkwardlydevelopedapps.unicharsheet.models.Note
import com.awkwardlydevelopedapps.unicharsheet.viewModels.NoteViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesFragmentList : Fragment() {

    val notes = ArrayList<Note>()
    val adapter = NotesListAdapter()
    private var characterId = 0


    lateinit var viewModel: NoteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        characterId = (requireActivity() as MainActivity).characterId
        viewModel = ViewModelProvider(this, NoteViewModel.NoteViewModelFactory(requireActivity().application, characterId))
                .get(NoteViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_notes_list, container, false)

        val floatingActionButtonAddNote = rootView.findViewById<FloatingActionButton>(R.id.add_button_notes)
        floatingActionButtonAddNote.setOnClickListener(AddNoteOnClickListener())

        val floatingActionButtonDeleteNote = rootView.findViewById<FloatingActionButton>(R.id.floatingActionButton_notes_delete)
        floatingActionButtonDeleteNote.setOnClickListener(DeleteNoteOnClickListener())
        floatingActionButtonDeleteNote.hide()


        val recyclerView = rootView.findViewById<RecyclerView>(R.id.notes_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = this.adapter

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel.getAllNotes().observe(viewLifecycleOwner, Observer {
            adapter.setNotes(it)
        })

    }

    /**
     * Inner classes
     */

    inner class AddNoteOnClickListener : View.OnClickListener {
        override fun onClick(p0: View?) {
            //TODO showing dialog with note creation
            viewModel.insert(Note("Really creative name for a note",
                    "Classes, objects, interfaces, constructors, functions, properties and their setters can have visibility modifiers. (Getters always have the same visibility as the property.) There are four visibility modifiers in Kotlin: private, protected, internal and public. The default visibility, used if there is no explicit modifier, is public.",
                    characterId))
        }

    }

    inner class DeleteNoteOnClickListener : View.OnClickListener {
        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
        }

    }


}