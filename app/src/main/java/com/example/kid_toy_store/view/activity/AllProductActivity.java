package com.example.kid_toy_store.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kid_toy_store.adapter.ProductAdapter;
import com.example.kid_toy_store.api.ApiService;
import com.example.kid_toy_store.api.RetrofitClient;
import com.example.kid_toy_store.databinding.ActivityAllProductBinding;
import com.example.kid_toy_store.model.Products;
import com.example.kid_toy_store.utils.ApiUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllProductActivity extends AppCompatActivity {

    ActivityAllProductBinding mBinding;
    View mView;

    private ProductAdapter productAdapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityAllProductBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();
        setContentView(mView);

        // Khởi tạo Retrofit
        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        mBinding.btnBack.setOnClickListener(v -> finish());

        setupProductRv();

    }

    private void setupProductRv() {

        RecyclerView recyclerView = mBinding.rvProduct;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        getProducts();

    }

    private void getProducts() {
        Call<List<Products>> call = apiService.getProducts();

        call.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Products> products = response.body();
                    // Gắn danh sách sản phẩm vào Adapter và hiển thị trong RecyclerView
                    productAdapter = new ProductAdapter(products, AllProductActivity.this);
                    mBinding.rvProduct.setAdapter(productAdapter);
                } else {
                    Toast.makeText(AllProductActivity.this, "Không thể tải danh sách sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                Toast.makeText(AllProductActivity.this, "Lỗi khi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }
}