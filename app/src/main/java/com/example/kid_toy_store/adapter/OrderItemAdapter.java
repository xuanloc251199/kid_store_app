package com.example.kid_toy_store.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.model.Order;
import com.example.kid_toy_store.model.OrderItem;
import com.example.kid_toy_store.utils.ApiUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private List<OrderItem> orderItems;
    private Context context;

    public OrderItemAdapter(List<OrderItem> orderItems, Context context) {
        this.orderItems = (orderItems != null) ? orderItems : new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order_item, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);

        if (item.getProduct() != null) {
            holder.itemName.setText(item.getProduct().getName());
            holder.itemPrice.setText(item.getPrice() + " VNĐ");
            holder.itemQuantity.setText("Quantity: " + item.getQuantity());
            Picasso.get().load(ApiUtils.IP_DEFAULT + item.getProduct().getThumbnail()).into(holder.itemImage);
        } else if (item.getTicket() != null) {
            holder.itemName.setText(item.getTicket().getName());
            holder.itemPrice.setText(item.getPrice() + " VNĐ");
            holder.itemQuantity.setText("Quantity: " + item.getQuantity());
            Picasso.get().load(ApiUtils.IP_DEFAULT + item.getTicket().getThumbnail()).into(holder.itemImage);
        }
    }

    @Override
    public int getItemCount() {
        return (orderItems != null) ? orderItems.size() : 0;
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemQuantity, itemPrice;
        ImageView itemImage;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemQuantity = itemView.findViewById(R.id.itemQuantity);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            itemImage = itemView.findViewById(R.id.itemImage);
        }
    }
}
