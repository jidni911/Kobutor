package com.jidnivai.kobutor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.jidnivai.kobutor.activities.chat.ChatActivity;
import com.jidnivai.kobutor.activities.messaging.HomeActivity;
import com.jidnivai.kobutor.activities.chat.NewChatActivity;
import com.jidnivai.kobutor.activities.chat.GroupChatActivity;
import com.jidnivai.kobutor.activities.settings.SettingsActivity;
import com.jidnivai.kobutor.activities.profile.ProfileActivity;
import com.jidnivai.kobutor.activities.auth.LoginActivity;
import com.jidnivai.kobutor.activities.auth.RegisterActivity;
import com.jidnivai.kobutor.activities.auth.ForgotPasswordActivity;
import com.jidnivai.kobutor.activities.status.AddStatusActivity;
import com.jidnivai.kobutor.activities.calls.CallActivity;
import com.jidnivai.kobutor.activities.media.MediaPreviewActivity;
import com.jidnivai.kobutor.activities.search.SearchActivity;
import com.jidnivai.kobutor.activities.status.StatusActivity;
import com.jidnivai.kobutor.activities.status.ViewStatusActivity;
import com.jidnivai.kobutor.service.AuthService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        Button btnHome = findViewById(R.id.btnHome);
        Button btnChat = findViewById(R.id.btnChat);
        Button btnNewChat = findViewById(R.id.btnNewChat);
        Button btnGroupChats = findViewById(R.id.btnGroupChats);
        Button btnSettings = findViewById(R.id.btnSettings);
        Button btnProfile = findViewById(R.id.btnProfile);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnForgotPassword = findViewById(R.id.btnForgotPassword);
        Button btnAddStatus = findViewById(R.id.btnAddStatus);
        Button btnCall = findViewById(R.id.btnCall);
        Button btnMediaPreview = findViewById(R.id.btnMediaPreview);
        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnStatus = findViewById(R.id.btnStatus);
        Button btnViewStatus = findViewById(R.id.btnViewStatus);

        // Set click listeners to open activities
        btnHome.setOnClickListener(v -> openActivity(HomeActivity.class));
        btnChat.setOnClickListener(v -> openActivity(ChatActivity.class));
        btnNewChat.setOnClickListener(v -> openActivity(NewChatActivity.class));
        btnGroupChats.setOnClickListener(v -> openActivity(GroupChatActivity.class));
        btnSettings.setOnClickListener(v -> openActivity(SettingsActivity.class));
        btnProfile.setOnClickListener(v -> openActivity(ProfileActivity.class));
        btnLogin.setOnClickListener(v -> openActivity(LoginActivity.class));
        btnRegister.setOnClickListener(v -> openActivity(RegisterActivity.class));
        btnForgotPassword.setOnClickListener(v -> openActivity(ForgotPasswordActivity.class));
        btnAddStatus.setOnClickListener(v -> openActivity(AddStatusActivity.class));
        btnCall.setOnClickListener(v -> openActivity(CallActivity.class));
        btnMediaPreview.setOnClickListener(v -> openActivity(MediaPreviewActivity.class));
        btnSearch.setOnClickListener(v -> openActivity(SearchActivity.class));
        btnStatus.setOnClickListener(v -> openActivity(StatusActivity.class));
        btnViewStatus.setOnClickListener(v -> openActivity(ViewStatusActivity.class));

        AuthService authService = new AuthService(this);
        authService.echo(()->{
            Toast.makeText(this, "Server is alive", Toast.LENGTH_SHORT).show();
        },()->{
            Toast.makeText(this, "Server is dead", Toast.LENGTH_SHORT).show();
        });

        SharedPreferences sharedPreferences = getSharedPreferences("kobutor", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            openActivity(HomeActivity.class);
        } else {
            openActivity(LoginActivity.class);
        }

//        try {
//            Thread.sleep(1000);
//            btnHome.performClick();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        btnLogin.performClick();
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }
}
