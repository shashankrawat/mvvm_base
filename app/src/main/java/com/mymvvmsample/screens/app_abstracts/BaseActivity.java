package com.mymvvmsample.screens.app_abstracts;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mymvvmsample.data.app_prefs.UserSession;
import com.mymvvmsample.data.app_prefs.UserSessionImpl;
import com.mymvvmsample.utils.PreferencesUtil;
import com.mymvvmsample.utils.ProgressDialogUtils;

import static com.mymvvmsample.utils.PreferencesUtil.PREFERENCE_NAME;

public abstract class BaseActivity extends AppCompatActivity
{
    private UserSession mUserSession;
    private Gson mGson;
    private PreferencesUtil mPreferencesUtil;
    private ProgressDialog mBar;
    private ProgressDialogUtils progressUtil;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void hideKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void showSnackBar(View view, String msg) {
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
    }

    public void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    public void showProgressDialog()
    {
        progressUtil = new ProgressDialogUtils();
        mBar = progressUtil.getDialog(this);
        progressUtil.showDialog(mBar);
    }

    public void dismissProgressDialog()
    {
        if(progressUtil != null) {
            progressUtil.onDismiss(mBar);
        }
        progressUtil = null;
        mBar = null;
    }

    // get GSON Object
    public Gson getGsonBuilder() {
        if (mGson == null)
            mGson = new GsonBuilder()
                    .enableComplexMapKeySerialization()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                    .setPrettyPrinting()
                    .setVersion(1.0)
                    .setLenient()
                    .create();
        return mGson;
    }

    // get shared preference
    private PreferencesUtil getPreferencesUtil() {
        if (mPreferencesUtil == null)
            mPreferencesUtil = new PreferencesUtil(getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE));
        return mPreferencesUtil;
    }


    // get User sessions
    public UserSession getUserSession() {
        if (mUserSession == null) {
            mUserSession = UserSessionImpl.getInstance(getPreferencesUtil(), getGsonBuilder());
        }
        return mUserSession;
    }
}
