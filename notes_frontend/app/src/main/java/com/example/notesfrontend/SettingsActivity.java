package com.example.notesfrontend;

import android.content.Intent;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

/**
 * PUBLIC_INTERFACE
 * SettingsActivity - App settings and preferences page, with theme switching functionality.
 */
public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Apply user preference for dark/light mode before super
        applyUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);

        // Add theme switcher Spinner just after the Toolbar
        LinearLayout rootLayout = null;
        View contentRoot = findViewById(android.R.id.content);
        if (contentRoot instanceof androidx.coordinatorlayout.widget.CoordinatorLayout) {
            View maybeLinear = ((androidx.coordinatorlayout.widget.CoordinatorLayout) contentRoot).getChildAt(0);
            if (maybeLinear instanceof LinearLayout) {
                rootLayout = (LinearLayout) maybeLinear;
            }
        }
        Spinner themeSpinner = new Spinner(this);
        String[] themes = {getString(R.string.theme_system), getString(R.string.theme_light), getString(R.string.theme_dark)};
        int[] themeValues = {AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, AppCompatDelegate.MODE_NIGHT_NO, AppCompatDelegate.MODE_NIGHT_YES};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, themes);
        themeSpinner.setAdapter(spinnerAdapter);

        // Find current mode
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int savedMode = prefs.getInt("app_theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        int selectedIndex = 0;
        for (int i = 0; i < themeValues.length; i++) {
            if (themeValues[i] == savedMode) selectedIndex = i;
        }
        themeSpinner.setSelection(selectedIndex);

        // Insert spinner after toolbar (assuming Toolbar is first child)
        if (rootLayout != null) {
            rootLayout.addView(themeSpinner, 1); // Insert at index 1 after toolbar
        }

        themeSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int pos, long id) {
                int selectedMode = themeValues[pos];
                if (selectedMode != savedMode) {
                    prefs.edit().putInt("app_theme_mode", selectedMode).apply();
                    AppCompatDelegate.setDefaultNightMode(selectedMode);
                    recreate();
                }
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setSelectedItemId(R.id.menu_settings);
        navigation.setOnNavigationItemSelectedListener(item -> {
            navigateFromMenu(item.getItemId());
            return true;
        });
    }

    /**
     * PUBLIC_INTERFACE
     * Applies the user's selected theme mode (system, light, or dark).
     * Call in onCreate before setContentView in every activity.
     */
    public static void applyUserTheme(android.content.Context ctx) {
        if (ctx == null) return;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ctx);
        int mode = prefs.getInt("app_theme_mode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        AppCompatDelegate.setDefaultNightMode(mode);
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
