package com.mygdx.game;

import retrofit2.http.GET;
import rx.Observable;

public interface EventsInterface {
    @GET("events/details")
    Observable<Data> getEvents();
}
