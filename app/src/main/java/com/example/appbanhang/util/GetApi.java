package com.example.appbanhang.util;

import com.example.appbanhang.model.request.ForgotPasswordEmailRequest;
import com.example.appbanhang.model.request.LoginRequest;
import com.example.appbanhang.model.request.RegisterRequest;
import com.example.appbanhang.model.request.ResetPasswordRequest;
import com.example.appbanhang.model.response.UserResponse;
import com.example.appbanhang.model.response.MenuResponse;
import com.example.appbanhang.model.response.ProductResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GetApi {
    @GET("apiMenu/getall")
    Single<MenuResponse> getAllMenu();

    @GET("apiProduct/getall")
    Single<ProductResponse> getAllProduct();

    @GET("apiProduct/getCategory")
    Single<ProductResponse> getProductCategory(@Query("category") int category, @Query("page") int page, @Query("limit") int limit);

    @GET("apiProduct/getTop10_new")
    Single<ProductResponse> getProductTop10New();

    @GET("apiProduct/getTop10_bestseller")
    Single<ProductResponse> getTop10BestSeller();

    @POST("apiUser/register")
    Single<UserResponse> register(@Body RegisterRequest registerRequest);

    @POST("apiUser/login")
    Single<UserResponse> login(@Body LoginRequest registerRequest);

    @POST("apiUser/forgot-password")
    Single<UserResponse> forgotPassword(@Body ForgotPasswordEmailRequest forgotPasswordEmailRequest);

    @POST("apiUser/reset-password")
    Single<UserResponse> resetPassword(@Body ResetPasswordRequest resetPasswordRequest);


}
