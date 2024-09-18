package com.example.kid_toy_store.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.formatter.PromotionFormatter;
import com.example.kid_toy_store.view.activity.DetailProductActivity;
import com.example.kid_toy_store.formatter.CurrencyFormatter;
import com.example.kid_toy_store.formatter.QuantityFormatter;
import com.example.kid_toy_store.model.Products;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    List<Products> products;
    Context mContext;

    public ProductAdapter(List<Products> products, Context mContext) {
        this.products = products;
        this.mContext = mContext;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
        notifyDataSetChanged();  // Cập nhật lại RecyclerView khi có thay đổi về dữ liệu
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {

        Products products = this.products.get(position);
        String pricePrd = CurrencyFormatter.formatCurrency(products.getFinalPrice());
        String soldPrd = QuantityFormatter.formatQuantity(products.getSold());

        holder.tvNameProduct.setText(products.getName());
        holder.tvPriceProduct.setText(pricePrd);

        if (products.getPromotion() != 0){
            holder.tvPromotion.setText(PromotionFormatter.formatPromotion(products.getPromotion()));
        } else holder.tvPromotion.setVisibility(View.GONE);

        if (products.getSold() == 0){
            holder.tvSoldProduct.setVisibility(View.GONE);
        } else {
            holder.tvSoldProduct.setText(soldPrd);
        }

        Picasso.get().load(ApiUtils.IP_DEFAULT + products.getThumbnail()).into(holder.imgProduct);

        holder.cvProduct.setOnClickListener(v -> {

            Intent intent = new Intent(mContext, DetailProductActivity.class);
            intent.putExtra(KeyUtils.PRODUCT_KEY, products.getId());
            mContext.startActivity(intent);

        });
    }



    @Override
    public int getItemCount() {
        return products == null ? 0 : products.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cvProduct;
        ImageView imgProduct;
        TextView tvNameProduct, tvPriceProduct, tvSoldProduct, tvPromotion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cvProduct = itemView.findViewById(R.id.cvProduct);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            tvNameProduct = itemView.findViewById(R.id.tvNameProduct);
            tvPriceProduct = itemView.findViewById(R.id.tvPriceProduct);
            tvPromotion = itemView.findViewById(R.id.tvPromotion);
            tvSoldProduct = itemView.findViewById(R.id.tvSoldProduct);

        }
    }
}
