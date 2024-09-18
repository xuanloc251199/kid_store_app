package com.example.kid_toy_store.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.example.kid_toy_store.databinding.ActivityRegisterBinding;
import com.example.kid_toy_store.response.UserResponse;
import com.example.kid_toy_store.utils.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding mBinding;
    View mView;

    private boolean isPasswordVisible = false;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRegisterBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();
        setContentView(mView);

        // Khởi tạo Retrofit
        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        mBinding.editPassword.setOnTouchListener((@SuppressLint("ClickableViewAccessibility") View v, @SuppressLint("ClickableViewAccessibility") MotionEvent event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) { // Kiểm tra sự kiện ACTION_UP
                if (event.getRawX() >= (mBinding.editPassword.getRight() - mBinding.editPassword.getCompoundDrawables()[2].getBounds().width())) {
                    togglePasswordVisibility(); // Chuyển đổi hiển thị/ẩn mật khẩu
                    return true;
                }
            }
            return false;
        });

        mBinding.editConfirmPassword.setOnTouchListener((@SuppressLint("ClickableViewAccessibility") View v, @SuppressLint("ClickableViewAccessibility") MotionEvent event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) { // Kiểm tra sự kiện ACTION_UP
                if (event.getRawX() >= (mBinding.editPassword.getRight() - mBinding.editPassword.getCompoundDrawables()[2].getBounds().width())) {
                    togglePasswordVisibility(); // Chuyển đổi hiển thị/ẩn mật khẩu
                    return true;
                }
            }
            return false;
        });

        mBinding.btnRegister.setOnClickListener(v -> {

            String name = mBinding.editFullName.getText().toString().trim();
            String email = mBinding.editEmail.getText().toString().trim();
            String password = mBinding.editPassword.getText().toString().trim();
            String passwordConfirm = mBinding.editConfirmPassword.getText().toString().trim();

            if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && !passwordConfirm.isEmpty()) {
                if (password.equals(passwordConfirm)) {
                    registerUser(name, email, password, passwordConfirm);
                } else {
                    Toast.makeText(RegisterActivity.this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            }
;
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

    // Phương thức để đăng ký người dùng mới
    private void registerUser(String name, String email, String password, String passwordConfirmation) {
        Call<UserResponse> call = apiService.registerUser(name, email, password, passwordConfirmation);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    UserResponse userResponse = response.body();
                    if (userResponse != null) {
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();  // Đóng RegisterActivity sau khi chuyển
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("Register Error", "Response Code: " + response.code());
                    Toast.makeText(RegisterActivity.this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("Register Error", t.getMessage());
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}