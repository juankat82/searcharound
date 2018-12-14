package com.main.searcharound;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

;

public class LogoutActivity extends AppCompatActivity {

    private static Context mContext;
    private static final String DIALOG="Logout Dialog";
    private static final int REQUEST_CODE=0;
    private static final String TAG="LogoutFragment";
    private FragmentManager mFragmentManager;
    private Toolbar mToolbar;

    public Intent newIntent(Context context)
    {
            mContext=context;
            Intent intent=new Intent(LogoutActivity.this,UserScreenActivity.class);
            return intent;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.setActionBar(null);
        setContentView(R.layout.logout_layout);
        mToolbar=(Toolbar) findViewById(R.id.logout_toolbar);
        setTheme(R.style.MyMaterialTheme);
        mToolbar.setBackgroundColor(getResources().getColor(R.color.orange));
        mToolbar.setTitle(R.string.logout_string);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configure the alertdialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_DARK);
        builder.setTitle(R.string.logout_string);
        builder.setMessage(R.string.logout_question_string);
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Here check the firebase user and log it out. Replace next code
                finish();
            }
        });
        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).create();
        builder.show();

//        LogoutDialogFragment dialogFragment=new LogoutDialogFragment();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return true;
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
}
