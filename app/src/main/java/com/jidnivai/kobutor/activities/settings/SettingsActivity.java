package com.jidnivai.kobutor.activities.settings;

// SettingsActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.activities.auth.LoginActivity;

public class SettingsActivity extends AppCompatActivity {

    MaterialSwitch notificationSwitch;
    RadioGroup themeRadioGroup;
    RadioButton radioLight, radioDark;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Initialize views
        notificationSwitch = findViewById(R.id.notificationSwitch);
        themeRadioGroup = findViewById(R.id.themeRadioGroup);
        radioLight = findViewById(R.id.radioLight);
        radioDark = findViewById(R.id.radioDark);
        logoutButton = findViewById(R.id.logoutButton);

        // Set up notification switch
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                Toast.makeText(SettingsActivity.this, "Notifications Enabled", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SettingsActivity.this, "Notifications Disabled", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up theme radio buttons
        themeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioLight) {
                setTheme(R.style.AppTheme_Light);
                Toast.makeText(SettingsActivity.this, "Light Theme Selected", Toast.LENGTH_SHORT).show();
            } else if (checkedId == R.id.radioDark) {
                setTheme(R.style.AppTheme_Dark);
                Toast.makeText(SettingsActivity.this, "Dark Theme Selected", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up logout button
        logoutButton.setOnClickListener(v -> logout());
    }

    private void logout() {
        // Clear user session or token (Implement your logout logic here)
        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();  // Close the current activity
    }
}
