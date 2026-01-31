package com.example.appbanhang.api;

import com.example.appbanhang.model.MenuResponse;
import com.example.appbanhang.model.ProductResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ProductApi {
    @GET("apiMenu/getall")
    Single<MenuResponse> getAllMenu();

    @GET("apiProduct/getall")
    Single<ProductResponse> getAllProduct();

    @GET("apiProduct/getCategory")
    Single<ProductResponse> getProductCategory(
            @Query("category") int category,
            @Query("page") int page,
            @Query("limit") int limit
    );
}
