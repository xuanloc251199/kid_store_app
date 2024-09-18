package com.example.kid_toy_store.view.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kid_toy_store.databinding.ActivityOrderSuccessfullyBinding;
import com.example.kid_toy_store.helper.CustomStartActivity;

public class OrderSuccessfullyActivity extends AppCompatActivity {

    ActivityOrderSuccessfullyBinding mBinding;
    View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityOrderSuccessfullyBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();

        setOnClickListener(mBinding);

        setContentView(mView);
    }

    private void setOnClickListener(ActivityOrderSuccessfullyBinding binding) {
        binding.btnBack.setOnClickListener(v -> CustomStartActivity.customStartActivity(this, MainActivity.class));
    }
}