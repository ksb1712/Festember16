package com.festember16.app;

import java.util.Map;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface LoginService {

    @FormUrlEncoded
    @POST("auth/app")
    Observable<LoginRegister> authenticate(@Field("user_email") String user_email, @Field("user_pass") String password);

    @FormUrlEncoded
    @POST("user/register/")
    Observable<LoginRegister> register(@FieldMap Map<String, String> parameters);
}