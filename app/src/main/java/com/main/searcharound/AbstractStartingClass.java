package com.main.searcharound;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import helperclasses.Singleton;


public abstract class AbstractStartingClass extends AppCompatActivity {




    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutResId());
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Singleton.setSharedPreferences(mSharedPreferences);
        if (mSharedPreferences.contains("isDecimal"))
            Singleton.setDecimal(true);
        else
            Singleton.setDecimal(false);
    }
    @LayoutRes
    private int getLayoutResId()
    {
        return R.layout.activity_main;
    }

    public abstract Intent newIntent(Context sContext);
}

