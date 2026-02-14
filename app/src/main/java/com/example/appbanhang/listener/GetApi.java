package com.example.appbanhang.listener;

import com.example.appbanhang.model.Orders;
import com.example.appbanhang.model.Statistical;
import com.example.appbanhang.model.User;
import com.example.appbanhang.model.request.ChangePasswordRequest;
import com.example.appbanhang.model.request.ForgotPasswordEmailRequest;
import com.example.appbanhang.model.request.LoginRequest;
import com.example.appbanhang.model.request.RegisterRequest;
import com.example.appbanhang.model.request.ResetPasswordRequest;
import com.example.appbanhang.model.response.DetailOrderResponse;
import com.example.appbanhang.model.response.MenuResponse;
import com.example.appbanhang.model.response.NotificationResponse;
import com.example.appbanhang.model.response.OrdersResponse;
import com.example.appbanhang.model.response.UserResponse;
import com.example.appbanhang.model.response.CategoriesResponse;
import com.example.appbanhang.model.response.ProductResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetApi {

    // Loại sản phẩm
    @GET("apiCategories/get-all")
    Single<CategoriesResponse> getAllCategories();
    @GET("apiMenu/get-all")
    Single<MenuResponse> getAllMenu();

    // Sản phẩm
    @GET("apiProduct/get-all")
    Single<ProductResponse> getAllProduct();

    @GET("apiProduct/get-category")
    Single<ProductResponse> getProductCategory(@Query("category") int category, @Query("page") int page, @Query("limit") int limit);

    @GET("apiProduct/get-top10-new")
    Single<ProductResponse> getProductTop10New();

    @GET("apiProduct/get-top10-bestseller")
    Single<ProductResponse> getTop10BestSeller();

    @GET("apiProduct/search-product")
    Single<ProductResponse> searchProduct(@Query("keyword") String keyword);

    @GET("apiProduct/search-product-category")
    Single<ProductResponse> searchProductCategory(@Query("keyword") String keyword, @Query("category") Integer category);

    @Multipart
    @POST("apiProduct/add-product")
    Call<ProductResponse> addProduct(@Part("nameProduct") RequestBody name, @Part("price") RequestBody price,
                                     @Part MultipartBody.Part image, @Part("description") RequestBody description,
                                     @Part("categoryId") RequestBody categoryId, @Part("stockQuantity") RequestBody stockQuantity);


    @Multipart
    @POST("apiProduct/update-product")
    Call<ProductResponse> updateProduct(@Part("id") RequestBody id, @Part("nameProduct") RequestBody name, @Part("price") RequestBody price,
                                        @Part MultipartBody.Part image, @Part("description") RequestBody description,
                                        @Part("categoryId") RequestBody categoryId, @Part("stockQuantity") RequestBody stockQuantity);

    @POST("apiProduct/delete-product")
    Single<ProductResponse> deleteProduct(@Query("id") Integer id);

    @POST("apiProduct/update-stock")
    Single<ProductResponse> updateStock(@Query("id") Integer id, @Query("stockQuantity") Integer stockQuantity);

    @GET("apiProduct/get-low-stock")
    Single<ProductResponse> getLowStock(@Query("stock") Integer stock);

    // Người dùng
    @POST("apiUser/register")
    Single<UserResponse> register(@Body RegisterRequest registerRequest);

    @POST("apiUser/login")
    Single<UserResponse> login(@Body LoginRequest registerRequest);

    @POST("apiUser/forgot-password")
    Single<UserResponse> forgotPassword(@Body ForgotPasswordEmailRequest forgotPasswordEmailRequest);

    @POST("apiUser/reset-password")
    Single<UserResponse> resetPassword(@Body ResetPasswordRequest resetPasswordRequest);

    @POST("apiUser/change-password")
    Single<UserResponse> changePassword(@Body ChangePasswordRequest changePasswordRequest);

    @POST("apiUser/update-profile")
    Single<UserResponse> updateProfile(@Body User user);

    @POST("apiUser/verify-otp")
    Observable<Map<String, Object>> verifyOtp(@Body Map<String, String> data);

    @POST("apiUser/update-token")
    Single<Map<String, Object>> updateToken(@Body Map<String, String> data);

    @GET("apiUser/get-all-users-except/{id}")
    Single<UserResponse> getAllUsersExcept(@Path("id") Integer id);

    @GET("apiUser/get-chat-list/{id}/{role}")
    Single<UserResponse> getChatList(@Path("id") Integer id, @Path("role") Integer role);

    // Đơn hàng
    @POST("apiOrders/checkout")
    Single<OrdersResponse> checkOut(@Body Orders orderRequest);

    @POST("apiOrders/create-vnpay-url")
    Single<Map<String, String>> checkOutVNPay(@Body Map<String, Object> data);

    @POST("apiOrders/confirm-payment/{orderId}")
    Observable<Map<String, Object>> confirmPayment(@Path("orderId") int orderId);

    @GET("apiOrders/get-order-history/{user_id}")
    Single<OrdersResponse> getOrderHistory(@Path("user_id") int userId, @Query("page") int page, @Query("limit") int limit);

    @GET("apiOrders/get-order-detail/{id}")
    Single<DetailOrderResponse> getOrderDetail(@Path("id") int id);

    @POST("apiOrders/update-status/{orderId}")
    Call<Map<String, Object>> updateOrderStatus(@Path("orderId") int orderId, @Query("status") int status);

    @GET("apiOrders/get-all-orders-admin")
    Single<OrdersResponse> getAllOrdersAdmin(@Query("page") int page, @Query("limit") int limit);

    @GET("apiOrders/get-by-status-admin")
    Single<OrdersResponse> getByStatusAdmin(@Query("status") int status, @Query("page") int page, @Query("limit") int limit);

    @POST("apiOrders/get-revenue-7-days")
    Single<List<Statistical>> getRevenue7Days();

    // Thông báo
    @GET("apiNotification/get-notification/{user_id}")
    Single<NotificationResponse> getNotification(@Path("user_id") int userId);

    @POST("apiNotification/read-notification/{id}")
    Single<NotificationResponse> readNotification(@Path("id") int id);
}
