package com.jidnivai.kobutor.activities.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.activities.auth.LoginActivity;
import com.jidnivai.kobutor.activities.messaging.HomeActivity;
import com.jidnivai.kobutor.models.Image;
import com.jidnivai.kobutor.models.User;

public class OtherProfileActivity extends AppCompatActivity {

    private User user;

    private ImageView profileImageView;
    private TextView fullNameTextView, userNameTextView, emailTextView, genderTextView, dobTextView, phoneNumberTextView, addressTextView, rollsTextView;
    private Button message;

    private ConstraintLayout bgLayout;
    MediaPlayer mediaPlayer;


    SeekBar seekBar;
    private Handler handler = new Handler();

    private static final int PICK_IMAGE_REQUEST = 1; // For selecting profile picture
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
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
        message = findViewById(R.id.message);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            user = getIntent().getSerializableExtra("user",User.class);
        }

        setupMediaPlayer();

        // Simulate loading user profile data (this would be fetched from your backend)
        loadUserProfile();

        // Set up change profile picture button

        // Set up logout button
        message.setOnClickListener(v -> {
            finish();
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
    }

    private void loadUserProfile() {

        try {
            Image profilePicture = user.getProfilePicture();
            Image coverPicture = user.getCoverPicture();

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
        userNameTextView.setText(user.getUsername());
        fullNameTextView.setText(user.getFullName());
        emailTextView.setText(user.getEmail());
        genderTextView.setText(user.getGender().toString());
        dobTextView.setText(user.getDob().toString());
        phoneNumberTextView.setText(user.getPhoneNumber());
        addressTextView.setText(user.getAddress());
        rollsTextView.setText(user.getRoles().stream().map(v -> v.getName()).reduce("", (a, b) -> a + ", " + b));
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
            // Get the image URI and load it into the ImageView using Glide
            Glide.with(this)
                    .load(data.getData())
                    .into(profileImageView);
        }
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
