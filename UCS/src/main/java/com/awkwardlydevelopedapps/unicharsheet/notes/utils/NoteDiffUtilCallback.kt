package com.awkwardlydevelopedapps.unicharsheet.notes.utils

import androidx.recyclerview.widget.DiffUtil
import com.awkwardlydevelopedapps.unicharsheet.notes.model.Note

class NoteDiffUtilCallback(private val oldNotes: List<Note>,
                           private val newNotes: List<Note>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldNotes.size
    }

    override fun getNewListSize(): Int {
        return newNotes.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldNotes[oldItemPosition].id == newNotes[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldNote = oldNotes[oldItemPosition]
        val newNote = newNotes[newItemPosition]

        return oldNote.title == newNote.title &&
                oldNote.note == newNote.note
    }


}