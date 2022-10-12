package com.example.roomdatabase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdatabase.adapter.NoteAdapter
import com.example.roomdatabase.data.Note
import kotlinx.android.synthetic.main.activity_main.*

const val ADD_NOTE_REQUEST = 1
const val EDIT_NOTE_REQUEST = 2

class MainActivity : AppCompatActivity() {

    lateinit var vm: NoteViewModel
    lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpRecyclerView()
        setUpListener()

        vm = ViewModelProvider(this)[NoteViewModel::class.java]
        vm.getAllNotes().observe(this) {
            Log.i("Note Observed", "$it")
            adapter.submitList(it)
        }
    }

    private fun setUpListener() {

        float_button.setOnClickListener {
            val intent = Intent(this, AddEditNoteActivity::class.java)
            startActivityForResult(intent, ADD_NOTE_REQUEST)
        }
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val note = adapter.getNoteAt(viewHolder.adapterPosition)
                vm.delete(note)
            }
        }).attachToRecyclerView(recycler_view)
    }

    private fun setUpRecyclerView() {

        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        adapter = NoteAdapter { clickedNote ->
            val intent = Intent(this, AddEditNoteActivity::class.java)
            intent.putExtra(EXTRA_ID, clickedNote.id)
            intent.putExtra(EXTRA_TITLE, clickedNote.title)
            intent.putExtra(EXTRA_DESCRIPTION, clickedNote.desc)
            intent.putExtra(EXTRA_PRIORITY, clickedNote.priority)
            startActivityForResult(intent, EDIT_NOTE_REQUEST)
        }
        recycler_view.adapter = adapter
    }

    // this is Intent activity result listener
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == ADD_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val title: String = data.getStringExtra(EXTRA_TITLE).toString()
            val description: String = data.getStringExtra(EXTRA_DESCRIPTION).toString()
            val priority: Int = data.getIntExtra(EXTRA_PRIORITY, -1)
            vm.insert(Note(title, description, priority))
            Toast.makeText(this, "Note inserted!", Toast.LENGTH_SHORT).show()
        } else if (data != null && requestCode == EDIT_NOTE_REQUEST && resultCode == Activity.RESULT_OK) {
            val id = data.getIntExtra(EXTRA_ID, -1)
            if (id == -1) {
                Toast.makeText(this, "Note couldn't be updated!", Toast.LENGTH_SHORT).show()
                return
            }
            val title: String = data.getStringExtra(EXTRA_TITLE).toString()
            val description: String =
                data.getStringExtra(EXTRA_DESCRIPTION).toString()
            val priority: Int = data.getIntExtra(EXTRA_PRIORITY, -1)
            vm.update(Note(title, description, priority, id))
            Toast.makeText(this, "Note updated!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Note Not Saved", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> {
                vm.deleteAllNotes()
                Toast.makeText(this, "All notes deleted!", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}