package com.example.kid_toy_store.view.fragment;

import static android.app.ProgressDialog.show;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kid_toy_store.view.activity.AllPlaygroundActivity;
import com.example.kid_toy_store.view.activity.AllProductActivity;
import com.example.kid_toy_store.view.activity.CartActivity;
import com.example.kid_toy_store.adapter.CategoryAdapter;
import com.example.kid_toy_store.adapter.TicketAdapter;
import com.example.kid_toy_store.adapter.ProductAdapter;
import com.example.kid_toy_store.api.ApiService;
import com.example.kid_toy_store.api.RetrofitClient;
import com.example.kid_toy_store.databinding.FragmentHomeBinding;
import com.example.kid_toy_store.model.Categories;
import com.example.kid_toy_store.model.Products;
import com.example.kid_toy_store.model.Tickets;
import com.example.kid_toy_store.response.CartResponse;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    View mView;
    FragmentHomeBinding mBinding;

    private CategoryAdapter categoryAdapter;
    private ProductAdapter productAdapter;
    private TicketAdapter ticketAdapter;
    private ApiService apiService;
    String token;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentHomeBinding.inflate(inflater, container, false);

        // Khởi tạo Retrofit
        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        // Lấy access_token từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(KeyUtils.ACCESS_TOKEN, null);

        getCart();

        setupRv();

        setOnClickListener(mBinding);

        return mBinding.getRoot();
    }

    private void setOnClickListener(FragmentHomeBinding binding) {

        binding.tvViewMoreProduct.setOnClickListener(v -> startActivity(new Intent(getContext(), AllProductActivity.class)));
        binding.tvViewMorePlayGround.setOnClickListener(v -> startActivity(new Intent(getContext(), AllPlaygroundActivity.class)));
        binding.btnCart.setOnClickListener(v -> startActivity(new Intent(getContext(), CartActivity.class)));

    }

    private void setupRv() {

        RecyclerView rvProduct, rvCategory, rvPlayground;

        rvProduct = mBinding.rvProduct;
        rvProduct.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        getProducts();


        rvCategory = mBinding.rvCategory;
        rvCategory.setLayoutManager(new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false));
        // Gọi API để lấy danh sách Category
        getCategories();


        rvPlayground = mBinding.rvTicketPlayGround;
        rvPlayground.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        getTickets();

    }

    private void getCategories() {
        Call<List<Categories>> call = apiService.getCategories();

        call.enqueue(new Callback<List<Categories>>() {
            @Override
            public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Categories> categories = response.body();

                    // Gắn danh sách Category vào Adapter và hiển thị trong RecyclerView
                    categoryAdapter = new CategoryAdapter(categories, getContext());
                    mBinding.rvCategory.setAdapter(categoryAdapter);
                } else {
                    Toast.makeText(getActivity(), "Failed to fetch categories", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Categories>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error fetching categories", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getProducts() {
        Call<List<Products>> call = apiService.getProducts();

        call.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Products> products = response.body();

                    // Gắn danh sách sản phẩm vào Adapter và hiển thị trong RecyclerView
                    productAdapter = new ProductAdapter(products, getContext());
                    mBinding.rvProduct.setAdapter(productAdapter);
                } else {
                    Toast.makeText(getActivity(), "Không thể tải danh sách sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi khi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getTickets() {
        Call<List<Tickets>> call = apiService.getEvents();
        call.enqueue(new Callback<List<Tickets>>() {
            @Override
            public void onResponse(Call<List<Tickets>> call, Response<List<Tickets>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Tickets> tickets = response.body();

                    // Gắn dữ liệu vào Adapter và hiển thị trong RecyclerView
                    ticketAdapter = new TicketAdapter(tickets, getContext());
                    mBinding.rvTicketPlayGround.setAdapter(ticketAdapter);
                } else {
                    Toast.makeText(getActivity(), "Không thể tải sự kiện", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Tickets>> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi khi tải sự kiện", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(), "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e("DetailProductActivity", "Lỗi khi lấy giỏ hàng: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }
}