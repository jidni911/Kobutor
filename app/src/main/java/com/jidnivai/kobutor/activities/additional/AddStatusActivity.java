package com.jidnivai.kobutor.activities.additional;

// AddStatusActivity.java
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.jidnivai.kobutor.R;

public class AddStatusActivity extends AppCompatActivity {

    private ImageView imagePreview;
    private VideoView videoPreview;
    private Button btnUploadStatus;
    private Uri selectedMediaUri;
    private boolean isVideo = false; // To check media type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_status);

        Button btnPickMedia = findViewById(R.id.btnPickMedia);
        imagePreview = findViewById(R.id.imagePreview);
        videoPreview = findViewById(R.id.videoPreview);
        btnUploadStatus = findViewById(R.id.btnUploadStatus);

        // Media picker intent
        ActivityResultLauncher<Intent> mediaPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedMediaUri = result.getData().getData();
                        String mimeType = getContentResolver().getType(selectedMediaUri);

                        if (mimeType != null && mimeType.startsWith("video")) {
                            // Video selected
                            isVideo = true;
                            videoPreview.setVisibility(View.VISIBLE);
                            imagePreview.setVisibility(View.GONE);
                            videoPreview.setVideoURI(selectedMediaUri);
                            videoPreview.start();
                        } else {
                            // Image selected
                            isVideo = false;
                            imagePreview.setVisibility(View.VISIBLE);
                            videoPreview.setVisibility(View.GONE);
                            Glide.with(this).load(selectedMediaUri).into(imagePreview);
                        }

                        // Show upload button
                        btnUploadStatus.setVisibility(View.VISIBLE);
                    }
                }
        );

        // Button to pick media
        btnPickMedia.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/* video/*"); // Allow both image & video selection
            mediaPickerLauncher.launch(intent);
        });

        // Upload button (Dummy Function)
        btnUploadStatus.setOnClickListener(v -> uploadStatus());
    }

    private void uploadStatus() {
        if (selectedMediaUri != null) {
            // TODO: Upload status to Firebase or your server
            // Currently, it just simulates an upload
            String message = isVideo ? "Uploading Video..." : "Uploading Image...";
            btnUploadStatus.setText(message);
        }
    }
}
