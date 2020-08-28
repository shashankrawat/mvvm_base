package com.mymvvmsample.data.network;

import com.google.gson.JsonObject;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiUtils {
    String HOST = "http://3.218.105.239:3000/";
    String URL_MASTER = HOST + "api/";

    @POST("loginFB")
    Call<JsonObject> fbLogin(@Body JsonObject obj);

    @POST("loginINSTA")
    Call<JsonObject> instaLogin(@Body JsonObject obj);
}
