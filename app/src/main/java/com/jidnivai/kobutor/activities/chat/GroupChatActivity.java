package com.jidnivai.kobutor.activities.chat;

// GroupChatActivity.java
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.adapters.MessagesAdapter;
import com.jidnivai.kobutor.models.Message;

import java.util.ArrayList;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private Button btnSend;
    private TextView textViewGroupName;
    private MessagesAdapter messagesAdapter;
    private List<Message> messageList = new ArrayList<>();
    private String groupName = "Group Chat";  // You can set this dynamically based on the group

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        btnSend = findViewById(R.id.btnSend);
        textViewGroupName = findViewById(R.id.textViewGroupName);

        // Set the group name
        textViewGroupName.setText(groupName);

        // Set up RecyclerView
        recyclerViewMessages.setLayoutManager(new LinearLayoutManager(this));
        messagesAdapter = new MessagesAdapter(messageList);
        recyclerViewMessages.setAdapter(messagesAdapter);

        // Simulate incoming messages
        simulateIncomingMessages();

        // Handle the send button click
        btnSend.setOnClickListener(v -> {
            String messageText = editTextMessage.getText().toString().trim();

            if (!TextUtils.isEmpty(messageText)) {
                sendMessage(messageText);
            } else {
                Toast.makeText(GroupChatActivity.this, "Message cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void simulateIncomingMessages() {
        // Simulate receiving messages from different group members
        messageList.add(new Message("Hello, this is user 1!", "user1", false));
        messageList.add(new Message("Hi, I'm user 2. How's everyone?", "user2", false));
        messageList.add(new Message("This is user 3. Ready to chat!", "user3", false));
        messageList.add(new Message("Hey, user 1! I'm good, thanks!", "user4", true)); // Sent message
        messagesAdapter.notifyDataSetChanged();
    }

    private void sendMessage(String messageText) {
        // Add the sent message to the list
        messageList.add(new Message(messageText, "user4", true));

        // Update RecyclerView
        messagesAdapter.notifyItemInserted(messageList.size() - 1);
        recyclerViewMessages.scrollToPosition(messageList.size() - 1);

        // Clear the EditText
        editTextMessage.setText("");

        // Simulate receiving a response after a short delay
        recyclerViewMessages.postDelayed(() -> {
            messageList.add(new Message("Got your message!", "user2", false));
            messagesAdapter.notifyItemInserted(messageList.size() - 1);
            recyclerViewMessages.scrollToPosition(messageList.size() - 1);
        }, 1000);
    }
}
