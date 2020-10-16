package com.awkwardlydevelopedapps.unicharsheet.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.models.Note
import com.awkwardlydevelopedapps.unicharsheet.utils.NoteDiffUtilCallback

class NotesListAdapter() : RecyclerView.Adapter<NotesListAdapter.ViewHolder>() {

    private var notes = ArrayList<Note>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewTitle: TextView = itemView.findViewById(R.id.textView_title_note_list_item)
        val textViewNoteSummary: TextView = itemView.findViewById(R.id.textView_text_summary_note_list_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.list_item_note, parent, false)

        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textViewTitle.text = notes[position].title
        val noteText = notes[position].note
        if (noteText.length >= 300) {
            val noteShortened: String = noteText.subSequence(0, 297) as String
            holder.textViewNoteSummary.text = "$noteShortened..."
        } else {
            holder.textViewNoteSummary.text = noteText
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun setNotes(notes: List<Note>) {
        val noteDiffUtilCallback = NoteDiffUtilCallback(this.notes, notes)
        val diffResult = DiffUtil.calculateDiff(noteDiffUtilCallback)

        this.notes.clear()
        this.notes.addAll(notes)
        diffResult.dispatchUpdatesTo(this)
    }
}