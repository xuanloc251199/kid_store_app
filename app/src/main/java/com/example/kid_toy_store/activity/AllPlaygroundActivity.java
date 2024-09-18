package com.example.kid_toy_store.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kid_toy_store.databinding.ActivityAllPlaygroundBinding;

public class AllPlaygroundActivity extends AppCompatActivity {

    ActivityAllPlaygroundBinding mBinding;
    View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityAllPlaygroundBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();

        mBinding.btnBack.setOnClickListener(v -> onBackPressed());

        setContentView(mView);
    }
}