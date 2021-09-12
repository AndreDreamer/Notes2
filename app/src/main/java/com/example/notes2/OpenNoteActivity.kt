
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.notes2.MainActivity
import com.example.notes2.Note


class OpenNoteActivity : Activity() {
    lateinit var title: EditText
    lateinit var text: EditText
    var index = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        active = true
        super.onCreate(savedInstanceState)
        setContentView(R.layout.open_note)
        title = findViewById(R.id.editTitle)
        text = findViewById(R.id.editText)
        index = intent.getIntExtra("NoteID", 0)
        if (index == -1) {
            title.setHint("Edit title")
            text.setHint("Put notes")
        } else {
            val note: Note = MainActivity.notes.get(index)
            title.setText(note.title)
            text.setText(note.text)
        }
        val ok = findViewById<Button>(R.id.buttonOK)
        ok.setOnClickListener { finish() }
        removeService()
    }

    private fun removeService() {
        run { stopService(Intent(this@OpenNoteActivity, FloatingWidgetService::class.java)) }
    }

    override fun finish() {
        if (!text!!.text.toString().isEmpty()) {
            var header = title!!.text.toString()
            val plot = text!!.text.toString()
            if (title!!.text.toString().isEmpty()) {
                header = if (plot.length > 15) plot.substring(0, 15) + "..." else plot
            }
            if (index == -1) {
                val note = Note(header, plot)
                MainActivity.notes.add(note)
            } else {
                val note = Note(header, plot)
                MainActivity.notes.set(index, note)
            }
            MainActivity.db.putNotes(MainActivity.notes)
        }
        val myIntent = Intent(this, MainActivity::class.java)
        myIntent.putExtra("NoteID", -1)
        startActivity(myIntent)
        super.finish()
    }

    override fun onResume() {
        super.onResume()
        active = true
        removeService()
    }

    override fun onStop() {
        super.onStop()
        active = false
        if (!MainActivity.active && !active) {
            // To prevent starting the service if the required permission is NOT granted.
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)) {
                startService(
                    Intent(
                        this@OpenNoteActivity,
                        FloatingWidgetService::class.java
                    ).putExtra("activity_background", true)
                )
            } else {
                errorToast()
            }
        }
    }

    private fun errorToast() {
        Toast.makeText(
            this,
            "Draw over other app permission not available. Can't start the application without the permission.",
            Toast.LENGTH_LONG
        ).show()
    }

    companion object {
        var active = false
    }
}