package com.mymvvmsample.screens.phone_authentication_screen;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.mymvvmsample.R;
import com.mymvvmsample.screens.app_abstracts.BaseActivity;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneAuthenticationScreen extends BaseActivity
{
    private Context context;
    private static final String TAG = "PhoneAuth";
    @BindView(R.id.phone_number_et)
    EditText phoneET;
    @BindView(R.id.pass_code_et)
    EditText codeET;
    @BindView(R.id.authenticate_btn)
    Button authBtn;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_authentication_screen);
        ButterKnife.bind(this);

        initViews();
    }

    private void initViews() {
        context = this;
    }

    @OnClick({R.id.authenticate_btn})
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.authenticate_btn:
                if(authBtn.getText().toString().equalsIgnoreCase(getString(R.string.send_code)))
                {
                    if(!TextUtils.isEmpty(phoneET.getText().toString())) {
                        if(phoneET.getText().length() >= 10){
                            codeET.setVisibility(View.VISIBLE);
                            authBtn.setText(getString(R.string.verify));
                            codeET.requestFocus();
                        }else {
                            showSnackBar(authBtn, "Please enter a valid phone number");
                        }
                    }else {
                        showSnackBar(authBtn, "Please enter a valid phone number");
                    }
                }
                else if(authBtn.getText().toString().equalsIgnoreCase(getString(R.string.verify)))
                {

                }
                break;
        }
    }

    private void phoneAuthenticationMethod(String phoneNumber){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }


    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            Log.d(TAG, "onVerificationCompleted: " + phoneAuthCredential);
//            signInWithPhoneAuthCredential(credential);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

        }
    };
}
