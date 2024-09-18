package com.example.kid_toy_store.view.activity;

import static com.example.kid_toy_store.helper.CustomStartActivity.customStartActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.databinding.ActivityCheckOutBinding;
import com.example.kid_toy_store.databinding.ActivityPaymentMethodBinding;
import com.example.kid_toy_store.utils.KeyUtils;

public class PaymentMethodActivity extends AppCompatActivity {

    ActivityPaymentMethodBinding mBinding;
    private double totalPrice;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityPaymentMethodBinding.inflate(getLayoutInflater());
        View mView = mBinding.getRoot();
        setContentView(mView);

        totalPrice = getIntent().getDoubleExtra(KeyUtils.ORDER_KEY, 0.0);

        mBinding.rgPaymentMethod.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbCash) {
                Toast.makeText(this, "Bạn đã chọn Thanh toán trực tiếp", Toast.LENGTH_SHORT).show();
            }else if (checkedId == R.id.rbCard) {
                Toast.makeText(this, "Bạn đã chọn Thanh toán bằng thẻ", Toast.LENGTH_SHORT).show();
            }
        });

        setOnClickListener();
    }

    private void setOnClickListener() {
        mBinding.btnCheckout.setOnClickListener(v -> {
            int selectedMethodId = mBinding.rgPaymentMethod.getCheckedRadioButtonId();
            if (selectedMethodId == -1) {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent;
            if (selectedMethodId == R.id.rbCash) {
                // Chuyển đến màn hình DirectPaymentActivity
                intent = new Intent(PaymentMethodActivity.this, DirectPaymentActivity.class);
            } else if (selectedMethodId == R.id.rbCard) {
                // Chuyển đến màn hình CheckOutActivity
                intent = new Intent(PaymentMethodActivity.this, CheckOutActivity.class);
            } else {
                Toast.makeText(this, "Phương thức thanh toán không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            // Truyền totalPrice qua Intent
            intent.putExtra(KeyUtils.ORDER_KEY, totalPrice);
            startActivity(intent);
        });
    }
}