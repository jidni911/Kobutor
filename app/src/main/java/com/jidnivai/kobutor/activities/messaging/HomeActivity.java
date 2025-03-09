package com.jidnivai.kobutor.activities.messaging;

// MainActivity.java

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.jidnivai.kobutor.models.User;
import com.jidnivai.kobutor.service.MessageService;
import com.jidnivai.kobutor.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HomeActivity extends AppCompatActivity {

    public static User currentUser;

    RecyclerView recyclerViewChats;
    ChatsAdapter chatsAdapter;
    List<Chat> chatList = new ArrayList<>();
    List<Chat> oldChatList = new ArrayList<>();
    FloatingActionButton fabNewChat;

    Toolbar toolbar;

    MessageService messageService;


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

        UserService userService = new UserService(this);
        Long currentUserId = getSharedPreferences("kobutor", MODE_PRIVATE).getLong("id", 0L);
        userService.getUserById(currentUserId, user -> {
                    currentUser = user;
            Toast.makeText(this, "Welcome " + user.getFullName() + "!", Toast.LENGTH_SHORT).show();
                }, error -> {
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
        );

        setContentView(R.layout.activity_home);
        messageService = new MessageService(this);
        recyclerViewChats = findViewById(R.id.recyclerViewChats);
        fabNewChat = findViewById(R.id.fabNewChat);
        toolbar = findViewById(R.id.toolbar);

        // Set up RecyclerView
        chatsAdapter = new ChatsAdapter(chatList, oldChatList, chat -> {

            List<Chat> temp = oldChatList.stream().filter(c -> !c.getId().equals(chat.getId())).collect(Collectors.toCollection(ArrayList::new));
            temp.add(chat);
            oldChatList.clear();
            oldChatList.addAll(temp);
            chatsAdapter.notifyDataSetChanged();
            // Navigate to ChatActivity for the selected chat
            Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
            intent.putExtra("chat", chat);
            startActivity(intent);
        });
        recyclerViewChats.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewChats.setAdapter(chatsAdapter);

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
        } else if (item.getItemId() == R.id.app_bar_key) {
            showTokenDialog();
        }
        return true;
    }

    private void loadRecentChats() {
        messageService.loadAllChats(
                chats -> {
                    if (chats.isEmpty()) return;
                    String seq1 = chats.stream().map(v -> v.getId().toString() + v.getMessegeCount()).collect(Collectors.joining(","));
                    String seq2 = chatList.stream().map(v -> v.getId().toString() + v.getMessegeCount()).collect(Collectors.joining(","));

                    boolean isNewChat = chatList.isEmpty() ||
                            !seq1.equals(seq2);

                    if (isNewChat) {
                        chatList.clear();
                        chatList.addAll(chats);
                        if (oldChatList.isEmpty()) {
                            oldChatList.addAll(chats);
                        }
                        chatsAdapter.notifyDataSetChanged();

//                        int scrollPosition = recyclerViewChats.computeVerticalScrollOffset();
//                        int totalHeight = recyclerViewChats.computeVerticalScrollRange();
//                        int screenHeight = recyclerViewChats.getHeight();
//
//                        boolean isNearBottom = (scrollPosition + screenHeight) >= (totalHeight - 300); // 300px threshold
//
//                        if (isNearBottom) {
//                            recyclerViewChats.scrollToPosition(chatsAdapter.getItemCount() - 1);
//                        }
                    }

                },
                error -> {
                    Log.e("Volley Error", error.toString());
                }
        );


    }


    private void showTokenDialog() {
        SharedPreferences sharedPreferences = getSharedPreferences("kobutor", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");

        // Create a vertical LinearLayout
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 20);

        // Create an EditText for token input
        EditText editTextToken = new EditText(this);
        editTextToken.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        editTextToken.setHint("Enter or paste JWT token");
        editTextToken.setText(token);
        layout.addView(editTextToken);

        // Create a Paste Button
        Button btnCopy = new Button(this);
        btnCopy.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        btnCopy.setText("Copy");
        layout.addView(btnCopy);
        // Create a Paste Button
        Button btnPaste = new Button(this);
        btnPaste.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        btnPaste.setText("Paste");
        layout.addView(btnPaste);

        // Create AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("JWT Token")
                .setView(layout)
                .setPositiveButton("Save", (dialog, which) -> {
                    String newToken = editTextToken.getText().toString();
                    sharedPreferences.edit().putString("token", newToken).apply();
                    Toast.makeText(this, "Token saved!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Clear", (dialog, which) -> {
                    sharedPreferences.edit().remove("token").apply();
                });

        AlertDialog dialog = builder.create();
        dialog.show();

        btnCopy.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("jwt_token", token);
            clipboard.setPrimaryClip(clip);

            Toast.makeText(this, "Copied to clipboard!", Toast.LENGTH_SHORT).show();
        });

        // Paste Button Click Listener
        btnPaste.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard.hasPrimaryClip() && clipboard.getPrimaryClip().getItemCount() > 0) {
                String pastedText = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
                editTextToken.setText(pastedText);
                Toast.makeText(this, "Pasted from clipboard!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
