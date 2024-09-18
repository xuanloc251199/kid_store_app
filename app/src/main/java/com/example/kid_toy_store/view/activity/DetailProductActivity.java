package com.example.kid_toy_store.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.text.LineBreaker;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kid_toy_store.api.ApiService;
import com.example.kid_toy_store.api.RetrofitClient;
import com.example.kid_toy_store.databinding.ActivityDetailProductBinding;
import com.example.kid_toy_store.formatter.CurrencyFormatter;
import com.example.kid_toy_store.formatter.QuantityFormatter;
import com.example.kid_toy_store.model.CartItem;
import com.example.kid_toy_store.request.CartRequest;
import com.example.kid_toy_store.model.Products;
import com.example.kid_toy_store.response.CartResponse;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;
import com.squareup.picasso.Picasso;

import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity {

    ActivityDetailProductBinding mBinding;
    View mView;

    AtomicInteger numberCounter = new AtomicInteger(1);

    private ApiService apiService;

    String token;
    double totalPrice;
    int productId, updateCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityDetailProductBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();
        setContentView(mView);

        // Khởi tạo Retrofit
        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        // Lấy access_token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(KeyUtils.ACCESS_TOKEN, null);

        getCart();

        setOnClickListener(mBinding);

        mBinding.tvDescription.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);

        productId = getIntent().getIntExtra(KeyUtils.PRODUCT_KEY, -1);

        getProductById(productId);
    }

    private void getProductById(int productId) {

        Call<Products> call = apiService.getProductById(productId);

        call.enqueue(new Callback<Products>() {
            @Override
            public void onResponse(Call<Products> call, Response<Products> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Products products = response.body();

                    mBinding.tvNameProduct.setText(products.getName());
                    mBinding.tvFinalPriceProduct.setText(CurrencyFormatter.formatCurrency(products.getFinalPrice()));
                    mBinding.tvPriceProduct.setText(CurrencyFormatter.formatCurrency(products.getPrice()));
                    mBinding.tvDescription.setText(products.getDescription());
                    mBinding.tvSoldProduct.setText(QuantityFormatter.formatQuantity(products.getSold()));
                    mBinding.tvTotalPrice.setText(CurrencyFormatter.formatCurrency(products.getFinalPrice()));

                    totalPrice = products.getFinalPrice();

                    Picasso.get().load(ApiUtils.IP_DEFAULT + products.getThumbnail()).into(mBinding.imgProduct);

                } else {
                    Toast.makeText(DetailProductActivity.this, "Không thể tải danh sách sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Products> call, Throwable t) {
                Toast.makeText(DetailProductActivity.this, "Lỗi khi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setOnClickListener(ActivityDetailProductBinding binding) {

        binding.cvBack.setOnClickListener(v -> finish());
        binding.cvCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        binding.cvBackHome.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
        binding.btnAddToCart.setOnClickListener(v -> {
            addToCart();
            getCart();
            updateCounter = 1;
        });

        binding.tvNumberCounter.setText(String.valueOf(numberCounter.get()));

        binding.cvPlus.setOnClickListener(v -> {
            updateCounter = numberCounter.get() + 1;
            binding.tvNumberCounter.setText(String.valueOf(updateCounter));

            double totalUpdate = totalPrice * updateCounter;

            binding.tvTotalPrice.setText(CurrencyFormatter.formatCurrency(totalUpdate));

            numberCounter.set(updateCounter);
        });

        binding.cvMinus.setOnClickListener(v -> {

            if (numberCounter.get() > 1) {
                updateCounter = numberCounter.get() - 1;
                binding.tvNumberCounter.setText(String.valueOf(updateCounter));

                double totalUpdate = totalPrice * updateCounter;

                binding.tvTotalPrice.setText(CurrencyFormatter.formatCurrency(totalUpdate));

                numberCounter.set(updateCounter);
            }
        });

    }

    private void addToCart() {
        int quantity = Integer.parseInt(mBinding.tvNumberCounter.getText().toString().trim());

        // Cập nhật đối tượng CartItem để chứa cả `type`
        CartRequest cartRequest = new CartRequest(productId, "product", quantity);  // Giả sử "product" là type cho sản phẩm

        Call<CartItem> call = apiService.addToCart("Bearer " + token, cartRequest);

        call.enqueue(new Callback<CartItem>() {
            @Override
            public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    getCart();
                    Toast.makeText(DetailProductActivity.this, "Sản phẩm đã được thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailProductActivity.this, "Thêm sản phẩm thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("ADDTOCART", response.message());
                }
            }

            @Override
            public void onFailure(Call<CartItem> call, Throwable t) {
                Toast.makeText(DetailProductActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ADDTOCART", "Lỗi: " + t.getMessage());
            }
        });
    }

    private void getCart() {
        Call<CartResponse> call = apiService.getCart("Bearer " + token);
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CartResponse cartResponse = response.body();
                    int totalQuantity = cartResponse.getTotalQuantity();
                    mBinding.tvTotalQuantity.setText(String.valueOf(totalQuantity));
                } else {
                    Toast.makeText(DetailProductActivity.this, "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("DetailProductActivity", "Lỗi khi lấy giỏ hàng: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(DetailProductActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }
}