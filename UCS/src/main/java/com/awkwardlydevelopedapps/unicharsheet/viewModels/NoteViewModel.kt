package com.awkwardlydevelopedapps.unicharsheet.viewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.awkwardlydevelopedapps.unicharsheet.adapters.NotesListAdapter
import com.awkwardlydevelopedapps.unicharsheet.models.Note
import com.awkwardlydevelopedapps.unicharsheet.repositories.NoteRepository

class NoteViewModel(application: Application,
                    charId: Int) : ViewModel() {

    private val noteRepository = NoteRepository(application, charId)
    private val allNotes = noteRepository.allNotes

    fun insert(note: Note) {
        noteRepository.insert(note)
    }

    fun delete(note: Note) {
        noteRepository.delete(note)
    }

    fun getAllNotes(): LiveData<List<Note>> {
        return allNotes
    }

    fun checkAndDeleteSpells(adapter: NotesListAdapter) {
        val tempNotes = allNotes.value
        if (tempNotes != null) {
            for (note in tempNotes) {
                if (note.isChecked) {
                    noteRepository.delete(note)
                }
            }
            adapter.setShowChecks()
        }
    }

    /**
     * Factory
     */

    class NoteViewModelFactory(val application: Application, val charId: Int) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return NoteViewModel(application, charId) as T
        }

    }
}