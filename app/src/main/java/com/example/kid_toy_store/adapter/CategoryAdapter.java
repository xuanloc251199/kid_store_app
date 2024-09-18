package com.example.kid_toy_store.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kid_toy_store.view.activity.DetailCategoryActivity;
import com.example.kid_toy_store.R;
import com.example.kid_toy_store.model.Categories;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Categories> categories;
    private Context mContext;

    public CategoryAdapter(List<Categories> categories, Context mContext) {
        this.categories = categories;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Categories categories = this.categories.get(position);
        holder.tvName.setText(categories.getName());

        // Sử dụng Picasso để tải hình ảnh từ URL
        Picasso.get().load(ApiUtils.IP_DEFAULT + categories.getThumbnail()).into(holder.imgThumbnail);

        holder.lnCategory.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DetailCategoryActivity.class);
            intent.putExtra(KeyUtils.CATEGORY_KEY, categories.getId());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        LinearLayout lnCategory;
        TextView tvName;
        ImageView imgThumbnail;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            lnCategory = itemView.findViewById(R.id.lnCategory);
            tvName = itemView.findViewById(R.id.tvNameCategory);
            imgThumbnail = itemView.findViewById(R.id.imgCategory);
        }
    }
}
