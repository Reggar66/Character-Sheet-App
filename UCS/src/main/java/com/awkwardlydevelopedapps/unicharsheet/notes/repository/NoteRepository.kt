package com.awkwardlydevelopedapps.unicharsheet.notes.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.awkwardlydevelopedapps.unicharsheet.common.utils.ExecSingleton
import com.awkwardlydevelopedapps.unicharsheet.common.data.DbSingleton
import com.awkwardlydevelopedapps.unicharsheet.notes.dao.NoteDao
import com.awkwardlydevelopedapps.unicharsheet.notes.model.Note

class NoteRepository(application: Application,
                     charId: Int) {

    private val noteDao: NoteDao = DbSingleton.Instance(application).noteDao
    val allNotes: LiveData<List<Note>> = noteDao.getLiveDataAllNotes(charId)
    val allNotesByNameAsc: LiveData<List<Note>> = noteDao.getLiveDataAllNotesByNameAsc(charId)
    val allNotesByNameDesc: LiveData<List<Note>> = noteDao.getLiveDataAllNotesByNameDesc(charId)

    fun insert(note: Note) {
        ExecSingleton.getInstance().execute {
            noteDao.insert(note)
        }
    }

    fun delete(note: Note) {
        ExecSingleton.getInstance().execute {
            noteDao.delete(note)
        }
    }

    fun getNote(noteId: Int): LiveData<Note> {
        return noteDao.getLiveDataNote(noteId)
    }

    fun updateNote(newNote: String, charId: Int, noteId: Int) {
        ExecSingleton.getInstance().execute {
            noteDao.updateNote(newNote, charId, noteId)
        }
    }
}