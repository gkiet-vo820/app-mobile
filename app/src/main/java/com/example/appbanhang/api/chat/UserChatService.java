package com.example.appbanhang.api.chat;

import android.content.Context;

import com.example.appbanhang.listener.GetApi;
import com.example.appbanhang.model.User;
import com.example.appbanhang.service.RetrofitClient;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UserChatService {
    private Context context;
    private GetApi getApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public UserChatService(Context context) {
        this.context = context;
        getApi = RetrofitClient.getInstance().create(GetApi.class);
    }

    public interface UserChatListener {
        void onSuccess(List<User> dsUser);
        void onError(String message);
    }

    public void getAllUsersExcept(int id, UserChatListener listener){
        compositeDisposable.add(
                getApi.getAllUsersExcept(id)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if(res.isSuccess()){
                                        listener.onSuccess(res.getData());
                                    }
                                },
                                err -> {
                                    listener.onError(err.getMessage());
                                }
                        )
        );
    }

    public void getChatList(int id, int role, UserChatListener listener){
        compositeDisposable.add(
                getApi.getChatList(id, role)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                res -> {
                                    if(res.isSuccess()){
                                        listener.onSuccess(res.getData());
                                    }
                                },
                                err -> {
                                    listener.onError(err.getMessage());
                                }
                        )
        );
    }

    public void clear(){
        compositeDisposable.clear();
    }
}
