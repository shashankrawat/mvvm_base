package com.mymvvmsample.screens.login_screen.view_model;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.google.gson.JsonObject;
import com.mymvvmsample.data.app_prefs.UserSession;
import com.mymvvmsample.data.network.Resource;

public class SignInViewModel extends AndroidViewModel
{

    private MutableLiveData<JsonObject> fbSignIn = new MutableLiveData<>();
    private LiveData<Resource<String>> fbSignInLD;

    private MutableLiveData<JsonObject> instaSignIn = new MutableLiveData<>();
    private LiveData<Resource<String>> instaSignInLD;

    public SignInViewModel(@NonNull Application application) {
        super(application);
        fbSignInLD = Transformations.switchMap(fbSignIn, new Function<JsonObject, LiveData<Resource<String>>>() {
            @Override
            public LiveData<Resource<String>> apply(JsonObject input) {
                return SignInRepo.get().FBSignIn(input);
            }
        });

        instaSignInLD = Transformations.switchMap(instaSignIn, new Function<JsonObject, LiveData<Resource<String>>>() {
            @Override
            public LiveData<Resource<String>> apply(JsonObject input) {
                return SignInRepo.get().InstaSignIn(input);
            }
        });
    }


    public void fbSignIn(JsonObject s) {
        fbSignIn.setValue(s);
    }
    public LiveData<Resource<String>> fbSignIn() {
        return fbSignInLD;
    }


    public void instaSignIn(JsonObject s) {
        instaSignIn.setValue(s);
    }
    public LiveData<Resource<String>> instaSignIn() {
        return instaSignInLD;
    }
}
