package com.jidnivai.kobutor.activities.userandsettings;

// ProfileActivity.java
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.jidnivai.kobutor.R;

public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private EditText nameEditText, emailEditText;
    private Button updateProfileButton, changeProfilePictureButton;

    private static final int PICK_IMAGE_REQUEST = 1; // For selecting profile picture

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        profileImageView = findViewById(R.id.profileImageView);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        updateProfileButton = findViewById(R.id.updateProfileButton);
        changeProfilePictureButton = findViewById(R.id.changeProfilePictureButton);

        // Simulate loading user profile data (this would be fetched from your backend)
        loadUserProfile();

        // Set up change profile picture button
        changeProfilePictureButton.setOnClickListener(v -> openGallery());

        // Set up update profile button
        updateProfileButton.setOnClickListener(v -> updateProfile());
    }

    private void loadUserProfile() {
        // Here, you'd fetch user profile data from the backend.
        // For demonstration, we set dummy data:
        String profileImageUrl = "https://example.com/path/to/profile-pic.jpg";
        String fullName = "John Doe";
        String email = "johndoe@example.com";

        // Load profile picture using Glide
        Glide.with(this)
                .load(profileImageUrl)
                .placeholder(R.mipmap.icon)
                .into(profileImageView);

        // Set name and email
        nameEditText.setText(fullName);
        emailEditText.setText(email);
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

    private void updateProfile() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            // Update the profile (In reality, send a request to the backend)
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
