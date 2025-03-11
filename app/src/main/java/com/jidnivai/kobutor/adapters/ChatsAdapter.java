package com.jidnivai.kobutor.adapters;

// ChatsAdapter.java
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.models.Chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ChatViewHolder> {

    List<Chat> chatList;
    List<Chat> oldChatList;
    OnChatClickListener listener;

    public ChatsAdapter(List<Chat> chatList,List<Chat> oldChatList, OnChatClickListener listener) {
        this.chatList = chatList;
        this.oldChatList = oldChatList;
        this.listener = listener;
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {
        Chat chat = chatList.get(position);
        Chat oldChat = oldChatList.stream().filter(c -> c.getId().equals(chat.getId())).findFirst().orElse(null);
        holder.nameTextView.setText(chat.getName());
        holder.lastMessageTextView.setText(chat.getLastMessage());
//        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime messageTime = chat.getLastMessageTime(); // Assuming this returns a LocalDateTime
            LocalDateTime now = LocalDateTime.now();

            // Check if the message is from today
            if (ChronoUnit.DAYS.between(messageTime, now) == 0) {
                // Message is today - use the "hh:mm a" format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault());
                String formattedTime = messageTime.format(formatter);
                holder.timeTextView.setText(formattedTime);
            } else if (ChronoUnit.DAYS.between(messageTime, now) == 1) {
                // Message was yesterday
                holder.timeTextView.setText("Yesterday");
            } else if (ChronoUnit.DAYS.between(messageTime, now) < 7) {
                // Message was within the last 7 days
                long daysAgo = ChronoUnit.DAYS.between(messageTime, now);
                holder.timeTextView.setText(String.format(Locale.getDefault(), "%d days ago", daysAgo));
            } else {
                // Message is older than 7 days - use full date format
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy", Locale.getDefault());
                String formattedDate = messageTime.format(dateFormatter);
                holder.timeTextView.setText(formattedDate);
            }
        }
        if(oldChat!=null){
            System.out.println(oldChat.getId());

            if(chat.getMessegeCount()==oldChat.getMessegeCount()){
                holder.textViewUnread.setVisibility(View.INVISIBLE);
            } else {
                holder.textViewUnread.setVisibility(View.VISIBLE);
                holder.textViewUnread.setText(chat.getMessegeCount()-oldChat.getMessegeCount() + "");
            }
        }
        if (oldChat==null){
            System.out.println("null");
            holder.textViewUnread.setText(chat.getMessegeCount()+"");
        }

//        holder.imageButton;
        if(chat.getGroupImage()!=null){
            Glide.with(holder.itemView.getContext())
                    .load(holder.imageButton.getContext().getResources().getString(R.string.api_url)+chat.getGroupImage().getUrl())
                    .placeholder(R.drawable.baseline_person_24)
                    .into(holder.imageButton);
        }

        holder.itemView.setOnClickListener(v -> {

            listener.onChatClick(chat);
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public interface OnChatClickListener {
        void onChatClick(Chat chat);
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView lastMessageTextView;
        TextView timeTextView;

        TextView textViewUnread;
        ImageButton imageButton;

        public ChatViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            lastMessageTextView = itemView.findViewById(R.id.textViewLastMessage);
            timeTextView = itemView.findViewById(R.id.textViewTime);
            textViewUnread = itemView.findViewById(R.id.textViewUnread);
            imageButton = itemView.findViewById(R.id.imageButton);
        }
    }
}
