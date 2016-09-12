package com.festember16.app;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface EventsInterface {
    @GET("/* TODO Add url to query here */")
    Observable<Data> getEvents(@Query("token") String token);
}
