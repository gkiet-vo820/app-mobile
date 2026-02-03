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

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @POST("apiUser/register")
    Single<UserResponse> register(@Body RegisterRequest registerRequest);

    @POST("apiUser/login")
    Single<UserResponse> login(@Body LoginRequest registerRequest);

    @POST("apiUser/forgot-password")
    Single<UserResponse> forgotPassword(@Body ForgotPasswordEmailRequest forgotPasswordEmailRequest);

    @POST("apiUser/reset-password")
    Single<UserResponse> resetPassword(@Body ResetPasswordRequest resetPasswordRequest);

    @POST("/apiOrders/checkout")
    Single<OrdersResponse> checkOut(@Body Orders orderRequest);

    @GET("/apiOrders/get-order-history/{id_user}")
    Single<OrdersResponse> getOrderHistory(@Path("id_user") int idUser, @Query("page") int page, @Query("limit") int limit);
}
