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
import com.example.kid_toy_store.utils.KeyUtils;
import com.example.kid_toy_store.view.activity.DetailPlaygroundActivity;
import com.example.kid_toy_store.view.activity.DetailProductActivity;
import com.example.kid_toy_store.formatter.CurrencyFormatter;
import com.example.kid_toy_store.model.CartItem;
import com.example.kid_toy_store.model.Products;
import com.example.kid_toy_store.model.Tickets;
import com.example.kid_toy_store.utils.ApiUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

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

        if (cartItem.getType().equals("product") && cartItem.getProduct() != null) {
            // Xử lý hiển thị sản phẩm
            Products productItem = cartItem.getProduct();
            String pricePrd = CurrencyFormatter.formatCurrency(productItem.getFinalPrice());
            int numberCounter = cartItem.getQuantity();

            holder.tvNameProduct.setText(productItem.getName());
            holder.tvDetailProduct.setText(productItem.getDetail());
            holder.tvNumberCounter.setText("Số lượng: " + numberCounter);
            holder.tvTotalPrice.setText(pricePrd);
            Picasso.get().load(ApiUtils.IP_DEFAULT + productItem.getThumbnail()).into(holder.imgProduct);

        } else if (cartItem.getType().equals("ticket") && cartItem.getTicket() != null) {
            // Xử lý hiển thị vé
            Tickets ticketItem = cartItem.getTicket();
            String priceTkt = CurrencyFormatter.formatCurrency(ticketItem.getFinalPrice());
            int numberCounter = cartItem.getQuantity();

            holder.tvNameProduct.setText(ticketItem.getName());
            holder.tvDetailProduct.setText(ticketItem.getDetail());
            holder.tvNumberCounter.setText("Số lượng: " + numberCounter);
            holder.tvTotalPrice.setText(priceTkt);
            Picasso.get().load(ApiUtils.IP_DEFAULT + ticketItem.getThumbnail()).into(holder.imgProduct);

        } else {
            // Nếu dữ liệu không hợp lệ, ẩn item
            removeCartItem(position);
        }

        Log.d("CartAdapter", "Quantity: " + cartItem.getQuantity());

        holder.cvCartProduct.setOnClickListener(v -> {
            if (cartItem.getType().equals("product")) {
                Intent intent = new Intent(mContext, DetailProductActivity.class);
                intent.putExtra(KeyUtils.PRODUCT_KEY, cartItem.getProduct().getId());
                mContext.startActivity(intent);
            } else if (cartItem.getType().equals("ticket")) {
                // Điều hướng đến màn hình chi tiết vé nếu cần thiết
                Intent intent = new Intent(mContext, DetailPlaygroundActivity.class);
                intent.putExtra(KeyUtils.TICKET_KEY, cartItem.getTicket().getId());
                mContext.startActivity(intent);
            }
        });

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

    public List<CartItem> getCartItems() {
        return cartItems;
    }


    private void removeCartItem(int position) {
        if (position >= 0 && position < this.cartItems.size()) {
            this.cartItems.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, this.cartItems.size()); // Cập nhật lại chỉ số các item còn lại

            // Gọi API để xóa sản phẩm khỏi giỏ hàng nếu cần thiết
            // Ví dụ: deleteCartItem(cartItem.getId());
        } else {
            Log.e("CartProductAdapter", "Invalid index: " + position + ", Size: " + this.cartItems.size());
        }
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
