package com.example.kid_toy_store.activity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.databinding.ActivityEditShippingAddressBinding;

public class EditShippingAddressActivity extends AppCompatActivity {

    ActivityEditShippingAddressBinding mBinding;
    View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityEditShippingAddressBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();

        setOnClickListener(mBinding);

        setContentView(mView);

    }

    private void setOnClickListener(ActivityEditShippingAddressBinding binding) {
        binding.btnBack.setOnClickListener(v -> onBackPressed());
    }
}