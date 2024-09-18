package com.example.kid_toy_store.view.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.kid_toy_store.api.ApiService;
import com.example.kid_toy_store.api.RetrofitClient;
import com.example.kid_toy_store.databinding.ActivityUpdateProfileBinding;
import com.example.kid_toy_store.model.User;
import com.example.kid_toy_store.response.UserResponse;
import com.example.kid_toy_store.utils.ApiUtils;
import com.example.kid_toy_store.utils.KeyUtils;
import com.squareup.picasso.Picasso;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity {

    ActivityUpdateProfileBinding mBinding;
    View mView;

    Uri imageUri;

    private ApiService apiService;
    private String token;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CODE_READ_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityUpdateProfileBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();
        setContentView(mView);

        // Khởi tạo Retrofit
        apiService = RetrofitClient.getClient(ApiUtils.BASE_IP).create(ApiService.class);

        // Lấy access_token từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences(KeyUtils.SHARE_NAME_TOKEN, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(KeyUtils.ACCESS_TOKEN, null);
        Log.d("Update Activity", token);

        mBinding.imgAvatar.setOnClickListener(v -> {
            openImagePicker();
        });

        getUserProfile();

        mBinding.btnSummit.setOnClickListener(v -> {
            updateUserProfile();
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
                    mBinding.edtFullName.setText(userResponse.getUser().getName());
                    mBinding.edtEmail.setText(userResponse.getUser().getEmail());
                    mBinding.edtPhoneNumber.setText(userResponse.getUser().getNumberPhone());
                    mBinding.edtAddress.setText(userResponse.getUser().getAddress());
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

    private void openImagePicker() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ_EXTERNAL_STORAGE);
        } else {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            // Hiển thị hình ảnh trên ImageView
            mBinding.imgAvatar.setImageURI(imageUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                // Quyền không được cấp, thông báo cho người dùng
                Toast.makeText(this, "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateUserProfile() {
        String name = mBinding.edtFullName.getText().toString().trim();
        String email = mBinding.edtEmail.getText().toString().trim();
        String phone = mBinding.edtPhoneNumber.getText().toString().trim();
        String address = mBinding.edtAddress.getText().toString().trim();

        // Tạo đối tượng RequestBody cho các trường
        RequestBody nameRequestBody = RequestBody.create(MediaType.parse("text/plain"), name);
        RequestBody emailRequestBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody phoneRequestBody = RequestBody.create(MediaType.parse("text/plain"), phone);
        RequestBody addressRequestBody = RequestBody.create(MediaType.parse("text/plain"), address);

        MultipartBody.Part avatarPart = null;
        // Kiểm tra xem người dùng có chọn avatar hay không
//        if (imageUri != null) {
//            File avatarFile = getFileFromUri(imageUri);
//            if (avatarFile != null && avatarFile.exists()) {
//                RequestBody avatarRequestBody = RequestBody.create(MediaType.parse("image/*"), avatarFile);
//                avatarPart = MultipartBody.Part.createFormData("avatar", avatarFile.getName(), avatarRequestBody);
//            } else {
//                Log.e("UpdateProfile", "Avatar file does not exist");
//            }
//        } else {
//            Log.e("UpdateProfile", "No avatar selected, keeping default avatar.");
//        }

//        Call<User> call = apiService.updateUser("Bearer " + token, nameRequestBody, emailRequestBody, phoneRequestBody, addressRequestBody);
//        call.enqueue(new Callback<User>() {
//            @Override
//            public void onResponse(Call<User> call, Response<User> response) {
//                if (response.isSuccessful()) {
//                    // Nhận dữ liệu người dùng đã cập nhật từ phản hồi
//                    User updatedUser = response.body();
//                    Log.d("UpdateUser", "Updated User: " + updatedUser.toString());
//                    Intent intent = new Intent(UpdateProfileActivity.this, MainActivity.class); // Thay MainActivity bằng Activity chứa Fragment
//                    intent.putExtra("fragment", "ProfileFragment"); // Truyền thông tin để hiển thị ProfileFragment
//                    startActivity(intent);
//                    Toast.makeText(UpdateProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(UpdateProfileActivity.this, "Cập nhật thất bại: " + response.message(), Toast.LENGTH_SHORT).show();
//                    Log.e("UPDATE", response.message());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<User> call, Throwable t) {
//                Toast.makeText(UpdateProfileActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                Log.e("API_ERROR", t.getMessage());
//            }
//        });
    }


    private File getFileFromUri(Uri uri) {
        String filePath = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            filePath = cursor.getString(column_index);
            cursor.close();
        }
        if (filePath != null) {
            return new File(filePath);
        }
        return null; // Trả về null nếu không tìm thấy đường dẫn
    }
}
