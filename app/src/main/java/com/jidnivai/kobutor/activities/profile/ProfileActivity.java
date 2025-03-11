package com.jidnivai.kobutor.activities.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.activities.auth.LoginActivity;
import com.jidnivai.kobutor.activities.messaging.HomeActivity;
import com.jidnivai.kobutor.models.Image;
import com.jidnivai.kobutor.service.UserService;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public class ProfileActivity extends AppCompatActivity {

    private ImageView profileImageView;
    private TextView fullNameTextView, userNameTextView, emailTextView, genderTextView, dobTextView, phoneNumberTextView, addressTextView, rollsTextView;
    private Button changeProfilePictureButton, logoutButton, editProfileButton;

    private ConstraintLayout bgLayout;
    MediaPlayer mediaPlayer;


    SeekBar seekBar;
    private Handler handler = new Handler();
    UserService userService;

    private static final int PICK_IMAGE_REQUEST = 1; // For selecting profile picture

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userService = new UserService(this);

        // Initialize views
        profileImageView = findViewById(R.id.profileImageView);
        bgLayout = findViewById(R.id.bgLayout);
        userNameTextView = findViewById(R.id.userNameTextView);
        fullNameTextView = findViewById(R.id.fullNameTextView);
        emailTextView = findViewById(R.id.emailTextView);
        genderTextView = findViewById(R.id.genderTextView);
        dobTextView = findViewById(R.id.dobTextView);
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView);
        addressTextView = findViewById(R.id.addressTextView);
        rollsTextView = findViewById(R.id.rollsTextView);
        changeProfilePictureButton = findViewById(R.id.changeProfilePictureButton);
        logoutButton = findViewById(R.id.logoutButton);
        editProfileButton = findViewById(R.id.editProfileButton);

        setupMediaPlayer();

        // Simulate loading user profile data (this would be fetched from your backend)
        loadUserProfile();

        // Set up change profile picture button
        changeProfilePictureButton.setOnClickListener(v -> openGalleryOrCamera());

        // Set up logout button
        logoutButton.setOnClickListener(v -> {
            // Clear user session data
            SharedPreferences sharedPreferences = getSharedPreferences("kobutor", MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void loadUserProfile() {
        // Here, you'd fetch user profile data from the backend.
        // For demonstration, we set dummy data:
        SharedPreferences sharedPreferences = getSharedPreferences("kobutor", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String fullName = sharedPreferences.getString("fullName", "");
        String email = sharedPreferences.getString("email", "");
        String gender = sharedPreferences.getString("gender", "");
        String dob = sharedPreferences.getString("dob", "");
        String phoneNumber = sharedPreferences.getString("phoneNumber", "");
        String address = sharedPreferences.getString("address", "");
        String roles = sharedPreferences.getString("roles", "");
        try {
            Image profilePicture = HomeActivity.currentUser.getProfilePicture();
            Image coverPicture = HomeActivity.currentUser.getCoverPicture();

            String profileImageUrl = getResources().getString(R.string.api_url) + profilePicture.getUrl();
            Glide.with(this)
                    .load(profileImageUrl)
                    .placeholder(R.mipmap.ic_launcher_foreground)
                    .into(profileImageView);

            String coverImageUrl = getResources().getString(R.string.api_url) + coverPicture.getUrl();

            Glide.with(this)
                    .asBitmap() // Load image as Bitmap
                    .load(coverImageUrl)
                    .placeholder(R.drawable.default_cover)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            Drawable drawable = new BitmapDrawable(getResources(), resource);
                            bgLayout.setBackground(drawable); // Set the bitmap as background
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            bgLayout.setBackgroundResource(R.drawable.default_cover); // Set placeholder if cleared
                        }
                    });

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        // Load profile picture using Glide


        // Set username and status
        userNameTextView.setText(username);
        fullNameTextView.setText(fullName);
        emailTextView.setText(email);
        genderTextView.setText(gender);
        dobTextView.setText(dob);
        phoneNumberTextView.setText(phoneNumber);
        addressTextView.setText(address);
        rollsTextView.setText(roles);
//        statusTextView.setText(status);
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
            Uri imageUri = data.getData();
            if (imageUri != null) {
                Glide.with(this).load(imageUri).into(profileImageView);
                userService.changeProfilePicture(imageUri);
            }
        }
    }





    private void openGalleryOrCamera() {
        // Create an Intent to pick an image from the gallery or take a photo
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Create chooser to let the user pick between camera and gallery
        Intent chooser = Intent.createChooser(galleryIntent, "Select Image");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{cameraIntent});

        startActivityForResult(chooser, PICK_IMAGE_REQUEST);
    }


    private void setupMediaPlayer(){
        seekBar = findViewById(R.id.musicSeekBar);
        mediaPlayer = MediaPlayer.create(this, R.raw.somewhere_only_we_know);

        seekBar = findViewById(R.id.musicSeekBar);
        ImageButton playButton = findViewById(R.id.playButton);
//        mediaPlayer.setOnInfoListener((mp, what, extra) -> {
        playButton.setOnClickListener(v -> {

            if (mediaPlayer.isPlaying()){
                mediaPlayer.pause();
                playButton.setImageResource(R.drawable.baseline_play_circle_24);
                return;
            }
            mediaPlayer.start();
            playButton.setImageResource(R.drawable.baseline_pause_circle_24);
            updateProgressAndSeekBar();
        });
        mediaPlayer.setOnCompletionListener(mp -> {
            seekBar.setProgress(0);  // Reset seek bar when audio finishes
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                mediaPlayer.pause();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                mediaPlayer.start();
            }
        });
    }
    private void updateProgressAndSeekBar() {
        int totalDuration = mediaPlayer.getDuration();
        seekBar.setMax(totalDuration);

        // Use a handler to update the progress bar every 100ms
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                    handler.postDelayed(this, 100);  // Continue updating every 100ms
                }
            }
        }, 100);
    }
}
