package com.mymvvmsample.screens.login_screen;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.os.Bundle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.JsonObject;
import com.mymvvmsample.R;
import com.mymvvmsample.data.app_prefs.UserSessionImpl;
import com.mymvvmsample.data.network.Resource;
import com.mymvvmsample.data.social_login.LoginViaSocialAccounts;
import com.mymvvmsample.screens.app_abstracts.BaseActivity;
import com.mymvvmsample.screens.base.listeners.AuthenticationListener;
import com.mymvvmsample.screens.base.dialogs.AuthenticationDialog;
import com.mymvvmsample.screens.home_screen.HomeActivity;
import com.mymvvmsample.screens.login_screen.view_model.SignInViewModel;
import com.mymvvmsample.screens.phone_authentication_screen.PhoneAuthenticationScreen;
import com.mymvvmsample.utils.CommonUtils;
import com.mymvvmsample.utils.DeviceId;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import static com.mymvvmsample.data.social_login.LoginViaSocialAccounts.RC_SIGN_IN;

public class SignInActivity extends BaseActivity implements AuthenticationListener, LoginViaSocialAccounts.SocialLoginResponseHandler {
    private static final String TAG = "SignInActivity";
    private Context context;

    @BindView(R.id.term_condition_tv)
    TextView termCondition;

    private SignInViewModel model;
    private LoginViaSocialAccounts socialLoginApi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);

        initializeItems();

        subscribeViewModel();
    }


    private void initializeItems()
    {
        context = this;
        socialLoginApi = new LoginViaSocialAccounts(this, this);

        setHyperLinkText();

        // social login requirements
        socialLoginApi.signOutGoogleAccount();
        socialLoginApi.signOutFBAccount();
    }

    private void setHyperLinkText() {
        termCondition.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "By signing up, you agree to our <font color=\"#000000\"><a href='https://www.google.com'>terms of service</a></font><br/> and <font color=\"#000000\"><a href='https://www.google.com'>privacy policy</a></font>.";
        Spannable s = (Spannable) Html.fromHtml(text);
        for (URLSpan u : s.getSpans(0, s.length(), URLSpan.class)) {
            s.setSpan(new UnderlineSpan() {
                public void updateDrawState(@NonNull TextPaint tp) {
                    tp.setUnderlineText(false);
                }
            }, s.getSpanStart(u), s.getSpanEnd(u), 0);
        }
        termCondition.setText(s);
    }


    private void subscribeViewModel() {
        model = ViewModelProviders.of(this).get(SignInViewModel.class);
        model.fbSignIn().observe(this, new Observer<Resource<String>>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onChanged(@Nullable Resource<String> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        startActivity(new Intent(context, HomeActivity.class));

                        finishAffinity();
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        showSnackBar(termCondition, resource.message);
                        break;
                }
            }
        });

        model.instaSignIn().observe(this, new Observer<Resource<String>>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onChanged(@Nullable Resource<String> resource) {
                if (resource == null) {
                    return;
                }
                switch (resource.status) {
                    case LOADING:
                        break;
                    case SUCCESS:
                        dismissProgressDialog();
                        startActivity(new Intent(context, HomeActivity.class));
                        finishAffinity();
                        break;
                    case ERROR:
                        dismissProgressDialog();
                        showSnackBar(termCondition, resource.message);
                        break;
                }
            }
        });
    }

    @OnClick({R.id.back_btn, R.id.fb_login_btn, R.id.insta_login_btn, R.id.phone_btn})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.back_btn:
                onBackPressed();
                break;

            case R.id.fb_login_btn:
                socialLoginApi.signInWithFacebook();
                break;

            case R.id.insta_login_btn:
                AuthenticationDialog authenticationDialog = AuthenticationDialog.getInstance();
                authenticationDialog.setListener(this);
                authenticationDialog.show(getSupportFragmentManager(), AuthenticationDialog.TAG);

//                socialLoginApi.signInWithGoogle();
                break;

            case R.id.phone_btn:
                startActivity(new Intent(context, PhoneAuthenticationScreen.class));
                break;
        }
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        socialLoginApi.fbOnActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            socialLoginApi.handleSignInResult(data);
        }
    }


    private void callSocialSigIn(Bundle args, String socialType)
    {
    if (!CommonUtils.isNetworkAvailable(context)) {
        hideKeyBoard();
        showSnackBar(termCondition, getString(R.string.no_network));
        return;
    }

    JsonObject obj = new JsonObject();
    obj.addProperty("name", args.getString("name"));
    obj.addProperty("email", args.getString("email"));
    if(socialType.equalsIgnoreCase("FB")) {
        obj.addProperty("facebook_id", args.getString("FB_id"));
    }else if(socialType.equalsIgnoreCase("INSTA")){
        obj.addProperty("insta_id", args.getString("FB_id"));
    }
    obj.addProperty("device_id", DeviceId.getInstance().getDeviceId(context, getUserSession()));
    obj.addProperty("device_type", "A");
    obj.addProperty("device_token", "kdljalsjdskljfalksdjflka");
    obj.addProperty("profile_image", args.getString("profile_pic"));

    if(socialType.equalsIgnoreCase("FB")) {
        model.fbSignIn(obj);
    }else if(socialType.equalsIgnoreCase("INSTA")){
        model.instaSignIn(obj);
    }
    showProgressDialog();
}


    @Override
    public void onTokenReceived(String auth_token) {
        if (auth_token == null)
            return;

        new RequestInstagramAPI().execute(auth_token);
    }

    @Override
    public void onSocialLoginSuccess(Bundle args, String socialType) {
        callSocialSigIn(args, socialType);
    }

    @Override
    public void onSocialLoginFailure() {
        showToast("Something went wrong, Please try again!");
    }


    public class RequestInstagramAPI extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(getResources().getString(R.string.get_user_info_url) + params[0]);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity httpEntity = response.getEntity();
                return EntityUtils.toString(httpEntity);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jsonData = jsonObject.getJSONObject("data");
                    if(jsonData.has("error_message") && jsonData.get("error_message").equals("You are not a sandbox user of this client")){
                        CookieSyncManager.createInstance(context);
                        CookieManager cookieManager = CookieManager.getInstance();
                        cookieManager.removeAllCookie();
                    }

                    Bundle args = new Bundle();
                    if (jsonData.has("id")) {
                        args.putString("name", jsonData.getString("username"));
                        args.putString("INSTA_id", jsonData.getString("id"));
                        if(jsonData.has("email"))
                            args.putString("email", jsonData.getString("email"));
                        args.putString("profile_pic", jsonData.getString("profile_picture"));

                        callSocialSigIn(args, "INSTA");
                    }
                } catch (JSONException e) {
                    Log.e("INSTA_EXP",""+e.toString());
                }
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),"Login error!",Toast.LENGTH_LONG);
                toast.show();
            }

        }
    }
}
