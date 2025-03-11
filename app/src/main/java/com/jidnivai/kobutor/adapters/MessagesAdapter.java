package com.jidnivai.kobutor.adapters;

// MessagesAdapter.java
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.models.Message;

import java.util.List;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageViewHolder> {

    private List<Message> messageList;
    private Long currentUserId = -1L;

    public MessagesAdapter(List<Message> messageList, Long currentUserId) {
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == 1) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_sent, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_message_received, parent, false);
        }
        return new MessageViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.messageTextView.setText(message.getMessage());
        Context context = holder.itemView.getContext();
        try{
            String url = context.getResources().getString(R.string.api_url) + message.getSender().getProfilePicture().getUrl();
            Glide
                    .with(context)
                    .load(url)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .into(holder.profileImageId);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // Return 1 if the message is sent by the user (isSent = true), else return 0 (received message)
        return messageList.get(position).getSender().getId().equals(currentUserId) ? 0 : 1;
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {

        TextView messageTextView;
        ImageView profileImageId;

        public MessageViewHolder(View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.textMessage);
            profileImageId = itemView.findViewById(R.id.profileImageId);
        }
    }
}
