package com.awkwardlydevelopedapps.unicharsheet.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.awkwardlydevelopedapps.unicharsheet.R
import com.awkwardlydevelopedapps.unicharsheet.models.Note
import com.awkwardlydevelopedapps.unicharsheet.utils.NoteDiffUtilCallback

class NotesListAdapter() : RecyclerView.Adapter<NotesListAdapter.ViewHolder>() {

    var notes = ArrayList<Note>()
    var showChecks = false
        private set
    private lateinit var context: Context

    var onItemClickListener: OnItemClickListener? = null
    var onItemLongClickListener: OnLongItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(itemView: View?, position: Int): Boolean
    }

    interface OnLongItemClickListener {
        fun onItemLongClick(itemView: View?, position: Int): Boolean
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
            View.OnClickListener,
            View.OnLongClickListener {
        init {
            itemView.setOnClickListener(this)
            itemView.setOnLongClickListener(this)
        }

        val textViewTitle: TextView = itemView.findViewById(R.id.textView_title_note_list_item)
        val textViewNoteSummary: TextView = itemView.findViewById(R.id.textView_text_summary_note_list_item)

        override fun onClick(p0: View?) {
            if (showChecks) {
                manageBackgroundSelection()
            } else {
                onItemClickListener?.onItemClick(p0, adapterPosition)
            }
        }

        override fun onLongClick(p0: View?): Boolean {
            onItemLongClickListener?.onItemLongClick(p0, adapterPosition)
            return true
        }

        private fun manageBackgroundSelection() {
            val note = notes[adapterPosition]
            if (note.isChecked) {
                note.isChecked = false
                itemView.background = ContextCompat.getDrawable(context, R.drawable.note_background_drawable)
            } else {
                note.isChecked = true
                itemView.background = ContextCompat.getDrawable(context, R.drawable.note_background_drawable_selected)
            }
        }

        fun bindCheckBox() {
            val note = notes[adapterPosition]

            if (note.isChecked) {
                itemView.background = ContextCompat.getDrawable(context, R.drawable.note_background_drawable_selected)
            } else {
                itemView.background = ContextCompat.getDrawable(context, R.drawable.note_background_drawable)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
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

        holder.bindCheckBox()
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

    fun setShowChecks() {
        if (!showChecks) {
            showChecks = true
        } else {
            showChecks = false
            for ((index, note) in notes.withIndex()) {
                if (note.isChecked) {
                    note.isChecked = false
                    notifyItemChanged(index)
                }
            }
        }
    }
}