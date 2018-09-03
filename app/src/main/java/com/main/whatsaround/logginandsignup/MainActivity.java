package com.main.whatsaround.logginandsignup;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.SupportActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;



public class MainActivity extends AppCompatActivity {

    private String TAG="MainFragment";
    private Button mLoginButton;
    private Button mRegisterButton;
    private static Context mContext;


    public Intent newIntent(Context context)
    {
        mContext=context;
        //change UserscreenActivity for BoneActivity
        Intent intent=new Intent(MainActivity.this,BoneActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.login_layout);

        mLoginButton = (Button)findViewById(R.id.login_button);
        mRegisterButton = (Button)findViewById(R.id.signup_button);
        // mFragmentManager = getSupportFragmentManager();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                mFragmentManager.beginTransaction().replace(R.id.main_activity_layout, SignupFragment.getFragment()).commit();
            }
        });
    }


}
