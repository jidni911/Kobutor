package com.jidnivai.kobutor.activities.chat;

// NewChatActivity.java
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.adapters.ContactsAdapter;
import com.jidnivai.kobutor.models.User;
import com.jidnivai.kobutor.service.MessageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewChatActivity extends AppCompatActivity {

    private RecyclerView recyclerViewContacts;
    private ContactsAdapter contactsAdapter;
    private List<User> contactList = new ArrayList<>();
    private Button buttonCreateGroup;
    private SearchView searchViewContacts;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat);

        // Binding
        recyclerViewContacts = findViewById(R.id.recyclerViewContacts);
        buttonCreateGroup = findViewById(R.id.buttonCreateGroup);
        searchViewContacts = findViewById(R.id.searchViewContacts);
        toolbar = findViewById(R.id.toolbar);

        toolbar.setNavigationOnClickListener(v -> {
            this.finish();
        });


        // Simulate loading contacts
//        loadContacts();

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
                if(!Objects.equals(newText, "")) {
                    loadContacts(newText);
                }
                return false;
            }
        });

    }

    private void loadContacts(String query) {
        // Simulate loading contacts from the backend or database
        MessageService messageService = new MessageService(this);
        messageService.searchContact(query,
                users -> {
                    contactList = users;

                    // Set up RecyclerView
                    recyclerViewContacts.setLayoutManager(new LinearLayoutManager(this));
                    contactsAdapter = new ContactsAdapter(contactList, contact -> {
                        // Navigate to ChatActivity with the selected contact
                        Intent intent = new Intent(NewChatActivity.this, ChatCreationActivity.class);
                        intent.putExtra("userId", contact.getId());
                        intent.putExtra("fullName", contact.getFullName());
                        intent.putExtra("email", contact.getEmail());
                        startActivity(intent);
                    });
                    recyclerViewContacts.setAdapter(contactsAdapter);
                    contactsAdapter.notifyDataSetChanged();

                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
