package com.festember16.app;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoginInterface {
    @GET("api/{email}/{password}")
    Call<Login> doAuth(@Path("username") String username, @Path("password") String password);
    @POST("api/{email}/{password}")
    Call<Login> register(@Path("username") String email, @Path("password") String password);
}
