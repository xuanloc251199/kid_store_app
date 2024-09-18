package com.example.kid_toy_store.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.model.Order;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orders;
    private Context context;

    public OrderAdapter(List<Order> orders, Context context) {
        this.orders = orders;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);

        holder.orderId.setText("Order ID: " + order.getId());
        holder.status.setText("Status: " + order.getStatus());
        holder.totalPrice.setText("Total: " + order.getTotal_price() + " VNĐ");
        holder.createdAt.setText("Created At: " + order.getCreatedAt());

        // Set up OrderItemAdapter
        OrderItemAdapter itemAdapter = new OrderItemAdapter(order.getOrderItems(), context);
        holder.orderItemsRecyclerView.setAdapter(itemAdapter);
        holder.orderItemsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderId, status, totalPrice, createdAt;
        RecyclerView orderItemsRecyclerView;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderId = itemView.findViewById(R.id.orderId);
            status = itemView.findViewById(R.id.status);
            totalPrice = itemView.findViewById(R.id.totalPrice);
            createdAt = itemView.findViewById(R.id.createdAt);
            orderItemsRecyclerView = itemView.findViewById(R.id.orderItemsRecyclerView);
        }
    }
}
