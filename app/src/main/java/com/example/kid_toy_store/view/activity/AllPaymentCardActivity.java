package com.example.kid_toy_store.view.activity;

import static com.example.kid_toy_store.helper.CustomStartActivity.customStartActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kid_toy_store.adapter.CardAdapter;
import com.example.kid_toy_store.api.ApiService;
import com.example.kid_toy_store.api.RetrofitClient;
import com.example.kid_toy_store.databinding.ActivityAllPaymentCardBinding;
import com.example.kid_toy_store.model.Card;
import com.example.kid_toy_store.model.CardInfo;
import com.example.kid_toy_store.response.CardListResponse;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPaymentCardActivity extends AppCompatActivity {

    ActivityAllPaymentCardBinding mBinding;
    View mView;

    private ApiService apiService;
    String token;

    CardAdapter cardAdapter;
    RecyclerView rvCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityAllPaymentCardBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();
        setContentView(mView);

        setOnclickListen();

        // Khởi tạo Retrofit
        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        // Lấy access_token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(KeyUtils.ACCESS_TOKEN, null);

        setupRv();
    }

    private void setOnclickListen() {

        mBinding.btnAddCard.setOnClickListener(v -> customStartActivity(AllPaymentCardActivity.this, AddCardPaymentActivity.class));
        mBinding.btnBack.setOnClickListener(v -> finish());

    }

    private void setupRv(){
        rvCard = mBinding.rvCardPayment;
        rvCard.setLayoutManager(new LinearLayoutManager(AllPaymentCardActivity.this, LinearLayoutManager.VERTICAL, false));
        fetchCardsFromApi();
    }

    private void fetchCardsFromApi() {

        Call<CardListResponse> call = apiService.showCards("Bearer " + token);
        call.enqueue(new Callback<CardListResponse>() {
            @Override
            public void onResponse(Call<CardListResponse> call, Response<CardListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Card> cardList = response.body().cards;
                    cardAdapter = new CardAdapter(cardList, AllPaymentCardActivity.this);
                    rvCard.setAdapter(cardAdapter);  // Gán adapter vào RecyclerView
                } else {
                    // Xử lý lỗi
                }
            }

            @Override
            public void onFailure(Call<CardListResponse> call, Throwable t) {
                // Xử lý lỗi kết nối hoặc gọi API thất bại
            }
        });
    }
}