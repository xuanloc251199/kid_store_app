package com.example.kid_toy_store.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.databinding.ActivityDetailPlaygroundBinding;

public class DetailPlaygroundActivity extends AppCompatActivity {

    ActivityDetailPlaygroundBinding mBinding;
    View mView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityDetailPlaygroundBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();

        mBinding.btnBack.setOnClickListener(v -> onBackPressed());

        setContentView(mView);
    }
}