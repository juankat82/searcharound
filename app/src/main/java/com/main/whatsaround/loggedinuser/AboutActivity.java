package com.main.whatsaround.loggedinuser;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import com.main.whatsaround.logginandsignup.R;

public class AboutActivity extends AppCompatActivity {

    private static Context sContext;
    private Toolbar mToolbar;

    public Intent newIntent(Context context)
    {
        sContext=context;
        return new Intent(AboutActivity.this,UserScreenActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.about_layout);
        mToolbar=(Toolbar)findViewById(R.id.about_toolbar);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.orange));
        mToolbar.setTitle(R.string.about_string);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.about_linear_layout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return true;
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
    //creates fadein and fadeout animation
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