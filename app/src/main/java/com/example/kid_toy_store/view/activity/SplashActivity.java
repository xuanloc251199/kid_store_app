package com.example.kid_toy_store.view.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.content.Intent;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.utils.KeyUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, MODE_PRIVATE);
                String token = sharedPreferences.getString(KeyUtils.ACCESS_TOKEN, null);

                if (token != null) {
                    // Người dùng đã đăng nhập, chuyển thẳng đến MainActivity
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                } else {
                    // Người dùng chưa đăng nhập, chuyển đến LoginActivity
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                } // Kết thúc SplashActivity
            }
        }, 3000);

    }
}