package com.jidnivai.kobutor.activities.messaging;

// MainActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.activities.chat.ChatActivity;
import com.jidnivai.kobutor.activities.chat.NewChatActivity;
import com.jidnivai.kobutor.activities.settings.SettingsActivity;
import com.jidnivai.kobutor.adapters.ChatsAdapter;
import com.jidnivai.kobutor.models.Chat;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerViewChats;
    ChatsAdapter chatsAdapter;
    List<Chat> chatList = new ArrayList<>();
    FloatingActionButton fabNewChat;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerViewChats = findViewById(R.id.recyclerViewChats);
        fabNewChat = findViewById(R.id.fabNewChat);
        toolbar = findViewById(R.id.toolbar);

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
//        toolbar.setTitle("Recent Chats");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
//        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//        toolbar.setBackgroundColor(getResources().getColor(R.color.icon_background));
        //TODO work with tool bar
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu resource
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        View rootView = findViewById(android.R.id.content);
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(rootView.getContext(), SettingsActivity.class));
        }
        return true;
    }

    private void loadRecentChats() {
        // Simulate loading recent chats from the database or backend
        chatList.add(new Chat(1, "John Doe", "Hello!", "10:30 AM"));
        chatList.add(new Chat(2, "Jane Smith", "How's it going?", "9:45 AM"));
        chatList.add(new Chat(3, "Mike", "Let's meet up soon.", "8:00 AM"));
        chatsAdapter.notifyDataSetChanged();
    }


}
