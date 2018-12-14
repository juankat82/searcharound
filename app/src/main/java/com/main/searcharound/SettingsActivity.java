package com.main.searcharound;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import helperclasses.Singleton;

public class SettingsActivity extends AppCompatActivity {

    private static  Context sContext;
    private Toolbar mToolbar;
    private Switch mSwitch;
    private static SharedPreferences mSharedPreferences;
    private TextView mSwitchTextView;

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
        mSharedPreferences=Singleton.getSharedPreferences();
        mSwitchTextView=(TextView) findViewById(R.id.textView_settings_switch);
        SpannableString spannableString=new SpannableString(mSwitchTextView.getText().toString());
        spannableString.setSpan(new UnderlineSpan(),0,spannableString.length(),0);
        mSwitchTextView.setText(spannableString);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setTheme(R.style.MyMaterialTheme);
        mToolbar=(Toolbar)findViewById(R.id.settings_toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.orange));
        mToolbar.setTitle(R.string.settings_string);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mSwitch=(Switch)findViewById(R.id.switch_decimal);
        mSwitch.setChecked(!Singleton.isDecimal());
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    Singleton.setDecimal(false);
                }
                else
                {
                    Singleton.setDecimal(true);
                }
            }
        });
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
    public static SharedPreferences getSharedPreferences()
    {
        return mSharedPreferences;
    }
    public static Context getContext()
    {
        return sContext;
    }

}
