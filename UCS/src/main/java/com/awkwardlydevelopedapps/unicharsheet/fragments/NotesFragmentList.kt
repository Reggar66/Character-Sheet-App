package com.awkwardlydevelopedapps.unicharsheet.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.awkwardlydevelopedapps.unicharsheet.MainActivity
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.adapters.NotesListAdapter
import com.awkwardlydevelopedapps.unicharsheet.models.Note
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesFragmentList : Fragment() {

    val notes = ArrayList<Note>()
    val adapter = NotesListAdapter(notes)

    private var characterId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        characterId = (requireActivity() as MainActivity).characterId
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


    /**
     * Inner classes
     */

    inner class AddNoteOnClickListener : View.OnClickListener {
        override fun onClick(p0: View?) {
            //TODO creating normal note entry, add to db
            notes.add(Note("Test note with cool name",
                    "Lorem ipsum dolor sit amet, " +
                            "consectetur adipiscing elit. Fusce euismod mauris " +
                            "ut metus condimentum efficitur. Donec ultrices magna sem, sed " +
                            "euismod massa dignissim at. Vestibulum eleifend egestas libero, quis volutpat" +
                            " ex gravida eu. In hac habitasse platea dictumst. In consequat nunc sed " +
                            "accumsan fermentum. Mauris velit lacus, tempus non sem non, " +
                            "sagittis tincidunt eros.",
                    characterId))
            adapter.notifyItemInserted(notes.size)

            notes.add(Note("Test note with cool name",
                    "Lorem ipsum dolor sit amet, " +
                            "consectetur adipiscing elit. Fusce euismod mauris " +
                            "ut metus condimentum efficitur. Donec ultrices magna sem, sed ",
                    characterId))
            adapter.notifyItemInserted(notes.size)
        }

    }

    inner class DeleteNoteOnClickListener : View.OnClickListener {
        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
        }

    }


}