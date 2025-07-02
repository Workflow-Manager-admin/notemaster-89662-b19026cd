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
 * ProfileActivity - User profile and info page.
 */
public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SettingsActivity.applyUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setSelectedItemId(R.id.menu_profile);
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
            // already here
        } else if (itemId == R.id.menu_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            finish();
        }
    }
}
