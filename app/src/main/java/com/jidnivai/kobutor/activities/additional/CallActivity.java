package com.jidnivai.kobutor.activities.additional;

// CallActivity.java
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

import com.jidnivai.kobutor.R;

public class CallActivity extends AppCompatActivity {

    private VideoView videoCallView;
    private TextView voiceCallPlaceholder;
    private Button btnSwitchToVideo, btnEndCall;
    private boolean isVideoCall = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);

        // Initialize UI elements
        videoCallView = findViewById(R.id.videoCallView);
        voiceCallPlaceholder = findViewById(R.id.voiceCallPlaceholder);
        btnSwitchToVideo = findViewById(R.id.btnSwitchToVideo);
        btnEndCall = findViewById(R.id.btnEndCall);

        // Start a voice call by default
        startVoiceCall();

        // Button to switch between voice and video call
        btnSwitchToVideo.setOnClickListener(v -> switchToVideoCall());

        // Button to end the call
        btnEndCall.setOnClickListener(v -> endCall());
    }

    private void startVoiceCall() {
        // Hide video view, show voice call placeholder
        videoCallView.setVisibility(View.GONE);
        voiceCallPlaceholder.setVisibility(View.VISIBLE);

        // Hide the "Switch to Video" button for voice calls
        btnSwitchToVideo.setVisibility(View.VISIBLE);

        // Set initial state to voice call
        isVideoCall = false;
    }

    private void switchToVideoCall() {
        if (!isVideoCall) {
            // Switch to video call: hide voice placeholder, show video view
            voiceCallPlaceholder.setVisibility(View.GONE);
            videoCallView.setVisibility(View.VISIBLE);

            // Replace the text on the button
            btnSwitchToVideo.setText("Switch to Voice");

            // Set state to video call
            isVideoCall = true;
        } else {
            // Switch back to voice call
            startVoiceCall();
        }
    }

    private void endCall() {
        // Finish the activity to end the call
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        endCall();
    }
}
