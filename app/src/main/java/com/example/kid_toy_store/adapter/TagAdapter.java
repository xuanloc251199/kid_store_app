package com.example.kid_toy_store.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.model.Tags;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.ViewHolder> {

    List<Tags> tags;
    Context mContext;

    public TagAdapter(List<Tags> tags) {
        this.tags = tags;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tag, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagAdapter.ViewHolder holder, int position) {

        Tags tag = tags.get(position);

        holder.chipTag.setOnCloseIconClickListener(v -> holder.chipTag.setVisibility(View.GONE));
        holder.chipTag.setText(tag.getTag());
        holder.chipTag.setCloseIconVisible(tag.isCloseable());

        // Xử lý sự kiện khi nhấn vào nút "X" trên Chip
        holder.chipTag.setOnCloseIconClickListener(v -> {
            // Kiểm tra chỉ số trước khi xóa để đảm bảo không vượt quá phạm vi
            if (position >= 0 && position < tags.size()) {
                // Xóa item khỏi danh sách và cập nhật RecyclerView
                tags.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, tags.size()); // Cập nhật lại chỉ số các item còn lại
            } else {
                Log.e("ChipAdapter", "Invalid index: " + position + ", Size: " + tags.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return tags == null ? 0 : tags.size();
    }

    // Xóa tất cả các item trong danh sách
    @SuppressLint("NotifyDataSetChanged")
    public void clearAllItems() {
        tags.clear();
        notifyDataSetChanged(); // Cập nhật toàn bộ RecyclerView sau khi xóa
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Chip chipTag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            chipTag = itemView.findViewById(R.id.chipTag);

        }
    }
}
