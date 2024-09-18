package com.example.kid_toy_store.view.activity;

import static com.example.kid_toy_store.helper.CustomStartActivity.customStartActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.kid_toy_store.R;
import com.example.kid_toy_store.api.ApiService;
import com.example.kid_toy_store.api.RetrofitClient;
import com.example.kid_toy_store.databinding.ActivityEditShippingAddressBinding;
import com.example.kid_toy_store.model.User;
import com.example.kid_toy_store.model.UserProfile;
import com.example.kid_toy_store.response.UserResponse;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditShippingAddressActivity extends AppCompatActivity {

    String TAG = "EditShippingAddressActivity";

    ActivityEditShippingAddressBinding mBinding;
    View mView;

    private ApiService apiService;
    private String token;

    String name, shippingAddress, address, country, city, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityEditShippingAddressBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();
        setContentView(mView);

        // Khởi tạo Retrofit
        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        // Lấy access_token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(KeyUtils.ACCESS_TOKEN, null);

        getUserProfile();

        setOnClickListener();



    }

    private void setOnClickListener() {
        mBinding.btnBack.setOnClickListener(v -> finish());

        mBinding.btnSaveAddress.setOnClickListener(v -> updateUser());

    }

    private void getUserProfile() {
        Call<UserResponse> call = apiService.getUserProfile("Bearer " + token);

        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    // Xử lý khi yêu cầu thành công
                    UserResponse userResponse = response.body();
                    // Hiển thị thông tin người dùng từ userResponse
                    mBinding.edtName.setText(userResponse.getUser().getName());
                    mBinding.edtAddress.setText(userResponse.getUser().getAddress());
                    mBinding.edtPhoneNumber.setText(userResponse.getUser().getNumberPhone());
                } else {
                    // Kiểm tra lỗi
                    Log.e("API Error", "Response Code: " + response.code());
                    try {
                        Log.e("API Error", "Response Body: " + response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Log.e("API Error", t.getMessage());
            }
        });
    }

    private void updateUser() {
        name = mBinding.edtName.getText().toString().trim();
        country = mBinding.edtCountry.getText().toString().trim();
        city = mBinding.edtCity.getText().toString().trim();
        address = mBinding.edtAddress.getText().toString().trim();
        shippingAddress = country + ", " + city + ", " + address;
        phoneNumber = mBinding.edtPhoneNumber.getText().toString().trim();

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", name);
        userInfo.put("number_phone", phoneNumber);
        userInfo.put("address", shippingAddress);

        UserProfile userProfile = new UserProfile(name, phoneNumber, shippingAddress);

        Call<UserResponse> call = apiService.updateProfile("Bearer " + token, userProfile);
        call.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    // Xử lý khi cập nhật thành công
                    Toast.makeText(EditShippingAddressActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    UserResponse userResponse = response.body();
                    customStartActivity(EditShippingAddressActivity.this, DirectPaymentActivity.class);
                    finish();
                } else {
                    Toast.makeText(EditShippingAddressActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, response.message() + response.code());
                    try {
                        // In ra phản hồi lỗi từ server
                        Log.e(TAG, "Lỗi từ server: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                // Xử lý lỗi mạng
                t.printStackTrace();
            }
        });
    }
}