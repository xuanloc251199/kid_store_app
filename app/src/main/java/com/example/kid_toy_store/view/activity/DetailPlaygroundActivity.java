package com.example.kid_toy_store.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kid_toy_store.api.ApiService;
import com.example.kid_toy_store.api.RetrofitClient;
import com.example.kid_toy_store.databinding.ActivityDetailPlaygroundBinding;
import com.example.kid_toy_store.formatter.CurrencyFormatter;
import com.example.kid_toy_store.formatter.QuantityFormatter;
import com.example.kid_toy_store.model.CartItem;
import com.example.kid_toy_store.request.CartRequest;
import com.example.kid_toy_store.model.Tickets;
import com.example.kid_toy_store.response.CartResponse;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;
import com.squareup.picasso.Picasso;

import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailPlaygroundActivity extends AppCompatActivity {

    ActivityDetailPlaygroundBinding mBinding;
    View mView;
    AtomicInteger numberCounter = new AtomicInteger(1);

    private ApiService apiService;

    String token;
    double totalPrice;
    int ticketId, updateCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityDetailPlaygroundBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();

        // Khởi tạo Retrofit
        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        // Lấy access_token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(KeyUtils.ACCESS_TOKEN, null);

        updateCounter = 1;
        mBinding.tvQuantityTickets.setText(String.valueOf(updateCounter));
        getCart();

        ticketId = getIntent().getIntExtra(KeyUtils.TICKET_KEY, -1);

        getTicketById(ticketId);

        setOnClickListener();

        setContentView(mView);
    }

    private void setOnClickListener() {

        mBinding.btnAddToCart.setOnClickListener(v -> {
            addToCart();
            getCart();
        });

        mBinding.cvCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        mBinding.cvBackHome.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));

        mBinding.cvBack.setOnClickListener(v -> finish());

        mBinding.cvPlus.setOnClickListener(v -> {
            updateCounter = numberCounter.get() + 1;
            mBinding.tvQuantityTickets.setText(String.valueOf(updateCounter));

            double totalUpdate = totalPrice * updateCounter;

            mBinding.tvTotalPrice.setText(CurrencyFormatter.formatCurrency(totalUpdate));

            numberCounter.set(updateCounter);
        });

        mBinding.cvMinus.setOnClickListener(v -> {

            if (numberCounter.get() > 1) {
                updateCounter = numberCounter.get() - 1;
                mBinding.tvQuantityTickets.setText(String.valueOf(updateCounter));

                double totalUpdate = totalPrice * updateCounter;

                mBinding.tvTotalPrice.setText(CurrencyFormatter.formatCurrency(totalUpdate));

                numberCounter.set(updateCounter);
            }
        });
    }

    private void getTicketById(int ticketId) {

        Call<Tickets> call = apiService.getTicketById(ticketId);

        call.enqueue(new Callback<Tickets>() {
            @Override
            public void onResponse(Call<Tickets> call, Response<Tickets> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Tickets tickets = response.body();

                    mBinding.tvNameEvent.setText(tickets.getName());
                    mBinding.tvFinalPrice.setText(CurrencyFormatter.formatCurrency(tickets.getFinalPrice()));
                    mBinding.tvPrice.setText(CurrencyFormatter.formatCurrency(tickets.getPrice()));
                    mBinding.tvDescription.setText(tickets.getDescription());
                    mBinding.tvSoldPG.setText(QuantityFormatter.formatQuantity(tickets.getSold()));
                    mBinding.tvTotalPrice.setText(CurrencyFormatter.formatCurrency(tickets.getFinalPrice()));

                    totalPrice = tickets.getFinalPrice();

                    Picasso.get().load(ApiUtils.IP_DEFAULT + tickets.getThumbnail()).into(mBinding.imgTicket);

                } else {
                    Toast.makeText(DetailPlaygroundActivity.this, "Không thể tải sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tickets> call, Throwable t) {
                Toast.makeText(DetailPlaygroundActivity.this, "Lỗi khi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addToCart() {
        int quantity = Integer.parseInt(mBinding.tvQuantityTickets.getText().toString().trim());
        // Tạo request body cho việc thêm vé vào giỏ hàng
        CartRequest cartRequest = new CartRequest(ticketId, "ticket", quantity);

        Call<CartItem> call = apiService.addToCart("Bearer " + token, cartRequest);

        call.enqueue(new Callback<CartItem>() {
            @Override
            public void onResponse(Call<CartItem> call, Response<CartItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    getCart();
                    Toast.makeText(DetailPlaygroundActivity.this, "Vé đã được thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DetailPlaygroundActivity.this, "Không thể thêm vé vào giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartItem> call, Throwable t) {
                Toast.makeText(DetailPlaygroundActivity.this, "Lỗi khi thêm vé vào giỏ hàng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DetailPlaygroundActivity.this, "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("DetailProductActivity", "Lỗi khi lấy giỏ hàng: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(DetailPlaygroundActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

}