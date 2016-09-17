package com.festember16.app;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface RegisterEventService {

    @FormUrlEncoded
    @POST("events/register")
    //TODO Events register API not working
    Observable<LoginRegister> registerEvent(@Field("user_id") String userId,
                                            @Field("token") String token,
                                            @Field("event_id") int eventId);

    @FormUrlEncoded
    @POST("events/user/details")
    Observable<LoginRegister> getRegistered(@Field("user_id") String userId,
                                            @Field("token") String token);
}

