package com.mymvvmsample.screens.login_screen.view_model;

import com.mymvvmsample.screens.base.view_model.CommonSignInRepo;

public class SignInRepo extends CommonSignInRepo
{

    public static SignInRepo get() {
        return new SignInRepo();
    }

}
