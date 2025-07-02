package com.example.notesfrontend;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ArrayAdapter;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

/**
 * PUBLIC_INTERFACE
 * SearchActivity - Dedicated search screen with a modern, minimal design.
 */
public class SearchActivity extends AppCompatActivity {

    private NotesRepository repository;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_NotesMaster_Light);
        setContentView(R.layout.activity_search);

        Toolbar toolbar = findViewById(R.id.searchToolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setSelectedItemId(R.id.menu_search);
        navigation.setOnNavigationItemSelectedListener(item -> {
            navigateFromMenu(item.getItemId());
            return true;
        });

        repository = new NotesRepository(this);
        ListView searchList = findViewById(R.id.searchListView);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        searchList.setAdapter(adapter);

        SearchView searchView = findViewById(R.id.searchBar);
        searchView.setQueryHint(getString(R.string.hint_search));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                doSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                doSearch(newText);
                return true;
            }
        });

        // Launch detail on note selection (by title)
        searchList.setOnItemClickListener((parent, view, position, id) -> {
            String title = adapter.getItem(position);
            // Try to find Note by title
            List<Note> resultNotes = repository.searchNotes(title == null ? "" : title);
            if (resultNotes.size() > 0) {
                Note found = resultNotes.get(0);
                Intent detail = new Intent(SearchActivity.this, NoteDetailActivity.class);
                detail.putExtra("note", found);
                startActivity(detail);
            }
        });
    }

    private void doSearch(String query) {
        adapter.clear();
        if (!TextUtils.isEmpty(query)) {
            for (Note note : repository.searchNotes(query)) {
                String display = (note.getTitle().isEmpty() ? "(Untitled)" : note.getTitle());
                adapter.add(display);
            }
        }
    }

    private void navigateFromMenu(int itemId) {
        if (itemId == R.id.menu_home) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else if (itemId == R.id.menu_notes) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (itemId == R.id.menu_search) {
            // already here
        } else if (itemId == R.id.menu_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        } else if (itemId == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        }
    }
}
