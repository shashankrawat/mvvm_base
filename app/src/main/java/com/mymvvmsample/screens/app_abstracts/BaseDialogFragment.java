package com.mymvvmsample.screens.app_abstracts;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.mymvvmsample.data.app_prefs.UserSession;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseDialogFragment extends DialogFragment
{
    private Unbinder unbinder;
    private BaseActivity activity;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (BaseActivity) getActivity();
    }

    protected View initUnbinder(@NonNull View v) {
        unbinder = ButterKnife.bind(this, v);
        return v;
    }

    // get User sessions
    public UserSession getUserSession() {
        if(activity != null){
            return activity.getUserSession();
        }
        return  null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }
}
