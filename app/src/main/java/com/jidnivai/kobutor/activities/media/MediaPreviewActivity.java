package com.jidnivai.kobutor.activities.media;

// MediaPreviewActivity.java
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.jidnivai.kobutor.R;

public class MediaPreviewActivity extends AppCompatActivity {

    ImageView imagePreview;
    VideoView videoPreview;
    EditText captionInput;
    Button sendButton, cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_preview);

        imagePreview = findViewById(R.id.imagePreview);
        videoPreview = findViewById(R.id.videoPreview);
        captionInput = findViewById(R.id.captionInput);
        sendButton = findViewById(R.id.sendButton);
        cancelButton = findViewById(R.id.cancelButton);

        // Get the media URI from the intent
        Intent intent = getIntent();
        Uri mediaUri = intent.getParcelableExtra("media_uri");
        String mediaType = intent.getStringExtra("media_type"); // "image" or "video"

        if (mediaUri != null) {
            if ("image".equals(mediaType)) {
                imagePreview.setImageURI(mediaUri);
                imagePreview.setVisibility(View.VISIBLE);
            } else if ("video".equals(mediaType)) {
                videoPreview.setVideoURI(mediaUri);
                videoPreview.setVisibility(View.VISIBLE);
                videoPreview.start();
            }
        }

        // Send Button Click
        sendButton.setOnClickListener(v -> {
            String caption = captionInput.getText().toString();
            sendMedia(mediaUri, caption);
        });

        // Cancel Button Click
        cancelButton.setOnClickListener(v -> finish());
    }

    private void sendMedia(Uri mediaUri, String caption) {
        // Simulate sending the media (in real implementation, upload to server)
        Toast.makeText(this, "Media sent with caption: " + caption +mediaUri, Toast.LENGTH_SHORT).show();
        finish(); // Close preview activity after sending
    }
}
