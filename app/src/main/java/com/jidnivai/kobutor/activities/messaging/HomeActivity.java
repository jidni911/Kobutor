package com.jidnivai.kobutor.activities.messaging;

// MainActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jidnivai.kobutor.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerViewChats;
    ChatsAdapter chatsAdapter;
    List<Chat> chatList = new ArrayList<>();
    FloatingActionButton fabNewChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerViewChats = findViewById(R.id.recyclerViewChats);
        fabNewChat = findViewById(R.id.fabNewChat);

        // Set up RecyclerView
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));
        chatsAdapter = new ChatsAdapter(chatList, new ChatsAdapter.OnChatClickListener() {
            @Override
            public void onChatClick(Chat chat) {
                // Navigate to ChatActivity for the selected chat
                Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                intent.putExtra("chatId", chat.getId());
                startActivity(intent);
            }
        });
        recyclerViewChats.setAdapter(chatsAdapter);

        // Simulate loading some recent chats
        loadRecentChats();

        // Handle FAB click to start a new chat
        fabNewChat.setOnClickListener(v -> {
            // Open NewChatActivity or GroupChatActivity
            Intent intent = new Intent(HomeActivity.this, NewChatActivity.class);
            startActivity(intent);
        });
    }

    private void loadRecentChats() {
        // Simulate loading recent chats from the database or backend
        chatList.add(new Chat(1, "John Doe", "Hello!", "10:30 AM"));
        chatList.add(new Chat(2, "Jane Smith", "How's it going?", "9:45 AM"));
        chatList.add(new Chat(3, "Mike", "Let's meet up soon.", "8:00 AM"));
        chatsAdapter.notifyDataSetChanged();
    }
}
