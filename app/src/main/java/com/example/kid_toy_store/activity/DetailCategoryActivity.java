package com.example.kid_toy_store.activity;

import static com.example.kid_toy_store.helper.GetFakeData.getFakeData;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kid_toy_store.adapter.ProductAdapter;
import com.example.kid_toy_store.databinding.ActivityDetailCategoryBinding;
import com.example.kid_toy_store.model.Products;

import java.util.ArrayList;

public class DetailCategoryActivity extends AppCompatActivity {

    ActivityDetailCategoryBinding mBinding;
    View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityDetailCategoryBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();
        setContentView(mView);

        mBinding.btnBack.setOnClickListener(v -> onBackPressed());

        setupProductRv();

    }

    private void setupProductRv() {

        ArrayList<Products> productsList = getFakeData(Products.class, 10);
        ProductAdapter productsAdapter = new ProductAdapter(productsList, this);
        RecyclerView recyclerView = mBinding.rvProduct;
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(productsAdapter);

    }
}