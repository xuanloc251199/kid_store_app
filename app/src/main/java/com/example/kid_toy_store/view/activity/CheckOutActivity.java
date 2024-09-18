package com.example.kid_toy_store.view.activity;

import static com.example.kid_toy_store.helper.CustomStartActivity.customStartActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.adapter.CardAdapter;
import com.example.kid_toy_store.api.ApiService;
import com.example.kid_toy_store.api.RetrofitClient;
import com.example.kid_toy_store.databinding.ActivityCheckOutBinding;
import com.example.kid_toy_store.formatter.CurrencyFormatter;
import com.example.kid_toy_store.model.Card;
import com.example.kid_toy_store.request.PaymentRequest;
import com.example.kid_toy_store.response.CardListResponse;
import com.example.kid_toy_store.response.CartResponse;
import com.example.kid_toy_store.response.PaymentResponse;
import com.example.kid_toy_store.response.UserResponse;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckOutActivity extends AppCompatActivity {

    private ActivityCheckOutBinding mBinding;
    private ApiService apiService;
    private String token;
    private String cardNumber, cardExpiryDate, cardCvv;
    private double totalPrice;
    private int cardId;
    private int cardIdDefault = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityCheckOutBinding.inflate(getLayoutInflater());
        View mView = mBinding.getRoot();
        setContentView(mView);

        // Khởi tạo API service
        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        // Lấy token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(KeyUtils.ACCESS_TOKEN, null);

        // Lấy ID thẻ từ Intent
        cardId = getIntent().getIntExtra(KeyUtils.CARD_PAYMENT_KEY, cardIdDefault);
        Log.d("CARDID", String.valueOf(cardId));

        // Lấy thông tin người dùng và thẻ
        getUserProfile();
        getOrderAmount(); // Kiểm tra số tiền của giỏ hàng
        getCardById(cardId);

        setOnClickListener();

        // Xử lý sự kiện quay lại
        mBinding.btnBack.setOnClickListener(v -> finish());
    }

    private void setOnClickListener() {
        // Xử lý sự kiện nút thanh toán
//        mBinding.btnPlaceOrder.setOnClickListener(v -> {
////            if (totalPrice > 0 && cardNumber != null && cardExpiryDate != null && cardCvv != null) {
//                makePayment(cardNumber, cardExpiryDate, cardCvv, totalPrice);
////            } else {
////                Toast.makeText(CheckOutActivity.this, "Không thể thanh toán, vui lòng kiểm tra thông tin!", Toast.LENGTH_SHORT).show();
////            }
//        });

        mBinding.cvPaymentMethod.setOnClickListener(v -> customStartActivity(this, AllPaymentCardActivity.class));
        mBinding.cvShippingAddress.setOnClickListener(v -> customStartActivity(this, EditShippingAddressActivity.class));

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
                    } else {
                        mBinding.tvShippingAddress.setVisibility(View.GONE);
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

    private void getCardById(int cardId) {
        Call<Card> call;

        if (cardId == cardIdDefault) {
            call = apiService.getFirstCard("Bearer " + token);
        } else {
            call = apiService.getCardById("Bearer " + token, cardId);
        }

        call.enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Call<Card> call, Response<Card> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Card card = response.body();
                    cardNumber = card.getCardNumber(); // Số thẻ đã giải mã
                    cardExpiryDate = card.getExpiryDate();
                    cardCvv = card.getCvv();

                    Log.d("CARD_RESPONSE", "Decrypted Card Number: " + cardNumber);

                    // Hiển thị số thẻ (4 số cuối) cho người dùng
                    String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);
                    mBinding.tvCardNumber.setText("**** **** **** " + lastFourDigits);

                    // Kết nối sự kiện thanh toán
                    mBinding.btnPlaceOrder.setEnabled(true);
                    mBinding.btnPlaceOrder.setOnClickListener(v -> makePayment(cardNumber, cardExpiryDate, cardCvv, totalPrice));
                } else {
                    Log.e("API Error", "Failed to load card details: " + response.message());
                    mBinding.btnPlaceOrder.setEnabled(false);
                }
            }

            @Override
            public void onFailure(Call<Card> call, Throwable t) {
                Log.e("API Error", "Network error: " + t.getMessage());
                Toast.makeText(CheckOutActivity.this, "Lỗi kết nối khi tải thẻ: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                mBinding.btnPlaceOrder.setEnabled(false);
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void getOrderAmount() {
        if (mBinding.tvShippingAddress.getText() == null) {
            Toast.makeText(CheckOutActivity.this, "Vui lòng thêm địa chỉ", Toast.LENGTH_SHORT).show();
            mBinding.btnPlaceOrder.setBackgroundColor(R.color.lightGray);
            mBinding.btnPlaceOrder.setEnabled(false);
        } else {
            Call<CartResponse> call = apiService.getCart("Bearer " + token);
            call.enqueue(new Callback<CartResponse>() {
                @Override
                public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        CartResponse cartResponse = response.body();
                        totalPrice = cartResponse.getTotalPrice();
                        mBinding.tvTotalPrice.setText(CurrencyFormatter.formatCurrency(totalPrice));

                        if (cartResponse.getCartItems().isEmpty()) {
                            Toast.makeText(CheckOutActivity.this, "Giỏ hàng trống. Không thể thanh toán.", Toast.LENGTH_SHORT).show();
                            mBinding.btnPlaceOrder.setEnabled(false);
                        }
                    } else {
                        Log.e("API Error", "Failed to load cart: " + response.message());
                        Toast.makeText(CheckOutActivity.this, "Không thể tải giỏ hàng", Toast.LENGTH_SHORT).show();
                        mBinding.btnPlaceOrder.setBackgroundColor(R.color.lightGray);
                        mBinding.btnPlaceOrder.setEnabled(false);
                    }
                }

                @Override
                public void onFailure(Call<CartResponse> call, Throwable t) {
                    Log.e("API Error", "Error loading cart: " + t.getMessage());
                    Toast.makeText(CheckOutActivity.this, "Lỗi khi tải giỏ hàng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    mBinding.btnPlaceOrder.setBackgroundColor(R.color.lightGray);
                    mBinding.btnPlaceOrder.setEnabled(false);
                }
            });
        }
    }

    private boolean validateCard(String cardNumber, String expiryDate, String cvv) {
        if (cardNumber == null || cardNumber.length() != 16) {
            Log.e("Payment", "Invalid card number");
            return false;
        }
        if (expiryDate == null || expiryDate.length() != 5) { // Format MM/YY
            Log.e("Payment", "Invalid expiry date");
            return false;
        }
        if (cvv == null || cvv.length() != 3) {
            Log.e("Payment", "Invalid CVV");
            return false;
        }
        return true;
    }

    public void makePayment(String cardNumber, String cardExpiryDate, String cardCvv, double amount) {
        PaymentRequest paymentRequest = new PaymentRequest(cardNumber, cardExpiryDate, cardCvv, amount);

        Call<PaymentResponse> call = apiService.processPayment("Bearer " + token, paymentRequest);
        call.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call, Response<PaymentResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(CheckOutActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình thông báo thành công
                    Intent intent = new Intent(CheckOutActivity.this, OrderSuccessfullyActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.e("PAYMENT_ERROR", "Thanh toán thất bại: " + response.code() + " - " + response.message());
                    Toast.makeText(CheckOutActivity.this, "Thanh toán thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {
                Log.e("PAYMENT_ERROR", "Lỗi kết nối: " + t.getMessage());
                Toast.makeText(CheckOutActivity.this, "Lỗi kết nối khi thanh toán: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
