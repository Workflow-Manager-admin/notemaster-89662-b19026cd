package com.example.notesfrontend;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

// PUBLIC_INTERFACE
public class EditNoteActivity extends AppCompatActivity {
    /**
     * Activity for creating and editing a note.
     * If a Note is provided via intent, operates in 'edit' mode.
     */
    private EditText etTitle, etContent;
    private boolean isEditMode = false;
    private Note originalNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Toolbar toolbar = findViewById(R.id.editToolbar);
        setSupportActionBar(toolbar);

        etTitle = findViewById(R.id.editNoteTitle);
        etContent = findViewById(R.id.editNoteContent);
        Button btnSave = findViewById(R.id.btnSaveNote);

        // Check if we are editing an existing note
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("note")) {
            isEditMode = true;
            originalNote = (Note) intent.getSerializableExtra("note");
            if (originalNote != null) {
                etTitle.setText(originalNote.getTitle());
                etContent.setText(originalNote.getContent());
            }
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String content = etContent.getText().toString().trim();
            if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
                Toast.makeText(this, "Please enter note title or content", Toast.LENGTH_SHORT).show();
                return;
            }

            NotesRepository repo = new NotesRepository(this);
            long timestamp = System.currentTimeMillis();

            if (isEditMode && originalNote != null) {
                originalNote.setTitle(title);
                originalNote.setContent(content);
                repo.updateNote(originalNote);
                setResult(RESULT_OK, new Intent().putExtra("note", originalNote));
                finish();
            } else {
                Note note = new Note(title, content, timestamp);
                long newId = repo.addNote(note);
                note.setId(newId);
                setResult(RESULT_OK, new Intent().putExtra("note", note));
                finish();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
