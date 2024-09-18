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
import com.example.kid_toy_store.databinding.ActivityDetailCategoryBinding;
import com.example.kid_toy_store.model.Products;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailCategoryActivity extends AppCompatActivity {

    ActivityDetailCategoryBinding mBinding;
    View mView;

    int categoryId;

    private ProductAdapter productAdapter;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityDetailCategoryBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();
        setContentView(mView);

        // Khởi tạo Retrofit
        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        mBinding.btnBack.setOnClickListener(v -> finish());

        categoryId = getIntent().getIntExtra(KeyUtils.CATEGORY_KEY, -1);

        setupProductRv();

    }

    private void setupProductRv() {

        RecyclerView recyclerView = mBinding.rvProduct;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        getProducts();

    }

    private void getProducts() {
        Call<List<Products>> call = apiService.getProductsByCategory(categoryId);

        call.enqueue(new Callback<List<Products>>() {
            @Override
            public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Gắn danh sách sản phẩm vào Adapter và hiển thị trong RecyclerView
                    List<Products> products = response.body();
                    productAdapter = new ProductAdapter(products, DetailCategoryActivity.this);
                    mBinding.rvProduct.setAdapter(productAdapter);
                    mBinding.rvProduct.setVisibility(View.VISIBLE);
                    mBinding.tvEmptyMessage.setVisibility(View.GONE); // Ẩn thông báo nếu có sản phẩm
                } else {
                    // Nếu không có sản phẩm, hiển thị thông báo
                    String emptyMessage = "Không có sản phẩm nào thuộc danh mục này.";
                    mBinding.rvProduct.setVisibility(View.GONE);
                    mBinding.tvEmptyMessage.setText(emptyMessage); // Hiển thị thông báo từ API
                    mBinding.tvEmptyMessage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<List<Products>> call, Throwable t) {
                Toast.makeText(DetailCategoryActivity.this, "Lỗi khi tải sản phẩm", Toast.LENGTH_SHORT).show();
            }
        });
    }
}