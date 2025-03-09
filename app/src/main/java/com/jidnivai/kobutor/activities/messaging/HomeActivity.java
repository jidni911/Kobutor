package com.jidnivai.kobutor.activities.messaging;

// MainActivity.java

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.activities.chat.ChatActivity;
import com.jidnivai.kobutor.activities.chat.NewChatActivity;
import com.jidnivai.kobutor.activities.profile.ProfileActivity;
import com.jidnivai.kobutor.activities.settings.SettingsActivity;
import com.jidnivai.kobutor.adapters.ChatsAdapter;
import com.jidnivai.kobutor.models.Chat;
import com.jidnivai.kobutor.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerViewChats;
    ChatsAdapter chatsAdapter;
    List<Chat> chatList = new ArrayList<>();
    List<Chat> oldChatList = new ArrayList<>();
    FloatingActionButton fabNewChat;

    Toolbar toolbar;


    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable refreshChatsRunnable = new Runnable() {
        @Override
        public void run() {
            loadRecentChats();
            handler.postDelayed(this, 2000); // Repeat after 2 seconds
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        recyclerViewChats = findViewById(R.id.recyclerViewChats);
        fabNewChat = findViewById(R.id.fabNewChat);
        toolbar = findViewById(R.id.toolbar);

        // Set up RecyclerView
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));
//        chatsAdapter = new ChatsAdapter(chatList, chat -> {
//            // Navigate to ChatActivity for the selected chat
//            Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
//            intent.putExtra("chatId", chat.getId());
//            startActivity(intent);
//        });
//        recyclerViewChats.setAdapter(chatsAdapter);

        // Simulate loading some recent chats
        loadRecentChats();
        handler.post(refreshChatsRunnable);


        // Handle FAB click to start a new chat
        fabNewChat.setOnClickListener(v -> {
            // Open NewChatActivity or GroupChatActivity
            Intent intent = new Intent(HomeActivity.this, NewChatActivity.class);
            startActivity(intent);
        });
        fabNewChat.setLongClickable(true);
        fabNewChat.setOnLongClickListener(v -> {
            Toast.makeText(this, "Refreshing", Toast.LENGTH_SHORT).show();
            loadRecentChats();
            return true;
        });
//        toolbar.setTitle("Recent Chats");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));
        //TODO work with tool bar

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(refreshChatsRunnable); // Stop the interval when activity is destroyed
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu resource
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        // Optional: Configure the search view
        searchView.setQueryHint("Search here...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search submission
                Toast.makeText(HomeActivity.this, "Searching: " + query, Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search text change
                return false;
            }
        });
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
        MessageService messageService = new MessageService(this);
        messageService.loadAllChats(
                chats -> {
                    chatList = chats;
                    if(oldChatList.isEmpty()){
                        oldChatList = chats;
                    }
                    chatsAdapter = new ChatsAdapter(chatList,oldChatList, chat -> {

                        oldChatList = oldChatList.stream().filter(c-> !c.getId().equals(chat.getId())).collect(Collectors.toCollection(ArrayList::new));
                        oldChatList.add(chat);
                        // Navigate to ChatActivity for the selected chat
                        Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                        intent.putExtra("chat", chat);
                        startActivity(intent);
                    });
                    recyclerViewChats.setAdapter(chatsAdapter);
//                    chatsAdapter.notifyDataSetChanged();

                },
                error -> {
                    Log.e("Volley Error", error.toString());
                }
        );


    }


}
