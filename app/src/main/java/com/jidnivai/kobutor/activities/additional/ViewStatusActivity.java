package com.jidnivai.kobutor.activities.additional;
// ViewStatusActivity.java
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.jidnivai.kobutor.R;

public class ViewStatusActivity extends AppCompatActivity {

    private ImageView imageView, btnClose;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_status);

        imageView = findViewById(R.id.imageView);
        videoView = findViewById(R.id.videoView);
        btnClose = findViewById(R.id.btnClose);

        // Get media URL from Intent
        String mediaUrl = getIntent().getStringExtra("media_url");

        if (mediaUrl != null) {
            if (mediaUrl.endsWith(".mp4")) {
                // Show video
                videoView.setVisibility(View.VISIBLE);
                videoView.setVideoURI(Uri.parse(mediaUrl));
                videoView.start();
            } else {
                // Show image
                imageView.setVisibility(View.VISIBLE);
                Glide.with(this).load(mediaUrl).into(imageView);
            }
        }

        // Close button listener
        btnClose.setOnClickListener(v -> finish());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
