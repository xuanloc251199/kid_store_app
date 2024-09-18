package com.example.kid_toy_store.view.fragment;

import static com.example.kid_toy_store.helper.CustomStartActivity.customStartActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kid_toy_store.view.activity.AllPaymentCardActivity;
import com.example.kid_toy_store.view.activity.LoginActivity;
import com.example.kid_toy_store.view.activity.PaidOrdersActivity;
import com.example.kid_toy_store.view.activity.UpdateProfileActivity;
import com.example.kid_toy_store.api.ApiService;
import com.example.kid_toy_store.api.RetrofitClient;
import com.example.kid_toy_store.databinding.FragmentProfileBinding;
import com.example.kid_toy_store.response.UserResponse;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    FragmentProfileBinding mBinding;
    View mView;

    private String TAG = "Profile Fragment";
    private ApiService apiService;
    private String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentProfileBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();

        // Khởi tạo Retrofit
        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        // Lấy access_token từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(KeyUtils.ACCESS_TOKEN, null);
        Log.d(TAG, token);

        getUserProfile();

        setOnClickListener();

        return mView;
    }

    private void setOnClickListener() {
        mBinding.tvEditProfile.setOnClickListener(v -> startActivity(new Intent(getContext(), UpdateProfileActivity.class)));
        mBinding.cvPaymentCard.setOnClickListener(v -> customStartActivity(getContext(), AllPaymentCardActivity.class));
        mBinding.cvPairOrder.setOnClickListener(v -> customStartActivity(getContext(), PaidOrdersActivity.class));
        mBinding.btnLogout.setOnClickListener(v -> {
            logoutUser();
        });
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
                    mBinding.tvFullName.setText(userResponse.getUser().getName());
                    mBinding.tvEmail.setText(userResponse.getUser().getEmail());
                    mBinding.tvPhoneNumber.setText(userResponse.getUser().getNumberPhone());
                    Picasso.get().load(ApiUtils.IP_DEFAULT + userResponse.getUser().getAvatar()).into(mBinding.imgAvatar);
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

    private void logoutUser() {

        Call<Void> call = apiService.logout("Bearer " + token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    // Xử lý đăng xuất thành công (xóa token, cập nhật UI, v.v.)
                    // Hiển thị thông báo đăng xuất thành công
                    Toast.makeText(getActivity(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();

                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.remove(KeyUtils.ACCESS_TOKEN); // Xóa token
                    editor.apply();

                    // Chuyển hướng đến màn hình đăng nhập
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);

                    // Kết thúc MainActivity để người dùng không quay lại màn hình này sau khi logout
                    requireActivity().finish();
                } else {
                    Log.e(TAG, "API Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "API Failure: " + t.getMessage());
            }
        });
    }

}