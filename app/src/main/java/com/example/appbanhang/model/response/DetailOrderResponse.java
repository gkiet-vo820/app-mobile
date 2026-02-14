package com.example.appbanhang.model.response;

import com.example.appbanhang.model.Orders;

public class DetailOrderResponse {
    private boolean success;
    private String message;
    private Orders data;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Orders getData() {
        return data;
    }

    public void setData(Orders data) {
        this.data = data;
    }
}
