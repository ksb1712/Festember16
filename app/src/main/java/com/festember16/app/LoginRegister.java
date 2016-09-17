
package com.festember16.app;

import com.google.gson.annotations.SerializedName;


public class LoginRegister {

    @SerializedName("status_code")
    public Integer statusCode;

    @SerializedName("message")
    public String message;

    @SerializedName("user_id")
    public String userId;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
