package com.example.kid_toy_store.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.api.ApiService;
import com.example.kid_toy_store.api.RetrofitClient;
import com.example.kid_toy_store.databinding.ActivityLoginBinding;
import com.example.kid_toy_store.model.User;
import com.example.kid_toy_store.response.UserResponse;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity{

    ActivityLoginBinding mBinding;
    View mView;
    private boolean isPasswordVisible = false;
    ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();
        setContentView(mView);

        // Khởi tạo Retrofit với baseUrl từ ApiUtils
        Retrofit retrofit = RetrofitClient.getClient(ApiUtils.BASE_IP); // BASE_IP phải chính xác
        apiService = retrofit.create(ApiService.class);

        mBinding.editPassword.setOnTouchListener((@SuppressLint("ClickableViewAccessibility") View v, @SuppressLint("ClickableViewAccessibility") MotionEvent event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) { // Kiểm tra sự kiện ACTION_UP
                if (event.getRawX() >= (mBinding.editPassword.getRight() - mBinding.editPassword.getCompoundDrawables()[2].getBounds().width())) {
                    togglePasswordVisibility(); // Chuyển đổi hiển thị/ẩn mật khẩu
                    return true;
                }
            }
            return false;
        });

        mBinding.btnLogin.setOnClickListener(v -> {
            String email = mBinding.editEmail.getText().toString().trim();
            String password = mBinding.editPassword.getText().toString().trim();
            if (!email.isEmpty() && !password.isEmpty()) {
                loginUser(email, password);
            } else {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập email và mật khẩu", Toast.LENGTH_SHORT).show();
            }
        });

        mBinding.btnRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });

    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Ẩn mật khẩu và đổi icon sang ic_show
            mBinding.editPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_show, 0);
            mBinding.editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance()); // Đặt lại chế độ ẩn mật khẩu
        } else {
            // Hiển thị mật khẩu và đổi icon sang ic_hide
            mBinding.editPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_hide, 0);
            mBinding.editPassword.setTransformationMethod(null); // Bỏ chế độ ẩn để hiển thị mật khẩu
        }
        isPasswordVisible = !isPasswordVisible; // Đổi trạng thái mật khẩu

        // Đảm bảo con trỏ vẫn ở cuối văn bản sau khi thay đổi
        mBinding.editPassword.setSelection(mBinding.editPassword.getText().length());
    }

    private void loginUser(String email, String password) {
        Call<UserResponse> call = apiService.loginUser(email, password);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null) {
                        // Lấy token và thông tin người dùng
                        String token = userResponse.getAccessToken();
                        User user = userResponse.getUser();

                        // Lưu token vào SharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(KeyUtils.ACCESS_TOKEN, token);
                        editor.apply();

                        // Chuyển sang MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();  // Đóng LoginActivity để không quay lại

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Login Error", "Response Code: " + response.code());
                    Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Login Error", t.getMessage());
                Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }


}