package com.jidnivai.kobutor.adapters;

// ChatsAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.models.Chat;

import java.util.ArrayList;
import java.util.List;

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
        holder.timeTextView.setText(chat.getLastMessageTime().toString());
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

        public ChatViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewName);
            lastMessageTextView = itemView.findViewById(R.id.textViewLastMessage);
            timeTextView = itemView.findViewById(R.id.textViewTime);
            textViewUnread = itemView.findViewById(R.id.textViewUnread);
        }
    }
}
