package com.example.kid_toy_store.activity;

import static com.example.kid_toy_store.helper.GetFakeData.getFakeData;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kid_toy_store.adapter.CartProductAdapter;
import com.example.kid_toy_store.api.ApiService;
import com.example.kid_toy_store.api.RetrofitClient;
import com.example.kid_toy_store.databinding.ActivityCartBinding;
import com.example.kid_toy_store.formatter.CurrencyFormatter;
import com.example.kid_toy_store.model.CartItem;
import com.example.kid_toy_store.model.Products;
import com.example.kid_toy_store.response.CartResponse;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {

    ActivityCartBinding mBinding;
    View mView;

    RecyclerView recyclerView;

    CartProductAdapter cartProductAdapter;
    ArrayList<Products> productsList;

    ApiService apiService;

    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityCartBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();
        setContentView(mView);

        // Khởi tạo Retrofit
        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        // Lấy access_token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(KeyUtils.ACCESS_TOKEN, null);

        Log.d("CART", token);

        setOnClickListener(mBinding);

        setupCartProductRv();
    }

    private void setOnClickListener(ActivityCartBinding binding) {
        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.tvRemoveAll.setOnClickListener(v -> {
            cartProductAdapter.clearAllItems();
        });
        binding.btnCheckout.setOnClickListener(v -> startActivity(new Intent(this, CheckOutActivity.class)));
    }


    private void setupCartProductRv() {
        RecyclerView recyclerView = mBinding.rvCartProduct;
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        loadCartItems();
    }

    private void loadCartItems() {
        Call<CartResponse> call = apiService.getCart("Bearer " + token);

        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful()) {
                    CartResponse cartResponse = response.body();
                    if (cartResponse != null) {
                        List<CartItem> cartItems = cartResponse.getCartItems();
                        cartProductAdapter = new CartProductAdapter(cartItems, CartActivity.this);
                        recyclerView = mBinding.rvCartProduct;
                        recyclerView.setAdapter(cartProductAdapter);

                        mBinding.tvTotalPrice.setText(CurrencyFormatter.formatCurrency(cartResponse.getTotalPrice()));
                    }
                } else {
                    Toast.makeText(CartActivity.this, "Failed to load cart", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}