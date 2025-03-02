package com.jidnivai.kobutor.activities.messaging;

// MainActivity.java
import android.content.Intent;
import android.os.Bundle;
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
        chatsAdapter = new ChatsAdapter(chatList, chat -> {
            // Navigate to ChatActivity for the selected chat
            Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
            intent.putExtra("chatId", chat.getId());
            startActivity(intent);
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
        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));
        //TODO work with tool bar


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
        // Simulate loading recent chats from the database or backend
        chatList.add(new Chat(1, "Ayesha Akter", "Ami tomake onek miss kori! 💕", "10:30 AM"));
        chatList.add(new Chat(2, "Rupa Islam", "Bhalobasha kono shimana jane na... 🥰", "9:45 AM"));
        chatList.add(new Chat(3, "Mitu Rahman", "Tumi chara shob kichu shunno lage... 😘", "8:00 AM"));
        chatList.add(new Chat(4, "Nusrat Jahan", "Tumi jodi pashe thako, shob kichu shundor lage! ❤️", "7:15 AM"));
        chatList.add(new Chat(5, "Farzana Hossain", "Tumar hasi amake pagol kore dey! 😍", "6:50 AM"));
        chatList.add(new Chat(6, "Sadia Rahman", "Tumi amar jiboner shobcheye boro opohar! 🎁💖", "5:30 AM"));
        chatList.add(new Chat(7, "Jannatul Ferdous", "Tomar sathe kotha bollei mon valo hoy... ☺️", "4:45 AM"));
        chatList.add(new Chat(8, "Sharmin Akter", "Tumi amar shopnogulo'r shotti hoye uthecho! ✨", "3:20 AM"));
        chatList.add(new Chat(9, "Sultana Akter", "Tumi chara ekdin-o vebe dekhchi na! 💞", "2:00 AM"));
        chatList.add(new Chat(10, "Maliha Chowdhury", "Tumar chokh gulo akash-er moto sundor! 💙", "1:10 AM"));

        chatsAdapter.notifyDataSetChanged();
    }



}
