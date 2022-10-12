package com.example.roomdatabase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_POSITION
import com.example.roomdatabase.R
import com.example.roomdatabase.data.Note

class NoteAdapter(private val onItemClickListener: (Note) -> Unit) :
    ListAdapter<Note, NoteAdapter.NoteHolder>(diffCallback) {


    inner class NoteHolder(iv: View) : RecyclerView.ViewHolder(iv) {
        val tvTitle = itemView.findViewById<TextView>(R.id.txtview_title)
        val tvDesc = itemView.findViewById<TextView>(R.id.txtview_description)
        val tvPriority = itemView.findViewById<TextView>(R.id.txtview_priority)

        init {
            itemView.setOnClickListener {
                if (adapterPosition != NO_POSITION) {
                    onItemClickListener(getItem(adapterPosition))
                }
            }
        }
    }

    fun getNoteAt(position: Int) = getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteHolder(itemView)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        with(getItem(position)) {
            holder.tvTitle.text = title
            holder.tvDesc.text = desc
            holder.tvPriority.text = priority.toString()
        }
    }

}

private val diffCallback = object : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Note, newItem: Note) = oldItem.title == newItem.title
            && oldItem.desc == newItem.desc
            && oldItem.priority == newItem.priority


}