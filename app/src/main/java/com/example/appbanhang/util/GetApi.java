package com.example.appbanhang.util;

import com.example.appbanhang.model.Orders;
import com.example.appbanhang.model.request.ForgotPasswordEmailRequest;
import com.example.appbanhang.model.request.LoginRequest;
import com.example.appbanhang.model.request.RegisterRequest;
import com.example.appbanhang.model.request.ResetPasswordRequest;
import com.example.appbanhang.model.response.MenuResponse;
import com.example.appbanhang.model.response.OrdersResponse;
import com.example.appbanhang.model.response.UserResponse;
import com.example.appbanhang.model.response.CategoriesResponse;
import com.example.appbanhang.model.response.ProductResponse;

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

    @GET("apiCategories/get-all")
    Single<CategoriesResponse> getAllCategories();
    @GET("apiMenu/get-all")
    Single<MenuResponse> getAllMenu();

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

    @POST("apiUser/register")
    Single<UserResponse> register(@Body RegisterRequest registerRequest);

    @POST("apiUser/login")
    Single<UserResponse> login(@Body LoginRequest registerRequest);

    @POST("apiUser/forgot-password")
    Single<UserResponse> forgotPassword(@Body ForgotPasswordEmailRequest forgotPasswordEmailRequest);

    @POST("apiUser/reset-password")
    Single<UserResponse> resetPassword(@Body ResetPasswordRequest resetPasswordRequest);

    @POST("apiUser/verify-otp")
    Observable<Map<String, Object>> verifyOtp(@Body Map<String, String> data);

    @POST("/apiOrders/checkout")
    Single<OrdersResponse> checkOut(@Body Orders orderRequest);

    @GET("/apiOrders/get-order-history/{id_user}")
    Single<OrdersResponse> getOrderHistory(@Path("id_user") int idUser, @Query("page") int page, @Query("limit") int limit);
}
