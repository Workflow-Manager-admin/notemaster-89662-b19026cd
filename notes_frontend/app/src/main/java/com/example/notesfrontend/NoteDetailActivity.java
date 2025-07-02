package com.example.notesfrontend;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Locale;

// PUBLIC_INTERFACE
public class NoteDetailActivity extends AppCompatActivity {
    /**
     * Activity for viewing note details.
     * Expects a Note passed via intent.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SettingsActivity.applyUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        Toolbar toolbar = findViewById(R.id.detailToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Note note = (Note) getIntent().getSerializableExtra("note");
        TextView title = findViewById(R.id.detailTitle);
        TextView content = findViewById(R.id.detailContent);
        TextView date = findViewById(R.id.detailDate);

        if (note != null) {
            title.setText(note.getTitle());
            content.setText(note.getContent());
            String formatted = SimpleDateFormat.getDateTimeInstance(
                    SimpleDateFormat.MEDIUM, SimpleDateFormat.SHORT, Locale.getDefault())
                    .format(note.getCreatedAt());
            date.setText(formatted);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
