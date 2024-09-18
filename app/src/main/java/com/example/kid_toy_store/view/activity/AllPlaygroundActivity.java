package com.example.kid_toy_store.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kid_toy_store.adapter.TicketAdapter;
import com.example.kid_toy_store.api.ApiService;
import com.example.kid_toy_store.api.RetrofitClient;
import com.example.kid_toy_store.databinding.ActivityAllPlaygroundBinding;
import com.example.kid_toy_store.model.Tickets;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPlaygroundActivity extends AppCompatActivity {

    ActivityAllPlaygroundBinding mBinding;
    View mView;

    private TicketAdapter ticketAdapter;

    private ApiService apiService;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityAllPlaygroundBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();

        // Khởi tạo Retrofit
        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        // Lấy access_token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(KeyUtils.ACCESS_TOKEN, null);

        setupRv();

        mBinding.btnBack.setOnClickListener(v -> finish());

        setContentView(mView);
    }

    private void setupRv() {

        RecyclerView rvPlayground;

        rvPlayground = mBinding.rvTicket;
        rvPlayground.setLayoutManager(new LinearLayoutManager(AllPlaygroundActivity.this, LinearLayoutManager.VERTICAL, false));
        getTickets();

    }

    private void getTickets() {
        Call<List<Tickets>> call = apiService.getEvents();
        call.enqueue(new Callback<List<Tickets>>() {
            @Override
            public void onResponse(Call<List<Tickets>> call, Response<List<Tickets>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Tickets> tickets = response.body();

                    // Gắn dữ liệu vào Adapter và hiển thị trong RecyclerView
                    ticketAdapter = new TicketAdapter(tickets, AllPlaygroundActivity.this);
                    mBinding.rvTicket.setAdapter(ticketAdapter);
                } else {
                    Toast.makeText(AllPlaygroundActivity.this, "Không thể tải sự kiện", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tickets>> call, Throwable t) {
                Toast.makeText(AllPlaygroundActivity.this, "Lỗi khi tải sự kiện", Toast.LENGTH_SHORT).show();
            }
        });
    }
}