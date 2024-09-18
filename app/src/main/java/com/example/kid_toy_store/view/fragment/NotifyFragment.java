package com.example.kid_toy_store.view.fragment;

import static com.example.kid_toy_store.helper.GetFakeData.getFakeData;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kid_toy_store.adapter.NotifyAdapter;
import com.example.kid_toy_store.api.ApiService;
import com.example.kid_toy_store.api.RetrofitClient;
import com.example.kid_toy_store.databinding.FragmentNotifyBinding;
import com.example.kid_toy_store.model.Notifications;
import com.example.kid_toy_store.response.NotificationResponse;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifyFragment extends Fragment {

    FragmentNotifyBinding mBinding;
    View mView;

    RecyclerView rvNotify;
    NotifyAdapter notifyAdapter;
    private List<NotificationResponse> notifications = new ArrayList<>();
    private ApiService apiService;

    private String token;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = FragmentNotifyBinding.inflate(inflater);
        mView = mBinding.getRoot();

        // Khởi tạo ApiService
        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        // Lấy access_token từ SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(KeyUtils.ACCESS_TOKEN, null);

        setupNotifyRv();

        loadNotifications();

        setOnClickListener();

        return mView;
    }

    private void setupNotifyRv() {
        // Khởi tạo RecyclerView
        mBinding.rvNotify.setLayoutManager(new LinearLayoutManager(getContext()));
        // Khởi tạo NotifyAdapter với danh sách rỗng ban đầu
        notifyAdapter = new NotifyAdapter(notifications, getContext());
        mBinding.rvNotify.setAdapter(notifyAdapter);
    }

    private void setOnClickListener() {
        mBinding.tvReadNotify.setOnClickListener(v -> markAllAsRead());
        mBinding.tvRemoveAllNotify.setOnClickListener(v -> clearAllNotifications());
    }

    private void loadNotifications() {
        apiService.getUserNotifications("Bearer " + token).enqueue(new Callback<List<NotificationResponse>>() {
            @Override
            public void onResponse(Call<List<NotificationResponse>> call, Response<List<NotificationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NotificationResponse> notifications = response.body();
                    notifyAdapter.setNotifications(notifications);  // Cập nhật dữ liệu cho RecyclerView
                }
            }

            @Override
            public void onFailure(Call<List<NotificationResponse>> call, Throwable t) {
                // Xử lý lỗi khi gọi API
            }
        });
    }

    private void markAllAsRead() {
        if (token != null) {
            Call<ResponseBody> call = apiService.markAllAsRead("Bearer " + token);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // Đọc thành công, cập nhật giao diện
                        Toast.makeText(getContext(), "All notifications read!", Toast.LENGTH_SHORT).show();
                        notifyAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), "Failed to read notifications", Toast.LENGTH_SHORT).show();
                        Log.e("CLEAR ALL", response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Token is missing", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearAllNotifications() {
        if (token != null) {
            Call<ResponseBody> call = apiService.clearAllNotifications("Bearer " + token);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        // Xoá thành công, cập nhật giao diện
                        Toast.makeText(getContext(), "All notifications clear!", Toast.LENGTH_SHORT).show();
                        notifyAdapter.clearAllItems();
                    } else {
                        Toast.makeText(getContext(), "Failed to clear notifications", Toast.LENGTH_SHORT).show();
                        Log.e("CLEAR ALL", response.message());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "Token is missing", Toast.LENGTH_SHORT).show();
        }
    }
}