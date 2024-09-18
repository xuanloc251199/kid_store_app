package com.example.kid_toy_store.api;

import com.example.kid_toy_store.model.CartItem;
import com.example.kid_toy_store.model.Categories;
import com.example.kid_toy_store.model.Tickets;
import com.example.kid_toy_store.model.Products;
import com.example.kid_toy_store.model.User;
import com.example.kid_toy_store.request.CheckoutRequest;
import com.example.kid_toy_store.request.UpdateCartItemRequest;
import com.example.kid_toy_store.response.CartResponse;
import com.example.kid_toy_store.response.CheckoutResponse;
import com.example.kid_toy_store.response.UserResponse;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @FormUrlEncoded
    @POST("u/login")
    Call<UserResponse> loginUser(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("u/register")
    Call<UserResponse> registerUser(
            @Field("name") String name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("password_confirmation") String passwordConfirmation
    );

    // Đăng xuất
    @POST("u/logout")
    Call<Void> logout(@Header("Authorization") String token);

    @GET("u/profile")
    Call<UserResponse> getUserProfile(@Header("Authorization") String token);

    @Multipart
    @PUT("u/user/update")
    Call<User> updateUser(@Header("Authorization") String token,
                          @Part("name") RequestBody name,
                          @Part("email") RequestBody email,
                          @Part("number_phone") RequestBody numberPhone,
                          @Part("address") RequestBody address);

    @GET("u/categories")  // Đường dẫn API trên server của bạn
    Call<List<Categories>> getCategories();

    // Phương thức để lấy danh sách Product
    @GET("u/products")  // Thay thế URL theo đúng endpoint của bạn
    Call<List<Products>> getProducts();

    @GET("u/products/{id}")  // Thay thế URL theo đúng endpoint của bạn
    Call<Products> getProductById(@Path("id") int productId);

    // Lấy danh sách các vé sự kiện
    @GET("u/tickets")  // Thay đường dẫn này thành API thực tế của bạn
    Call<List<Tickets>> getEvents();

    @POST("u/cart/store") // Đường dẫn đến API thêm sản phẩm vào giỏ hàng
    Call<CartItem> addToCart(
            @Header("Authorization") String token,
            @Body CartItem cartItem);

    @GET("u/cart") // Đường dẫn đến API lấy giỏ hàng
    Call<CartResponse> getCart(@Header("Authorization") String token);

    @PATCH("u/cart/update/{id}")
    Call<CartItem> updateCartItem(@Path("id") int cartItemId, @Body UpdateCartItemRequest updateRequest);

    @Headers("Content-Type: application/json")
    @POST("u/checkout")
    Call<CheckoutResponse> checkout(@Body CheckoutRequest checkoutRequest);
}
