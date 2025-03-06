package com.jidnivai.kobutor.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.models.User;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder>  {

    private List<User> contactList;
    private OnContactClickListener listener;

    public ContactsAdapter(List<User> contactList, OnContactClickListener listener) {
        this.contactList = contactList;
        this.listener = listener;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        User contact = contactList.get(position);
        holder.nameTextView.setText(contact.getFullName());
        holder.emailTextView.setText(contact.getEmail());

        holder.itemView.setOnClickListener(v -> listener.onContactClick(contact));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    // Interface to handle contact clicks
    public interface OnContactClickListener {
        void onContactClick(User contact);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView emailTextView;

        public ContactViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewContactName);
            emailTextView = itemView.findViewById(R.id.textViewContactEmail);
        }
    }
}
