package com.example.kid_toy_store.view.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.adapter.CartProductAdapter;
import com.example.kid_toy_store.adapter.OrderAdapter;
import com.example.kid_toy_store.adapter.ProductAdapter;
import com.example.kid_toy_store.adapter.TicketAdapter;
import com.example.kid_toy_store.api.ApiService;
import com.example.kid_toy_store.api.RetrofitClient;
import com.example.kid_toy_store.databinding.ActivityCartBinding;
import com.example.kid_toy_store.databinding.ActivityPaidOrdersBinding;
import com.example.kid_toy_store.model.Order;
import com.example.kid_toy_store.model.Products;
import com.example.kid_toy_store.model.Tickets;
import com.example.kid_toy_store.response.OrderResponse;
import com.example.kid_toy_store.response.PurchaseResponse;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaidOrdersActivity extends AppCompatActivity {
    ActivityPaidOrdersBinding mBinding;
    View mView;

    RecyclerView rvCartProducts, rvCartTickets;
    private OrderAdapter orderAdapter;
    private List<Order> orders = new ArrayList<>();
    ProductAdapter productAdapter;
    TicketAdapter ticketAdapter;

    private ApiService apiService;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityPaidOrdersBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();
        setContentView(mView);

        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        // Lấy access_token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(KeyUtils.ACCESS_TOKEN, null);

        fetchPurchases();
    }

    private void fetchPurchases() {
        Call<PurchaseResponse> call = apiService.getPurchasedItems("Bearer " + token);
        call.enqueue(new Callback<PurchaseResponse>() {
            @Override
            public void onResponse(Call<PurchaseResponse> call, Response<PurchaseResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Products> productItems = new ArrayList<>();
                    List<Tickets> ticketItems = new ArrayList<>();

                    // Duyệt qua tất cả các đơn hàng và phân loại sản phẩm và vé
                    for (PurchaseResponse.Purchase purchase : response.body().getPurchases()) {
                        for (PurchaseResponse.Purchase.Item item : purchase.getItems()) {
                            if (item.getProductName() != null) {
                                productItems.add(new Products(
                                        item.getProductName(),
                                        item.getProductThumbnail(),
                                        Double.parseDouble(item.getPrice()),
                                        item.getQuantity()
                                ));
                            } else if (item.getTicketName() != null) {
                                ticketItems.add(new Tickets(
                                        item.getTicketName(),
                                        item.getTicketThumbnail(),
                                        item.getTicketPlace(),
                                        item.getTicketDate(),
                                        Double.parseDouble(item.getPrice()),
                                        item.getQuantity()
                                ));
                            }
                        }
                    }

                    rvCartProducts = mBinding.rvCartProducts;
                    rvCartTickets = mBinding.rvCartTickets;

                    // Thiết lập RecyclerView cho sản phẩm
                    rvCartProducts.setLayoutManager(new GridLayoutManager(PaidOrdersActivity.this, 2, GridLayoutManager.VERTICAL, false));
                    productAdapter = new ProductAdapter(productItems, PaidOrdersActivity.this);
                    rvCartProducts.setAdapter(productAdapter);

                    // Thiết lập RecyclerView cho vé
                    rvCartTickets.setLayoutManager(new LinearLayoutManager(PaidOrdersActivity.this, LinearLayoutManager.VERTICAL, false));
                    ticketAdapter = new TicketAdapter(ticketItems, PaidOrdersActivity.this);
                    rvCartTickets.setAdapter(ticketAdapter);

                } else {
                    Toast.makeText(PaidOrdersActivity.this, "No purchases found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PurchaseResponse> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(PaidOrdersActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}