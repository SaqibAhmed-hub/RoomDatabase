package com.example.roomdatabase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.add_edit_note.*

const val EXTRA_ID = "com.example.roomdatabase.EXTRA_ID"
const val EXTRA_TITLE = "com.example.roomdatabase.EXTRA_TITLE"
const val EXTRA_DESCRIPTION = "com.example.roomdatabase.EXTRA_DESCRIPTION"
const val EXTRA_PRIORITY = "com.example.roomdatabase.EXTRA_PRIORITY"

class AddEditNoteActivity : AppCompatActivity() {

    private lateinit var mode: Mode
    private var noteId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_edit_note)

        number_picker_priority.minValue = 1
        number_picker_priority.maxValue = 10

        // This will setup the back Icon in toolbar
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)

        noteId = intent.getIntExtra(EXTRA_ID, -1)
        mode = if (noteId == -1) Mode.AddNote else Mode.EditNote
        when (mode) {
            Mode.AddNote -> title = "Add Note"
            Mode.EditNote -> {
                title = "Edit Note"
                ed_title.setText(intent.getStringExtra(EXTRA_TITLE))
                ed_description.setText(intent.getStringExtra(EXTRA_DESCRIPTION))
                number_picker_priority.value = intent.getIntExtra(EXTRA_PRIORITY, -1)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.add_note_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_save_note -> {
                saveNote()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNote() {
        val title = ed_title.text.toString()
        val desc = ed_description.text.toString()
        val priority = number_picker_priority.value

        if (title.isEmpty() || desc.isEmpty()) {
            Toast.makeText(this, "Please Insert title and description", Toast.LENGTH_SHORT).show()
            return
        }

        val data = Intent()
        if (noteId != -1)
            data.putExtra(EXTRA_ID, noteId)
        data.putExtra(EXTRA_TITLE, title)
        data.putExtra(EXTRA_DESCRIPTION, desc)
        data.putExtra(EXTRA_PRIORITY, priority)

        setResult(Activity.RESULT_OK, data)
        finish()
    }

    private sealed class Mode {
        object AddNote : Mode()
        object EditNote : Mode()

    }
}
