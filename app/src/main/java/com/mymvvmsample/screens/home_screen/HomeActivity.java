package com.mymvvmsample.screens.home_screen;

import android.os.Bundle;
import com.mymvvmsample.R;
import com.mymvvmsample.screens.app_abstracts.BaseActivity;

public class HomeActivity extends BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}
