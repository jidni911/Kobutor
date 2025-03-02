package com.jidnivai.kobutor.activities.additional;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jidnivai.kobutor.R;

import java.util.ArrayList;
import java.util.List;

public class StatusActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    StatusAdapter statusAdapter;
    List<StatusModel> statusList;
    FloatingActionButton fabAddStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        recyclerView = findViewById(R.id.recyclerViewStatus);
        fabAddStatus = findViewById(R.id.fabAddStatus);

        // Sample Status List
        statusList = new ArrayList<>();
        statusList.add(new StatusModel("Alice", "https://example.com/image1.jpg", System.currentTimeMillis()));
        statusList.add(new StatusModel("Bob", "https://example.com/video.mp4", System.currentTimeMillis()));
        statusList.add(new StatusModel("Charlie", "https://example.com/image2.jpg", System.currentTimeMillis()));

        // Setup RecyclerView
        statusAdapter = new StatusAdapter(statusList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(statusAdapter);

        // FAB Click - Add new status
        fabAddStatus.setOnClickListener(v -> {
            Intent intent = new Intent(StatusActivity.this, AddStatusActivity.class);
            startActivity(intent);
        });
    }
}