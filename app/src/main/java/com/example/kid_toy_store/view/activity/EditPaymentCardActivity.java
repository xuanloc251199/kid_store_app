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
import com.example.kid_toy_store.databinding.ActivityEditPaymentCardBinding;
import com.example.kid_toy_store.model.Card;
import com.example.kid_toy_store.response.CardResponse;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPaymentCardActivity extends AppCompatActivity {

    ActivityEditPaymentCardBinding mBinding;
    View mView;

    private ApiService apiService;
    private String token;

    String cardNumber, expiryDate, cvv;
    int cardId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityEditPaymentCardBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();
        setContentView(mView);

        // Khởi tạo Retrofit
        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        // Lấy access_token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(KeyUtils.ACCESS_TOKEN, null);

        cardId = getIntent().getIntExtra(KeyUtils.CARD_PAYMENT_KEY, -1);

        getCardById();

        setOnClickListener();

    }

    private void setOnClickListener() {
        mBinding.btnSaveCard.setOnClickListener(v -> updateCard());
        mBinding.btnBack.setOnClickListener(v -> finish());
    }

    private void getCardById() {
        Call<Card> call = apiService.getCardById("Bearer " + token, cardId);
        call.enqueue(new Callback<Card>() {
            @Override
            public void onResponse(Call<Card> call, Response<Card> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Lấy dữ liệu thẻ từ API
                    Card card = response.body();
                    mBinding.edtCardNumber.setText(card.getCardNumber());
                    mBinding.edtExpiryDate.setText(card.getExpiryDate());
                    mBinding.edtCvv.setText(card.getCvv());
                } else {
                    // Xử lý khi cập nhật thẻ thất bại
                    Log.e("SHOW CARD", response.message() + response.code());
                    Log.d("SHOW CARD", response.message() + response.code());
                    Toast.makeText(getApplicationContext(), "Failed to update card", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Card> call, Throwable t) {
                // Xử lý lỗi mạng
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateCard() {

        cardNumber = mBinding.edtCardNumber.getText().toString().trim();
        expiryDate = mBinding.edtExpiryDate.getText().toString().trim();
        cvv = mBinding.edtCvv.getText().toString().trim();

        // Tạo đối tượng CardInfo để chứa thông tin thẻ cập nhật
        Card cardInfo = new Card(cardId, cardNumber, expiryDate, cvv);

        // Gọi API cập nhật thẻ
        Call<CardResponse> call = apiService.updateCard("Bearer " + token, cardId, cardInfo);
        call.enqueue(new Callback<CardResponse>() {
            @Override
            public void onResponse(Call<CardResponse> call, Response<CardResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Card updated successfully", Toast.LENGTH_SHORT).show();
                    customStartActivity(EditPaymentCardActivity.this, AllPaymentCardActivity.class);
                } else {
                    // Xử lý khi cập nhật thẻ thất bại
                    Log.e("EDIT PAY", response.message() + response.code());
                    Toast.makeText(getApplicationContext(), "Failed to update card", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CardResponse> call, Throwable t) {
                // Xử lý lỗi mạng
                t.printStackTrace();
                Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}