package com.cegep.saporiitaliano;

import com.cegep.saporiitaliano.model.PushNotification;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("Pushvalue")
    Call<PushNotification> loginUser();

//    @GET("Pushvalue")
//    Call<PushNotification> signup(
//            @Query("username") String asdasd,
//            @Query("pass") String qweq,
//            @Query("username") String email,
//            @Query("username") String email,
//            @Query("username") String email
//    );
}
