package com.jidnivai.kobutor.activities.messaging;

// NewChatActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.jidnivai.kobutor.R;

import java.util.ArrayList;
import java.util.List;

public class NewChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewContacts;
    private ContactsAdapter contactsAdapter;
    private List<Contact> contactList = new ArrayList<>();
    private Button buttonCreateGroup;
    private SearchView searchViewContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);
        buttonCreateGroup = findViewById(R.id.buttonCreateGroup);
        searchViewContacts = findViewById(R.id.searchViewContacts);

        // Set up RecyclerView
        recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));
        contactsAdapter = new ContactsAdapter(contactList, new ContactsAdapter.OnContactClickListener() {
            @Override
            public void onContactClick(Contact contact) {
                // Navigate to ChatActivity with the selected contact
                Intent intent = new Intent(NewChatActivity.this, ChatActivity.class);
                intent.putExtra("contactId", contact.getId());
                startActivity(intent);
            }
        });
        recyclerViewContacts.setAdapter(contactsAdapter);

        // Simulate loading contacts
        loadContacts();

        // Handle the "Create Group Chat" button click
        buttonCreateGroup.setOnClickListener(v -> {
            // Handle creating a group chat (can open another activity to select multiple contacts)
            Toast.makeText(NewChatActivity.this, "Group Chat creation coming soon!", Toast.LENGTH_SHORT).show();
        });

        // Handle Search View for filtering contacts
        searchViewContacts.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Call getFilter to filter the contact list
                contactsAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    private void loadContacts() {
        // Simulate loading contacts from the backend or database
        contactList.add(new Contact(1, "John Doe"));
        contactList.add(new Contact(2, "Jane Smith"));
        contactList.add(new Contact(3, "Mike Johnson"));
        contactList.add(new Contact(4, "Emily Davis"));
        contactsAdapter.notifyDataSetChanged();
    }
}
