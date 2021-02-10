package com.awkwardlydevelopedapps.unicharsheet.viewModels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.awkwardlydevelopedapps.unicharsheet.adapters.NotesListAdapter
import com.awkwardlydevelopedapps.unicharsheet.data.Sort
import com.awkwardlydevelopedapps.unicharsheet.models.Note
import com.awkwardlydevelopedapps.unicharsheet.repositories.NoteRepository

class NoteViewModel(application: Application,
                    charId: Int) : ViewModel() {

    private val noteRepository = NoteRepository(application, charId)
    private val allNotes: MediatorLiveData<List<Note>> = MediatorLiveData()
    private val allNotesByNameAsc = noteRepository.allNotesByNameAsc
    private val allNotesByNameDesc = noteRepository.allNotesByNameDesc

    init {
        allNotes.addSource(allNotesByNameAsc) { allNotes.value = it }
    }

    fun orderBy(order: Int) {
        allNotes.removeSource(allNotesByNameAsc)
        allNotes.removeSource(allNotesByNameDesc)

        when (order) {
            Sort.BY_NAME_ASC -> allNotes.addSource(allNotesByNameAsc) { allNotes.value = it }
            Sort.BY_NAME_DESC -> allNotes.addSource(allNotesByNameDesc) { allNotes.value = it }
        }
    }

    fun insert(note: Note) {
        noteRepository.insert(note)
    }

    fun delete(note: Note) {
        noteRepository.delete(note)
    }

    fun getAllNotes(): MediatorLiveData<List<Note>> {
        return allNotes
    }

    fun getNote(noteId: Int): LiveData<Note> {
        return noteRepository.getNote(noteId)
    }

    fun updateNote(newNote: String, charId: Int, noteId: Int) {
        noteRepository.updateNote(newNote, charId, noteId)
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