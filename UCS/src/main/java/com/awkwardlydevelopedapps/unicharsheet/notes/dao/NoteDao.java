package com.awkwardlydevelopedapps.unicharsheet.notes.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.awkwardlydevelopedapps.unicharsheet.notes.model.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    long insert(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * FROM notes WHERE char_id = :charId")
    LiveData<List<Note>> getLiveDataAllNotes(int charId);

    @Query("SELECT * FROM notes WHERE char_id = :charId ORDER BY title ASC")
    LiveData<List<Note>> getLiveDataAllNotesByNameAsc(int charId);

    @Query("SELECT * FROM notes WHERE char_id = :charId ORDER BY title DESC")
    LiveData<List<Note>> getLiveDataAllNotesByNameDesc(int charId);

    @Query("SELECT * FROM notes WHERE id = :id")
    LiveData<Note> getLiveDataNote(int id);

    @Query("UPDATE notes SET note = :newNote WHERE char_id=:charId AND id=:noteId")
    void updateNote(String newNote, int charId, int noteId);
}
