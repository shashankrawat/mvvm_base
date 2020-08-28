package com.mymvvmsample.screens.base.view_model;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import com.google.gson.JsonObject;
import com.mymvvmsample.data.network.Resource;

public class CommonSignInViewModel extends AndroidViewModel
{
    private MutableLiveData<JsonObject> fbSignIn = new MutableLiveData<>();
    private LiveData<Resource<String>> fbSignInLD;

    private MutableLiveData<JsonObject> instaSignIn = new MutableLiveData<>();
    private LiveData<Resource<String>> instaSignInLD;

    public CommonSignInViewModel(@NonNull Application application) {
        super(application);

        fbSignInLD = Transformations.switchMap(fbSignIn, new Function<JsonObject, LiveData<Resource<String>>>() {
            @Override
            public LiveData<Resource<String>> apply(JsonObject input) {
                return CommonSignInRepo.get().FBSignIn(input);
            }
        });

        instaSignInLD = Transformations.switchMap(instaSignIn, new Function<JsonObject, LiveData<Resource<String>>>() {
            @Override
            public LiveData<Resource<String>> apply(JsonObject input) {
                return CommonSignInRepo.get().InstaSignIn(application.getBaseContext(), input);
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
