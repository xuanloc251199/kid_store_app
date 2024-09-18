package com.example.kid_toy_store.view.activity;

import static com.example.kid_toy_store.helper.CustomStartActivity.customStartActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.kid_toy_store.api.ApiService;
import com.example.kid_toy_store.api.RetrofitClient;
import com.example.kid_toy_store.databinding.ActivityAddCardPaymentBinding;
import com.example.kid_toy_store.model.Card;
import com.example.kid_toy_store.response.CardResponse;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;
import com.example.kid_toy_store.utils.Validation;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCardPaymentActivity extends AppCompatActivity {

    ActivityAddCardPaymentBinding mBinding;
    View mView;

    private ApiService apiService;
    private String token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityAddCardPaymentBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();
        setContentView(mView);

        // Khởi tạo Retrofit
        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        // Lấy access_token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(KeyUtils.ACCESS_TOKEN, null);

        setOnClickListener();

    }

    private void setOnClickListener() {
        mBinding.btnSaveCard.setOnClickListener(v -> storeCard());
        mBinding.btnBack.setOnClickListener(v -> finish());
    }

    private void storeCard() {
        String cardNumber = mBinding.edtCardNumber.getText().toString().trim();
        String expiryDate = mBinding.edtExpiryDate.getText().toString().trim();
        String cvv = mBinding.edtCvv.getText().toString().trim();

        if (!Validation.isValidCardNumber(cardNumber)) {
            mBinding.tvCardNumberError.setText("Card number must be 16 digits.");
            mBinding.tvCardNumberError.setVisibility(View.VISIBLE);
            return;
        }

        if (!Validation.isValidExpiryDate(expiryDate)) {
            mBinding.tvExpError.setText("Expiry date must be in MM/YY format.");
            mBinding.tvExpError.setVisibility(View.VISIBLE);
            return;
        }

        if (!Validation.isValidCvv(cvv)) {
            mBinding.tvCvvError.setText("CVV must be 3 digits.");
            mBinding.tvCvvError.setVisibility(View.VISIBLE);
            return;
        }

        // Thông tin thẻ
        Card card = new Card(cardNumber, expiryDate, cvv);


        Call<CardResponse> call = apiService.storeCard("Bearer " + token, card);
        call.enqueue(new Callback<CardResponse>() {
            @Override
            public void onResponse(Call<CardResponse> call, Response<CardResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Card save successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Xử lý khi cập nhật thẻ thất bại
                    Log.e("ADD PAY", response.message() + response.code());
                    Toast.makeText(getApplicationContext(), "Failed to update card", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CardResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(AddCardPaymentActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }


}