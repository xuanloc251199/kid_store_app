package com.example.kid_toy_store.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.activity.DetailProductActivity;
import com.example.kid_toy_store.formatter.CurrencyFormatter;
import com.example.kid_toy_store.model.CartItem;
import com.example.kid_toy_store.model.Products;
import com.example.kid_toy_store.utils.ApiUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CartProductAdapter extends RecyclerView.Adapter<CartProductAdapter.ViewHolder> {

    private List<CartItem> cartItems;
    Context mContext;

    public CartProductAdapter(List<CartItem> cartItems, Context mContext) {
        this.cartItems = cartItems;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_product, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CartItem cartItem = cartItems.get(position);
        Products productItem = cartItem.getProduct();
        String pricePrd = CurrencyFormatter.formatCurrency(productItem.getPrice());
        int numberCounter = cartItem.getQuantity();

        holder.tvNameProduct.setText(productItem.getName());
        holder.tvDetailProduct.setText(productItem.getDetail());
        holder.tvNumberCounter.setText("Số lượng: " + numberCounter);
        holder.tvTotalPrice.setText(pricePrd);
        Picasso.get().load(ApiUtils.IP_DEFAULT + productItem.getThumbnail()).into(holder.imgProduct);

        Log.d("CartAdapter", "Quantity: " + cartItem.getQuantity());

        holder.cvCartProduct.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, DetailProductActivity.class)));

//        holder.cvPlus.setOnClickListener(v -> {
//            // Tăng số lượng hiện tại
//            int currentQuantity = cartItem.getQuantity();
//            int updateCounter = currentQuantity + 1;
//
//            // Cập nhật giao diện và giá trị của sản phẩm
//            holder.tvNumberCounter.setText(String.valueOf(updateCounter));
//            cartItem.setQuantity(updateCounter);
//        });
//
//        holder.cvMinus.setOnClickListener(v -> {
//            // Giảm số lượng hiện tại
//            int currentQuantity = cartItem.getQuantity();
//            if (currentQuantity > 1) {
//                int updateCounter = currentQuantity - 1;
//
//                // Cập nhật giao diện và giá trị của sản phẩm
//                holder.tvNumberCounter.setText(String.valueOf(updateCounter));
//                cartItem.setQuantity(updateCounter);
//            } else if (currentQuantity == 1) {
//                // Nếu số lượng là 1 và nhấn nút trừ, thì xóa mục khỏi giỏ hàng
//                if (position >= 0 && position < this.cartItems.size()) {
//                    this.cartItems.remove(position);
//                    notifyItemRemoved(position);
//                    notifyItemRangeChanged(position, this.cartItems.size()); // Cập nhật lại chỉ số các mục còn lại
//                } else {
//                    Log.e("CartProductAdapter", "Invalid index: " + position + ", Size: " + this.cartItems.size());
//                }
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return cartItems == null ? 0 : cartItems.size();
    }

    // Xóa tất cả các item trong danh sách
    @SuppressLint("NotifyDataSetChanged")
    public void clearAllItems() {
        cartItems.clear();
        notifyDataSetChanged(); // Cập nhật toàn bộ RecyclerView sau khi xóa
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvCartProduct, cvMinus, cvPlus;
        ImageView imgProduct;
        TextView tvNameProduct, tvDetailProduct, tvNumberCounter, tvTotalPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvCartProduct = itemView.findViewById(R.id.cvCartProduct);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvDetailProduct = itemView.findViewById(R.id.tvDetailProduct);
            tvNumberCounter = itemView.findViewById(R.id.tvQuantity);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);

        }
    }
}
