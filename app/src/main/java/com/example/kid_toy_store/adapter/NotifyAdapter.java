package com.example.kid_toy_store.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.model.Notifications;
import com.example.kid_toy_store.model.Tags;
import com.example.kid_toy_store.response.NotificationResponse;
import com.google.android.material.chip.Chip;

import java.util.List;

public class NotifyAdapter extends RecyclerView.Adapter<NotifyAdapter.ViewHolder> {

    List<NotificationResponse> notifications;
    Context mContext;

    public NotifyAdapter(List<NotificationResponse> notifications, Context mContext) {
        this.notifications = notifications;
        this.mContext = mContext;
    }

    public NotifyAdapter(List<NotificationResponse> notifications) {
        this.notifications = notifications;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setNotifications(List<NotificationResponse> notifications) {
        this.notifications = notifications;
        notifyDataSetChanged(); // Cập nhật lại RecyclerView
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notify, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onBindViewHolder(@NonNull NotifyAdapter.ViewHolder holder, int position) {

        NotificationResponse notification = notifications.get(position);

        // Cài đặt nội dung cho TextView
        holder.tvNotifyTitle.setText(notification.getTitle());
        holder.tvNotify.setText(notification.getMessage());

        // Đổi màu và icon dựa trên type
        if (notification.getStatus().equals("unread")) {
            switch (notification.getType()) {
                case "success":
                    holder.cvNotify.setBackground(mContext.getDrawable(R.drawable.cardview_border_success));
                    holder.imgStatus.setImageResource(R.drawable.ic_success); // Đổi biểu tượng thành icon success
                    break;
                case "error":
                    holder.cvNotify.setBackground(mContext.getDrawable(R.drawable.cardview_border_error));
                    holder.imgStatus.setImageResource(R.drawable.ic_error); // Đổi biểu tượng thành icon error
                    break;
                case "warning":
                    holder.cvNotify.setBackground(mContext.getDrawable(R.drawable.cardview_border_warning));
                    holder.imgStatus.setImageResource(R.drawable.ic_warning); // Đổi biểu tượng thành icon warning
                    break;
                case "promotion":
                    holder.cvNotify.setBackground(mContext.getDrawable(R.drawable.cardview_border_warning));
                    holder.imgStatus.setImageResource(R.drawable.ic_discount); // Đổi biểu tượng thành icon warning
                    break;
                default:
                    holder.cvNotify.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
                    holder.imgStatus.setImageResource(R.drawable.ic_notification); // Icon mặc định
                    break;
            }
        } else if (notification.getStatus().equals("read")) {
            holder.cvNotify.setCardBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

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
        TextView tvNotifyTitle, tvNotify;
        ImageView imgStatus;
        CardView cvNotify;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNotifyTitle = itemView.findViewById(R.id.tvNotifyTitle);
            tvNotify = itemView.findViewById(R.id.tvNotify);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            cvNotify = itemView.findViewById(R.id.cvNotify);

        }
    }
}
