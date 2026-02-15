package com.example.appbanhang.service;

import com.example.appbanhang.model.eventbus.LogoutEvent;
import com.example.appbanhang.util.Configure;

import org.greenrobot.eventbus.EventBus;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.paperdb.Paper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder();

                    // Lấy token đã lưu từ Paper
                    String token = Paper.book().read("token");
                    if (token != null) {
                        // Đính kèm Token theo chuẩn Bearer
                        requestBuilder.header("Authorization", "Bearer " + token);
                    }

                    Request request = requestBuilder.build();
                    Response response = chain.proceed(request);

                    if (response.code() == 401 || response.code() == 403) {
                        // Xóa dữ liệu cũ
                        Paper.book().delete("token");
                        Paper.book().delete("user");

                        if (response.code() == 401) {
                            EventBus.getDefault().post(new LogoutEvent("Hết hạn đăng nhập"));
                        }
                        // Bắn một Intent hoặc dùng EventBus để yêu cầu Activity chuyển về màn hình Login
                        // Intent intent = new Intent(context, LoginActivity.class);
                        // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        // context.startActivity(intent);
                    }
                    return response;
                })
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Configure.URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
