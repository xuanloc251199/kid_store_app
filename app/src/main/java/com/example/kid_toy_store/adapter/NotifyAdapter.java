package com.example.kid_toy_store.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.model.Notifications;
import com.example.kid_toy_store.model.Tags;
import com.google.android.material.chip.Chip;

import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder> {

    List<Notifications> notifications;
    Context mContext;

    public NotifyAdapter(List<Notifications> notifications, Context mContext) {
        this.notifications = notifications;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notify, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifyAdapter.ViewHolder holder, int position) {

        Notifications notification = notifications.get(position);

        holder.tvNotify.setText(notification.getNotify());
        holder.imgStatus.setImageResource(R.drawable.ic_notification);
    }

    @Override
    public int getItemCount() {
        return notifications == null ? 0 : notifications.size();
    }

    // Xóa tất cả các item trong danh sách
    @SuppressLint("NotifyDataSetChanged")
    public void clearAllItems() {
        notifications.clear();
        notifyDataSetChanged(); // Cập nhật toàn bộ RecyclerView sau khi xóa
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNotify;
        ImageView imgStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNotify = itemView.findViewById(R.id.tvNotify);
            imgStatus = itemView.findViewById(R.id.imgStatus);

        }
    }
}
