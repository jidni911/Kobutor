package com.jidnivai.kobutor.activities.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.activities.auth.LoginActivity;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView usernameTextView, statusTextView;
    private Button changeProfilePictureButton, logoutButton;

    private static final int PICK_IMAGE_REQUEST = 1; // For selecting profile picture

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        profileImageView = findViewById(R.id.profileImageView);
        usernameTextView = findViewById(R.id.usernameTextView);
        statusTextView = findViewById(R.id.statusTextView);
        changeProfilePictureButton = findViewById(R.id.changeProfilePictureButton);
        logoutButton = findViewById(R.id.logoutButton);

        // Simulate loading user profile data (this would be fetched from your backend)
        loadUserProfile();

        // Set up change profile picture button
        changeProfilePictureButton.setOnClickListener(v -> openGallery());

        // Set up logout button
        logoutButton.setOnClickListener(v -> {
            // Clear user session data
            SharedPreferences sharedPreferences = getSharedPreferences("kobutor", MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void loadUserProfile() {
        // Here, you'd fetch user profile data from the backend.
        // For demonstration, we set dummy data:
        SharedPreferences sharedPreferences = getSharedPreferences("kobutor", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        String profileImageUrl = "https://example.com/path/to/profile-pic.jpg";
        String status = "Hey there! I'm using WhatsApp.";

        // Load profile picture using Glide
//        Glide.with(this)
//                .load(profileImageUrl)
//                .placeholder(R.mipmap.icon)
//                .into(profileImageView);

        // Set username and status
        usernameTextView.setText(username);
        statusTextView.setText(status);
    }

    private void openGallery() {
        // Open gallery to select a new profile picture
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            // Get the image URI and load it into the ImageView using Glide
            Glide.with(this)
                    .load(data.getData())
                    .into(profileImageView);
        }
    }
}
