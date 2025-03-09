package com.jidnivai.kobutor.activities.chat;

// ChatActivity.java

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

    private Long chatId;

    MessageService messageService ;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable refreshChatsRunnable = new Runnable() {
        @Override
        public void run() {
            loadMessages();
            handler.postDelayed(this, 2000); // Repeat after 2 seconds
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messageService = new MessageService(this);
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

        if (chat != null && chat.getName() != null) {
            toolbar.setTitle(chat.getName());
            chatId = chat.getId();
        } else {
            toolbar.setTitle("Chat");
        }

        // Initialize RecyclerView once
        messagesAdapter = new MessagesAdapter(messageList, currentUserId);
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewMessages.setAdapter(messagesAdapter);

        if (chatId != null) {
            loadMessages();
            handler.post(refreshChatsRunnable);
        }

        // Handle Send Button Click
        btnSend.setOnClickListener(v -> {
            String messageText = editTextMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(messageText)) {
                sendMessage(messageText, chatId);
            } else {
                Toast.makeText(ChatActivity.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void sendMessage(String messageText, Long chatId) {
        MessageService messageService = new MessageService(this);
        messageService.sendMessage(messageText, chatId,
                message -> {
                    messageList.add(message);
                    messagesAdapter.notifyItemInserted(messageList.size() - 1);
                    editTextMessage.setText("");

                    // Scroll only if user is at the bottom
                    if (!recyclerViewMessages.canScrollVertically(1)) {
                        recyclerViewMessages.scrollToPosition(messagesAdapter.getItemCount() - 1);
                    }
                },
                error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }


    private void loadMessages() {

        messageService.loadChat(chatId,
                messages -> {
                    if (messages.isEmpty()) return;

                    // Check if the last message has changed
                    boolean isNewMessage = messageList.isEmpty() ||
                            !messages.get(messages.size() - 1).getId().equals(
                                    messageList.get(messageList.size() - 1).getId());

                    if (isNewMessage) {
                        messageList.clear();
                        messageList.addAll(messages);
                        messagesAdapter.notifyDataSetChanged();

                        // Check if the user is near the bottom before scrolling
                        int scrollPosition = recyclerViewMessages.computeVerticalScrollOffset();
                        int totalHeight = recyclerViewMessages.computeVerticalScrollRange();
                        int screenHeight = recyclerViewMessages.getHeight();

                        boolean isNearBottom = (scrollPosition + screenHeight) >= (totalHeight - 300); // 300px threshold

                        if (isNearBottom) {
                            recyclerViewMessages.scrollToPosition(messagesAdapter.getItemCount() - 1);
                        }
                    }
                },
                error -> Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(refreshChatsRunnable); // Stop the interval when activity is destroyed
    }
}
