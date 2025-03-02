package com.jidnivai.kobutor.activities.messaging;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jidnivai.kobutor.R;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ContactViewHolder> implements Filterable {

    private List<Contact> contactList;
    private List<Contact> contactListFull; // For filtering
    private OnContactClickListener listener;

    public ContactsAdapter(List<Contact> contactList, OnContactClickListener listener) {
        this.contactList = contactList;
        this.contactListFull = new ArrayList<>(contactList);
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
        Contact contact = contactList.get(position);
        holder.nameTextView.setText(contact.getName());

        holder.itemView.setOnClickListener(v -> listener.onContactClick(contact));
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    // Implement the filter logic
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Contact> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(contactListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Contact contact : contactListFull) {
                        if (contact.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(contact);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                contactList.clear();
                if (results.values != null) {
                    contactList.addAll((List<Contact>) results.values);
                }
                notifyDataSetChanged();
            }
        };
    }

    // Interface to handle contact clicks
    public interface OnContactClickListener {
        void onContactClick(Contact contact);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;

        public ContactViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewContactName);
        }
    }
}
