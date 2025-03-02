package com.jidnivai.kobutor.activities.additional;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jidnivai.kobutor.R;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView recyclerView;
    SearchAdapter searchAdapter;
    List<String> allItems; // This will hold the full list of contacts, messages, or groups
    List<String> filteredItems; // This will hold filtered results

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerViewResults);

        // Initialize data (Replace with actual contacts/messages/groups)
        allItems = new ArrayList<>();
        allItems.add("Alice");
        allItems.add("Bob");
        allItems.add("Charlie");
        allItems.add("Group: Study Buddies");
        allItems.add("Message: Hey, how are you?");
        allItems.add("Message: Meeting at 5 PM");

        filteredItems = new ArrayList<>(allItems);

        // Set up RecyclerView
        searchAdapter = new SearchAdapter(filteredItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(searchAdapter);

        // Search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterResults(newText);
                return true;
            }
        });
    }

    private void filterResults(String query) {
        filteredItems.clear();
        if (TextUtils.isEmpty(query)) {
            filteredItems.addAll(allItems);
        } else {
            for (String item : allItems) {
                if (item.toLowerCase().contains(query.toLowerCase())) {
                    filteredItems.add(item);
                }
            }
        }
        searchAdapter.notifyDataSetChanged();
    }
}