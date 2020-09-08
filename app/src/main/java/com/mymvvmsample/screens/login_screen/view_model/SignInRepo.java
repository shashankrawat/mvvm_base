package com.mymvvmsample.screens.login_screen.view_model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.mymvvmsample.data.app_prefs.UserSessionImpl;
import com.mymvvmsample.data.network.CallServer;
import com.mymvvmsample.data.network.Resource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInRepo
{

    public static SignInRepo get() {
        return new SignInRepo();
    }


    public LiveData<Resource<String>> FBSignIn(JsonObject obj)
    {
        final MutableLiveData<Resource<String>> data = new MutableLiveData<>();
        data.setValue(Resource.loading(null));

        CallServer.get().getAPIName().fbLogin(obj).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().get("error").getAsString().equalsIgnoreCase("false"))
                        {
//                            UserSessionImpl.getInstance().saveUserData(response.body().get("userInfo").getAsJsonObject().toString());
//                            UserSessionImpl.getInstance().saveUserToken(response.body().get("token").getAsString());

                            data.setValue(Resource.success(response.body().get("message").getAsString()));
                        } else
                            data.setValue(Resource.error(response.body().get("message").getAsString(), null, 0, null));
                    }

                } else
                    data.setValue(Resource.<String>error(response.message(), null, 0, null));

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                data.setValue(Resource.<String>error(CallServer.serverError, null, 0, t));

            }
        });
        return data;
    }


    public LiveData<Resource<String>> InstaSignIn(JsonObject obj)
    {
        final MutableLiveData<Resource<String>> data = new MutableLiveData<>();
        data.setValue(Resource.<String>loading(null));

        CallServer.get().getAPIName().instaLogin(obj).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().get("error").getAsString().equalsIgnoreCase("false"))
                        {
                            data.setValue(Resource.success(response.body().get("message").getAsString()));
                        } else
                            data.setValue(Resource.<String>error(response.body().get("message").getAsString(), null, 0, null));
                    }

                } else
                    data.setValue(Resource.<String>error(response.message(), null, 0, null));

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                data.setValue(Resource.<String>error(CallServer.serverError, null, 0, t));

            }
        });
        return data;
    }

}
