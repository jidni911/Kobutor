package com.jidnivai.kobutor.adapters;

// StatusAdapter.java
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.jidnivai.kobutor.R;
import com.jidnivai.kobutor.models.StatusModel;
import com.jidnivai.kobutor.activities.status.ViewStatusActivity;

import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    private List<StatusModel> statusList;
    private Context context;

    public StatusAdapter(List<StatusModel> statusList, Context context) {
        this.statusList = statusList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StatusModel status = statusList.get(position);
        holder.userName.setText(status.getUserName());

        // Load image/video thumbnail using Glide
        Glide.with(context).load(status.getMediaUrl()).into(holder.statusImage);

        // Click to view full-screen status
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ViewStatusActivity.class);
            intent.putExtra("media_url", status.getMediaUrl());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return statusList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        ImageView statusImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userName);
            statusImage = itemView.findViewById(R.id.statusImage);
        }
    }
}
