package com.example.notesfrontend;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * MainActivity is the entry point for the NotesMaster app.
 * It displays the list of notes and features the add, edit, delete, view, and search functionalities.
 */
public class MainActivity extends AppCompatActivity implements NotesAdapter.OnNoteListener {

    private static final int REQ_CODE_NEW_NOTE = 1;
    private static final int REQ_CODE_EDIT_NOTE = 2;

    private NotesRepository repository;
    private NotesAdapter adapter;
    private List<Note> noteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SettingsActivity.applyUserTheme(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);

        repository = new NotesRepository(this);
        noteList = repository.getAllNotes();

        RecyclerView recyclerView = findViewById(R.id.recyclerViewNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesAdapter(this, noteList, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabAddNote);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
            startActivityForResult(intent, REQ_CODE_NEW_NOTE);
        });

        // Bottom navigation logic
        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setSelectedItemId(R.id.menu_notes);
        navigation.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.menu_home) {
                startActivity(new Intent(this, HomeActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.menu_notes) {
                // already here
                return true;
            } else if (itemId == R.id.menu_search) {
                startActivity(new Intent(this, SearchActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.menu_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                finish();
                return true;
            } else if (itemId == R.id.menu_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                finish();
                return true;
            }
            return false;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reloadNotes();
    }

    private void reloadNotes() {
        noteList = repository.getAllNotes();
        adapter.setNotes(noteList);
    }

    @Override
    public void onNoteClicked(Note note) {
        Intent intent = new Intent(this, NoteDetailActivity.class);
        intent.putExtra("note", note);
        startActivity(intent);
    }

    @Override
    public void onNoteEdit(Note note) {
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQ_CODE_EDIT_NOTE);
    }

    @Override
    public void onNoteDelete(Note note) {
        repository.deleteNote(note.getId());
        reloadNotes();
        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            reloadNotes();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) search.getActionView();
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        }
        searchView.setQueryHint("Search notes...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterNotes(query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filterNotes(newText);
                return true;
            }
        });
        return true;
    }

    private void filterNotes(String query) {
        if (TextUtils.isEmpty(query)) {
            adapter.setNotes(repository.getAllNotes());
        } else {
            adapter.setNotes(repository.searchNotes(query));
        }
    }
}
