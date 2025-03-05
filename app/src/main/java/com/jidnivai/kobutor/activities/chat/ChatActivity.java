package com.jidnivai.kobutor.activities.chat;

// ChatActivity.java
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Chat chat = getIntent().getParcelableExtra("chat");

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        btnSend = findViewById(R.id.btnSend);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(ChatActivity.this, ProfileActivity.class)));
        toolbar.setTitle(chat.getName());

        // Set up RecyclerView
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        messagesAdapter = new MessagesAdapter(messageList);
        recyclerViewMessages.setAdapter(messagesAdapter);



        // Simulate receiving a message from the other user
        simulateIncomingMessages();
        MessageService messageService = new MessageService(this);


        // Handle the send button click
        btnSend.setOnClickListener(v -> {
            String messageText = editTextMessage.getText().toString().trim();

            if (!TextUtils.isEmpty(messageText)) {
                sendMessage(messageText);
            } else {
                Toast.makeText(ChatActivity.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void simulateIncomingMessages() {
        // Simulate receiving messages from the other user
//        messageList.add(new Message("Hello!", "user", false)); // Incoming message
//        messageList.add(new Message("Hi there! How are you?", "other", true)); // Sent message
        messagesAdapter.notifyDataSetChanged();
    }

    private void sendMessage(String messageText) {
        // Add the sent message to the list
//        messageList.add(new Message(messageText, "user", true));

        // Update RecyclerView
        messagesAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerViewMessages.scrollToPosition(messageList.size() - 1);

        // Clear the EditText
        editTextMessage.setText("");

        // Simulate receiving a response after a short delay
        recyclerViewMessages.postDelayed(() -> {
//            messageList.add(new Message("Got your message!", "other", false));
            messagesAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerViewMessages.scrollToPosition(messageList.size() - 1);
        }, 1000);
    }
}
