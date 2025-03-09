package com.jidnivai.kobutor;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

public class WebSocketActivity extends AppCompatActivity {
    private WebSocketManager webSocketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_socket);

        webSocketManager = new WebSocketManager();
        webSocketManager.start();

        EditText editText = findViewById(R.id.messageInput);
        Button sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(view -> {
            String message = editText.getText().toString();
            webSocketManager.sendMessage(message);
            editText.setText("");
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webSocketManager.close();
    }
}
