package com.jidnivai.kobutor.activities.chat;

// ChatActivity.java

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.activities.messaging.HomeActivity;
import com.jidnivai.kobutor.activities.profile.ProfileActivity;
import com.jidnivai.kobutor.adapters.MessagesAdapter;
import com.jidnivai.kobutor.models.Chat;
import com.jidnivai.kobutor.models.Message;
import com.jidnivai.kobutor.service.MessageService;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private Button btnSend;
    private MessagesAdapter messagesAdapter;
    private List<Message> messageList = new ArrayList<>();

    private Toolbar toolbar;

    private Long currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Chat chat = (Chat) getIntent().getSerializableExtra("chat");

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        btnSend = findViewById(R.id.btnSend);
        toolbar = findViewById(R.id.toolbar);

        SharedPreferences sharedPreferences = getSharedPreferences("kobutor", MODE_PRIVATE);
        currentUserId = sharedPreferences.getLong("id", -1);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(ChatActivity.this, ProfileActivity.class)));
        toolbar.post(() -> {
            if (chat != null && chat.getName() != null) {
                toolbar.setTitle(chat.getName());
            } else {
                toolbar.setTitle("Chat");
            }
        });


        // Simulate receiving a message from the other user

        MessageService messageService = new MessageService(this);
        messageService.loadChat(chat.getId(),
                messages -> {
                    messageList = messages;
                    // Set up RecyclerView
                    recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
                    messagesAdapter = new MessagesAdapter(messageList, currentUserId);
                    recyclerViewMessages.setAdapter(messagesAdapter);
                    recyclerViewMessages.scrollToPosition(messagesAdapter.getItemCount() - 1);
                },
                error -> {
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );


        // Handle the send button click
        btnSend.setOnClickListener(v -> {
            String messageText = editTextMessage.getText().toString().trim();

            if (!TextUtils.isEmpty(messageText)) {
                sendMessage(messageText, chat.getId());
            } else {
                Toast.makeText(ChatActivity.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void sendMessage(String messageText, Long id) {
        MessageService messageService = new MessageService(this);
        messageService.sendMessage(messageText, id,
                message -> {
                    messageList.add(message);
                    recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
                    messagesAdapter = new MessagesAdapter(messageList, currentUserId);
                    recyclerViewMessages.setAdapter(messagesAdapter);
                    editTextMessage.setText("");//TODO works here
                    recyclerViewMessages.scrollToPosition(messagesAdapter.getItemCount() - 1);
                },
                error -> {
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
}
