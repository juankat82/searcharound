package com.main.whatsaround.loggedinuser;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;


import com.main.whatsaround.logginandsignup.R;

import java.util.zip.Inflater;

public class SettingsActivity extends AppCompatActivity {

    private static  Context sContext;
    private Toolbar mToolbar;

    public Intent newIntent(Context context)
    {
        sContext=context;
        Intent intent=new Intent(SettingsActivity.this,UserScreenActivity.class);
        return intent;

    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);
        //overridePendingTransition(R.anim.enter_from_left,R.anim.exit_to_right);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTheme(R.style.MyMaterialTheme);
        mToolbar=(Toolbar)findViewById(R.id.settings_toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.orange));
        mToolbar.setTitle(R.string.settings_string);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        onStartNewActivity();
    }
    @Override
    public void finish() {
        super.finish();
        onLeaveThisActivity();
    }

    protected void onStartNewActivity()
    {
        overridePendingTransition(R.anim.enter_from_right,R.anim.exit_to_left);
    }
    protected void onLeaveThisActivity() {
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }
    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
    }

}
