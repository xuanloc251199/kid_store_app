package com.example.kid_toy_store.api;

import com.example.kid_toy_store.model.Card;
import com.example.kid_toy_store.model.CartItem;
import com.example.kid_toy_store.request.CartRequest;
import com.example.kid_toy_store.model.Categories;
import com.example.kid_toy_store.model.Tickets;
import com.example.kid_toy_store.model.Products;
import com.example.kid_toy_store.model.User;
import com.example.kid_toy_store.model.UserProfile;
import com.example.kid_toy_store.request.CheckoutRequest;
import com.example.kid_toy_store.request.DirectPaymentRequest;
import com.example.kid_toy_store.request.PaymentRequest;
import com.example.kid_toy_store.request.UpdateCartItemRequest;
import com.example.kid_toy_store.response.CardListResponse;
import com.example.kid_toy_store.response.CardResponse;
import com.example.kid_toy_store.response.CartResponse;
import com.example.kid_toy_store.response.CheckoutResponse;
import com.example.kid_toy_store.response.DirectPaymentResponse;
import com.example.kid_toy_store.response.NotificationResponse;
import com.example.kid_toy_store.response.OrderResponse;
import com.example.kid_toy_store.response.PaymentResponse;
import com.example.kid_toy_store.response.PurchaseResponse;
import com.example.kid_toy_store.response.SearchResponse;
import com.example.kid_toy_store.response.UserResponse;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    //******** USER ********//

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
                          @Part("address") RequestBody address,
                          @Part MultipartBody.Part avatar);

    @PUT("u/update")
    Call<UserResponse> updateProfile(
            @Header("Authorization") String token,
            @Body UserProfile userProfile
    );

    @GET("u/user/getPaidOrder")
    Call<OrderResponse> getPaidOrder(@Header("Authorization") String token);

    @GET("u/categories")  // Đường dẫn API trên server của bạn
    Call<List<Categories>> getCategories();

    //******** PRODUCT ********//

    // Phương thức để lấy danh sách Product
    @GET("u/products")
    Call<List<Products>> getProducts();

    @GET("u/products/{id}")
    Call<Products> getProductById(@Path("id") int productId);

    @GET("u/products/category/{categoryId}")
    Call<List<Products>> getProductsByCategory(@Path("categoryId") int categoryId);

    //******** TICKET ********//

    // Lấy danh sách các vé sự kiện
    @GET("u/tickets")
    Call<List<Tickets>> getEvents();

    @GET("u/tickets/{id}")  // Thay thế URL theo đúng endpoint của bạn
    Call<Tickets> getTicketById(@Path("id") int ticketId);

    //******** CART ********//

    @GET("u/cart") // Đường dẫn đến API lấy giỏ hàng
    Call<CartResponse> getCart(@Header("Authorization") String token);

    @PATCH("u/cart/update/{id}")
    Call<CartItem> updateCartItem(@Path("id") int cartItemId, @Body UpdateCartItemRequest updateRequest);

    @POST("u/cart/checkout")
    Call<CheckoutResponse> checkout(@Header("Authorization") String token, @Body CheckoutRequest checkoutRequest);

    @POST("u/cart/store")
    Call<CartItem> addToCart(@Header("Authorization") String token, @Body CartRequest cartRequest);

    //******** SEARCH ********//

    // API tìm kiếm sản phẩm và vé
    @GET("u/search")
    Call<SearchResponse> search(@Query("query") String query);

    //******** NOTIFICATION ********//

    @GET("u/notification/getUserNotifications")
    Call<List<NotificationResponse>> getUserNotifications(@Header("Authorization") String token);

    @PUT("u/notification/mark-all-as-read")
    Call<ResponseBody> markAllAsRead(@Header("Authorization") String token);

    @DELETE("u/notification/clearAll")
    Call<ResponseBody> clearAllNotifications(@Header("Authorization") String token);

    @POST("/u/tickets/low-stock-notify")
    Call<Void> sendLowStockNotification(@Header("Authorization") String authToken, @Path("ticket_id") int ticketId);

    //******** PAYMENT ********//

    // API thêm card
    @POST("u/cards/store")
    Call<CardResponse> storeCard(@Header("Authorization") String token, @Body Card card);

    // API hiển thị toàn bộ card của người dùng đang đăng nhập
    @GET("u/cards/show") // API showCard
    Call<CardListResponse> showCards(@Header("Authorization") String token);

    @GET("u/cards/getCardById/{id}") // API showCard
    Call<Card> getCardById(@Header("Authorization") String token, @Path("id") int cardId);

    @GET("u/cards/getCardById")
    Call<Card> getFirstCard(@Header("Authorization") String token);

    @PUT("u/cards/update/{id}") // API updateCard
    Call<CardResponse> updateCard(@Header("Authorization") String token, @Path("id") int cardId, @Body Card card);

    @POST("u/cards/processPayment")
    Call<PaymentResponse> processPayment(
            @Header("Authorization") String token,
            @Body PaymentRequest paymentRequest
    );
    @POST("u/cards/directPayment")
    Call<DirectPaymentResponse> directPayment(
            @Header("Authorization") String token,
            @Body DirectPaymentRequest directPaymentRequest
    );

    @GET("u/cards/purchases") // API showCard
    Call<PurchaseResponse> getPurchasedItems(@Header("Authorization") String token);
}
