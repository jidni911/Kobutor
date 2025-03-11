package com.jidnivai.kobutor.activities.chat;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.models.Chat;
import com.jidnivai.kobutor.models.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ChatInfoActivity extends AppCompatActivity {

    Chat chat;

    private TextView chatName, chatType, chatCreator, lastMessage, lastMessageTime,
            chatCreatedAt, chatUpdatedAt, messageCount;
    private ImageView groupImage;
    private ListView membersList, requestedMembersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_info);
        chat = (Chat) getIntent().getSerializableExtra("chat");

        chatName = findViewById(R.id.chatName);
        chatType = findViewById(R.id.chatType);
        chatCreator = findViewById(R.id.chatCreator);
        lastMessage = findViewById(R.id.lastMessage);
        lastMessageTime = findViewById(R.id.lastMessageTime);
        chatCreatedAt = findViewById(R.id.chatCreatedAt);
        chatUpdatedAt = findViewById(R.id.chatUpdatedAt);
        messageCount = findViewById(R.id.messageCount);
        groupImage = findViewById(R.id.groupImage);
        membersList = findViewById(R.id.membersList);
        requestedMembersList = findViewById(R.id.requestedMembersList);

        if (chat != null) {
            // Set data
            chatName.setText(chat.getName());
            chatType.setText(chat.isGroup() ? "Group Chat" : "Private Chat");
            chatCreator.setText("Created by: " + chat.getCreator().getFullName());

            if (chat.getGroupImage() != null) {
                groupImage.setVisibility(View.VISIBLE);
                Glide.with(this).load(getResources().getString(R.string.api_url) + chat.getGroupImage().getUrl()).into(groupImage);
//                groupImage.setImageBitmap(chat.getGroupImage().getBitmap()); // Assuming you have a method to get Bitmap
            }

            lastMessage.setText("Last Message: " + chat.getLastMessage());
            lastMessageTime.setText("Last Message Time: " + formatTime(chat.getLastMessageTime()));
            chatCreatedAt.setText("Created At: " + formatTime(chat.getCreatedAt()));
            chatUpdatedAt.setText("Updated At: " + formatTime(chat.getUpdatedAt()));
            messageCount.setText("Total Messages: " + chat.getMessegeCount());

            // Populate members list
            List<String> memberNames = chat.getMembers().stream().map(User::getFullName).collect(Collectors.toList());
            membersList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, memberNames));

            // Populate requested members if available
            if (chat.getRequestedMembers() != null && !chat.getRequestedMembers().isEmpty()) {
                requestedMembersList.setVisibility(View.VISIBLE);
                List<String> requestedNames = chat.getRequestedMembers().stream().map(User::getFullName).collect(Collectors.toList());
                requestedMembersList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, requestedNames));
            }
        }

    }

    private String formatTime(LocalDateTime dateTime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return dateTime != null ? dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "N/A";
        }
        return "N/A";
    }
}