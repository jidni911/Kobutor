package com.jidnivai.kobutor.activities.chat;

// ChatActivity.java

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
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

    private Toolbar toolbarM;

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
        toolbarM = findViewById(R.id.toolbarM);

        SharedPreferences sharedPreferences = getSharedPreferences("kobutor", MODE_PRIVATE);
        currentUserId = sharedPreferences.getLong("id", -1);

//        setSupportActionBar(toolbarM);
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbarM.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(ChatActivity.this, ChatInfoActivity.class);
            intent.putExtra("chat",chat);
            startActivity(intent);
        });

        if (chat != null && chat.getName() != null) {
            toolbarM.setTitle(chat.getName());
            chatId = chat.getId();
        } else {
            toolbarM.setTitle("Chat");
        }
        try {
            if(chat!=null && chat.getGroupImage() != null) {


                String navIconUrl = getResources().getString(R.string.api_url) + chat.getGroupImage().getUrl();

                Glide.with(this)
                        .asBitmap()
                        .load(navIconUrl)
                        .circleCrop() // Ensures a circular icon
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                int size = 120;//(int) getResources().getDimension(R.dimen.toolbar_icon_size); // Define a proper size in res/values/dimens.xml
                                Bitmap scaledBitmap = Bitmap.createScaledBitmap(resource, size, size, true);
                                Drawable drawable = new BitmapDrawable(getResources(), scaledBitmap);
                                toolbarM.setNavigationIcon(drawable);
                                toolbarM.setPadding(40, 0, 0, 0);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                toolbarM.setNavigationIcon(R.drawable.ic_launcher_foreground);
                            }
                        });
            }
        }catch (NullPointerException e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
