package com.example.kid_toy_store.view.activity;

import static com.example.kid_toy_store.helper.CustomStartActivity.customStartActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.api.ApiService;
import com.example.kid_toy_store.api.RetrofitClient;
import com.example.kid_toy_store.databinding.ActivityDirectPaymentBinding;
import com.example.kid_toy_store.formatter.CurrencyFormatter;
import com.example.kid_toy_store.request.DirectPaymentRequest;
import com.example.kid_toy_store.response.CartResponse;
import com.example.kid_toy_store.response.DirectPaymentResponse;
import com.example.kid_toy_store.response.UserResponse;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectPaymentActivity extends AppCompatActivity {

    private ActivityDirectPaymentBinding mBinding;
    private ApiService apiService;
    private String token;
    private double totalPrice;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityDirectPaymentBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        // Lấy token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(KeyUtils.ACCESS_TOKEN, null);

        // Nhận dữ liệu từ Intent
        getOrderAmount();

        getUserProfile();

        setOnClickListener();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setOnClickListener() {
        mBinding.cvShippingAddress.setOnClickListener(v -> customStartActivity(this, EditShippingAddressActivity.class));
        mBinding.btnPlaceOrder.setOnClickListener(v -> processDirectPayment());
        mBinding.btnBack.setOnClickListener(v -> finish());

    }

    private void getUserProfile() {
        Call<UserResponse> call = apiService.getUserProfile("Bearer " + token);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse.getUser().getAddress() != null) {
                        mBinding.tvShippingAddress.setText(userResponse.getUser().getAddress());
                        mBinding.btnPlaceOrder.setEnabled(true);
                    } else {
                        mBinding.tvShippingAddress.setVisibility(View.GONE);
                        mBinding.btnPlaceOrder.setBackground(getDrawable(R.drawable.button_background_not_enabled)); // Đổi màu nền nút thành màu xám
                        mBinding.btnPlaceOrder.setEnabled(false);
                    }
                } else {
                    Log.e("API Error", "Failed to load user profile: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("API Error", "Error loading user profile: " + t.getMessage());
            }
        });
    }

    private void getOrderAmount() {
        Call<CartResponse> call = apiService.getCart("Bearer " + token);
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CartResponse cartResponse = response.body();
                    totalPrice = cartResponse.getTotalPrice();
                    mBinding.tvTotalPrice.setText(CurrencyFormatter.formatCurrency(totalPrice));

                    if (cartResponse.getCartItems().isEmpty()) {
                        Toast.makeText(DirectPaymentActivity.this, "Giỏ hàng trống. Không thể thanh toán.", Toast.LENGTH_SHORT).show();
                        mBinding.btnPlaceOrder.setEnabled(false);
                    }
                } else {
                    Log.e("API Error", "Failed to load cart: " + response.message());
                    Toast.makeText(DirectPaymentActivity.this, "Không thể tải giỏ hàng", Toast.LENGTH_SHORT).show();
                    mBinding.btnPlaceOrder.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Log.e("API Error", "Error loading cart: " + t.getMessage());
                Toast.makeText(DirectPaymentActivity.this, "Lỗi khi tải giỏ hàng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                mBinding.btnPlaceOrder.setEnabled(false);
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void processDirectPayment() {
        String address = mBinding.tvShippingAddress.getText().toString();
        DirectPaymentRequest request = new DirectPaymentRequest(totalPrice, address);
        Call<DirectPaymentResponse> call = apiService.directPayment("Bearer " + token, request);
        call.enqueue(new Callback<DirectPaymentResponse>() {
            @Override
            public void onResponse(Call<DirectPaymentResponse> call, Response<DirectPaymentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DirectPaymentResponse paymentResponse = response.body();
                    Toast.makeText(DirectPaymentActivity.this, paymentResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    customStartActivity(DirectPaymentActivity.this, OrderSuccessfullyActivity.class);
                } else {
                    Toast.makeText(DirectPaymentActivity.this, "Thanh toán thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DirectPaymentResponse> call, Throwable t) {
                Toast.makeText(DirectPaymentActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("CASH", t.getMessage());
            }
        });
    }
}