package com.example.notesfrontend;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.view.MenuItem;

/**
 * PUBLIC_INTERFACE
 * SettingsActivity - App settings and preferences page.
 */
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_NotesMaster_Light);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setSelectedItemId(R.id.menu_settings);
        navigation.setOnNavigationItemSelectedListener(item -> {
            navigateFromMenu(item.getItemId());
            return true;
        });
    }

    private void navigateFromMenu(int itemId) {
        if (itemId == R.id.menu_home) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else if (itemId == R.id.menu_notes) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else if (itemId == R.id.menu_search) {
            startActivity(new Intent(this, SearchActivity.class));
            finish();
        } else if (itemId == R.id.menu_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            finish();
        } else if (itemId == R.id.menu_settings) {
            // already here
        }
    }
}
