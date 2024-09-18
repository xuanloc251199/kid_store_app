package com.example.kid_toy_store.activity;

import static com.example.kid_toy_store.helper.CustomStartActivity.customStartActivity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.example.kid_toy_store.databinding.ActivityCheckOutBinding;

public class CheckOutActivity extends AppCompatActivity {

    ActivityCheckOutBinding mBinding;
    View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityCheckOutBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();

        setOnClickListener(mBinding);

        setContentView(mView);
    }

    private void setOnClickListener(ActivityCheckOutBinding binding) {

        binding.btnBack.setOnClickListener(v -> onBackPressed());
        binding.btnPlaceOrder.setOnClickListener(v -> customStartActivity(this, OrderSuccessfullyActivity.class));

        binding.cvPaymentMethod.setOnClickListener(v -> customStartActivity(this, EditPaymentMethodActivity.class));
        binding.cvShippingAddress.setOnClickListener(v -> customStartActivity(this, EditShippingAddressActivity.class));
    }
}