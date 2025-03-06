package com.jidnivai.kobutor.activities.chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.service.MessageService;

public class ChatCreationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_creation);
        Intent intent = getIntent();
        Long userId = intent.getLongExtra("userId",-1);
        String fullName = intent.getStringExtra("fullName");
        String email = intent.getStringExtra("email");

        TextInputEditText chatName = findViewById(R.id.chatName);
        chatName.setText(fullName);
        TextView info = findViewById(R.id.info);
        info.setText("Creating text with id: " + fullName + "");

        Button createChat = findViewById(R.id.createChat);

        MessageService messageService = new MessageService(this);

        createChat.setOnClickListener(v -> {
            messageService.createChat(chatName.getText().toString(),userId,
                    chat -> {
                        Intent intent1 = new Intent(ChatCreationActivity.this, ChatActivity.class);
                        intent1.putExtra("chat", chat);
                        startActivity(intent1);
                    },
                    error -> {
                        error.printStackTrace();
                    }
                    );

        });
    }
}