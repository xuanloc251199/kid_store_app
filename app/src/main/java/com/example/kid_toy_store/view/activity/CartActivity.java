package com.example.kid_toy_store.view.activity;

import static com.example.kid_toy_store.helper.CustomStartActivity.customStartActivity;

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
import com.example.kid_toy_store.request.CheckoutRequest;
import com.example.kid_toy_store.response.CartResponse;
import com.example.kid_toy_store.response.CheckoutResponse;
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

    RecyclerView rvCartProducts, rvCartTickets;

    CartProductAdapter productAdapter, ticketAdapter;
    private List<CartItem> cartProductItems = new ArrayList<>();
    private List<CartItem> cartTicketItems = new ArrayList<>();

    ApiService apiService;
    String token;
    double totalPrice;

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

        setupCartProductRv();
        loadCartItems(); // Gọi API để tải các mục trong giỏ hàng

        setOnClickListener(mBinding);
    }

    private void setOnClickListener(ActivityCartBinding binding) {
        binding.btnBack.setOnClickListener(v -> onBackPressed());

        binding.tvRemoveAllProducts.setOnClickListener(v -> {
            productAdapter.clearAllItems();
        });

        binding.tvRemoveAllTicket.setOnClickListener(v -> {
            ticketAdapter.clearAllItems();
        });
    }

    private void setupCartProductRv() {
        rvCartProducts = mBinding.rvCartProducts;
        rvCartTickets = mBinding.rvCartTickets;

        // Thiết lập RecyclerView cho sản phẩm
        rvCartProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        productAdapter = new CartProductAdapter(cartProductItems, this);
        rvCartProducts.setAdapter(productAdapter);

        // Thiết lập RecyclerView cho vé
        rvCartTickets.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        ticketAdapter = new CartProductAdapter(cartTicketItems, this);
        rvCartTickets.setAdapter(ticketAdapter);
    }

    private void loadCartItems() {
        Call<CartResponse> call = apiService.getCart("Bearer " + token);
        call.enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CartResponse cartResponse = response.body();
                    List<CartItem> allCartItems = cartResponse.getCartItems();

                    // Phân loại giữa sản phẩm và vé
                    for (CartItem item : allCartItems) {
                        if (item.getType().equals("product")) {
                            cartProductItems.add(item);
                        } else if (item.getType().equals("ticket")) {
                            cartTicketItems.add(item);
                        }
                    }

                    // Cập nhật lại giao diện
                    productAdapter.notifyDataSetChanged();
                    ticketAdapter.notifyDataSetChanged();

                    // Cập nhật giá tổng
                    updateTotalPrice(cartResponse.getTotalPrice());

                    totalPrice = cartResponse.getTotalPrice();
                    Log.d("PRICE", CurrencyFormatter.formatCurrency(totalPrice));

                    // Khi nhấn vào nút "Checkout", truyền totalPrice qua Intent
                    mBinding.btnCheckout.setOnClickListener(v -> {
                        Intent intent = new Intent(CartActivity.this, CheckOutActivity.class);
                        intent.putExtra(KeyUtils.ORDER_KEY, totalPrice); // Truyền totalPrice qua Intent
                        startActivity(intent);
                    });

                } else {
                    Toast.makeText(CartActivity.this, "Không thể tải giỏ hàng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(CartActivity.this, "Lỗi khi tải giỏ hàng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateTotalPrice(double totalPrice) {
        mBinding.tvTotalPrice.setText(CurrencyFormatter.formatCurrency(totalPrice));
    }

    private void checkoutCart() {
        if ((productAdapter == null || productAdapter.getItemCount() == 0) &&
                (ticketAdapter == null || ticketAdapter.getItemCount() == 0)) {
            Toast.makeText(this, "Giỏ hàng trống!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy danh sách các sản phẩm và vé trong giỏ hàng
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.addAll(productAdapter.getCartItems());
        cartItems.addAll(ticketAdapter.getCartItems());

        // Tạo yêu cầu thanh toán
        CheckoutRequest checkoutRequest = new CheckoutRequest(cartItems);

        // Gọi API checkout
        Call<CheckoutResponse> call = apiService.checkout("Bearer " + token, checkoutRequest);
        call.enqueue(new Callback<CheckoutResponse>() {
            @Override
            public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {
                if (response.isSuccessful()) {
                    CheckoutResponse checkoutResponse = response.body();
                    if (checkoutResponse != null) {
                        // Hiển thị thông báo thanh toán thành công
                        Toast.makeText(CartActivity.this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();

                        // Xóa giỏ hàng sau khi thanh toán thành công
                        productAdapter.clearAllItems();
                        ticketAdapter.clearAllItems();

                        // Chuyển sang trang OrderSuccessActivity hoặc thực hiện hành động khác
                        Intent intent = new Intent(CartActivity.this, OrderSuccessfullyActivity.class);
                        intent.putExtra("order_id", checkoutResponse.getOrder().getId());
                        startActivity(intent);
                    }
                } else {
                    // Lỗi từ phía server
                    Toast.makeText(CartActivity.this, "Thanh toán thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckoutResponse> call, Throwable t) {
                // Lỗi kết nối
                Toast.makeText(CartActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
